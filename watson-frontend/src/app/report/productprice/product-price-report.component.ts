import {Component} from '@angular/core';

@Component({
  selector: 'ws-product-price-report',
  template: `
    <ws-panel>
      <ws-product-price-report-category-tree>
      </ws-product-price-report-category-tree>
      <ws-product-price-report-panel>
      </ws-product-price-report-panel>
    </ws-panel>
  `,
  styleUrls: [
    'product-price-report.component.scss'
  ]
})
export class ProductPriceReportComponent {
}
