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
      <ws-product-price-report-panel *ngIf="report; else spinner"
                                     [report]="report"
                                     [includeSubcategories]="includeSubcategories"
                                     (includeSubcategoriesChange)="onIncludeSubcategoriesChanged($event)">
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
  includeSubcategories = false;
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
      .subscribe(categoryUuid => {
        this.categoryUuid = categoryUuid
        this.fetchReport();
      });
  }

  onCategorySelected(category: CategoryTreeDto) {
    this.router.navigate(['report/product-price/', category !== undefined ? category.uuid : 'root']);
  }

  onIncludeSubcategoriesChanged(includeSubcategories: boolean) {
    console.log({
      method: 'onIncludeSubcategoriesChanged',
      includeSubcategories
    });
    this.includeSubcategories = includeSubcategories;
    this.fetchReport();
  }

  private fetchReport() {
    if (this.getReportSubscription) {
      this.getReportSubscription.unsubscribe();
    }
    this.report = undefined;
    this.getReportSubscription = this.httpClient
      .get<ProductPriceReport>(
        `/api/v1/product-price/report/${this.categoryUuid}`,
        {
          params: {
            includeSubcategories: this.includeSubcategories ? 'true' : 'false'
          }
        }
      )
      .subscribe(report => this.report = report);
  }

}
