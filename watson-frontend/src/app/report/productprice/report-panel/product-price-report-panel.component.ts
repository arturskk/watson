import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ProductPriceReport} from '../product-price-report';

@Component({
  selector: 'ws-product-price-report-panel',
  template: `
    <ws-product-price-report-panel-filter>
    </ws-product-price-report-panel-filter>
    <ws-product-price-report-panel-options [includeSubcategories]="includeSubcategories" 
                                           (includeSubcategoriesChange)="includeSubcategoriesChange.next($event)">
    </ws-product-price-report-panel-options>
    <ws-product-price-report-panel-data-table [report]="report">
    </ws-product-price-report-panel-data-table>
    <ws-product-price-report-panel-summary>
    </ws-product-price-report-panel-summary>
  `,
  styleUrls: [
    'product-price-report-panel.component.scss'
  ]
})
export class ProductPriceReportPanelComponent {

  @Input() report: ProductPriceReport;
  @Input() includeSubcategories: boolean;
  @Output() includeSubcategoriesChange = new EventEmitter();

}
