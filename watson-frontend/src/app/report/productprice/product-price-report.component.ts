import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {CategoryTreeDto} from '../../category/list/category-tree-dto';
import {ProductPriceReport} from './product-price-report';

@Component({
  selector: 'ws-product-price-report',
  template: `
    <ws-panel>
      <ws-product-price-report-category-tree [categoryUuid]="categoryUuid" 
                                             (categorySelected)="onCategorySelected($event)">
      </ws-product-price-report-category-tree>
      <ws-product-price-report-panel *ngIf="report; else spinner" [report]="report">
      </ws-product-price-report-panel>
    </ws-panel>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'product-price-report.component.scss'
  ]
})
export class ProductPriceReportComponent implements OnInit {

  report: ProductPriceReport;
  categoryUuid: string;
  private getReportSubscription: Subscription;

  constructor(
    private httpClient: HttpClient,
    private route: ActivatedRoute,
    private router: Router) {
  }

  ngOnInit(): void {
    this.route
      .paramMap
      .map(map => map.get('category'))
      .subscribe(this.onCategoryChanged.bind(this));
  }

  onCategorySelected(category: CategoryTreeDto) {
    this.router.navigate(['report/product-price/', category !== undefined ? category.uuid : 'root']);
  }

  private onCategoryChanged(categoryUuid: string = 'root') {
    if (this.getReportSubscription) {
      this.getReportSubscription.unsubscribe();
    }
    this.categoryUuid = categoryUuid;
    this.report = undefined;
    this.getReportSubscription = this.httpClient
      .get<ProductPriceReport>(`/api/v1/product-price/report/${categoryUuid}`)
      .subscribe(report => this.report = report);
  }

}
