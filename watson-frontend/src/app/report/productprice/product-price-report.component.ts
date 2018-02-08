import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Subscription} from 'rxjs/Subscription';
import {ProductPriceReport} from './product-price-report';

@Component({
  selector: 'ws-product-price-report',
  template: `
    <ws-panel>
      <ng-container *ngIf="report; else spinner">
        <ws-product-price-report-category-tree>
        </ws-product-price-report-category-tree>
        <ws-product-price-report-panel [report]="report">
        </ws-product-price-report-panel>
      </ng-container>
    </ws-panel>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'product-price-report.component.scss'
  ]
})
export class ProductPriceReportComponent  implements OnInit {

  private getReportSubscription: Subscription;
  report: ProductPriceReport;

  constructor(private httpClient: HttpClient, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route
      .paramMap
      .map(map => map.get('category'))
      .subscribe(this.onCategoryChanged.bind(this));
  }

  private onCategoryChanged(categoryUuid: string = 'root') {
    if (this.getReportSubscription) {
      this.getReportSubscription.unsubscribe();
    }
    this.report = undefined;
    this.getReportSubscription = this.httpClient
      .get<ProductPriceReport>(`/api/v1/product-price/report/${categoryUuid}`)
      .subscribe(report => this.report = report);
  }

}
