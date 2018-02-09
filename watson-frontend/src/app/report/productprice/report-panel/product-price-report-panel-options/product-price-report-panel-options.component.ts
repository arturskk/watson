import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'ws-product-price-report-panel-options',
  template: `
    <label>
      <input type="checkbox"
             [checked]="includeSubcategories"
             (change)="includeSubcategoriesChange.next(!includeSubcategories)"/>
      Pokaż produkty z podkategorii
    </label>
  `,
  styleUrls: ['./product-price-report-panel-options.component.scss']
})
export class ProductPriceReportPanelOptionsComponent {

  @Input() includeSubcategories: boolean;
  @Output() includeSubcategoriesChange = new EventEmitter();

}
