import {Component, Input, OnInit} from '@angular/core';
import {ObjectsUtil} from '../../../../util/objects-util';
import {ProductPriceReport} from '../../product-price-report';

@Component({
  selector: 'ws-product-price-report-panel-data-table',
  template: `
    <div class="header row">
      <div *ngFor="let column of columns" [class]="'column column-' + column.span">{{column.key}}</div>
    </div>
    <div class="data">
      <div *ngFor="let item of report.items" class="item row">
        <div *ngFor="let column of columns" [class]="'column column-' + column.span">{{byProperty(item, column.field)}}{{column.valueSuffix}}</div>
      </div>
    </div>
  `,
  styleUrls: ['./product-price-report-panel-data-table.component.scss']
})
export class ProductPriceReportPanelDataTableComponent {

  readonly byProperty = ObjectsUtil.getByProperty;
  @Input() report: ProductPriceReport;
  columns = [
    {key: 'Produkt', span: 6, field: 'product.name'},
    {key: 'Sklep', span: 4, field: 'shop.name'},
    {key: 'Data', span: 2, field: 'date'},
    {key: 'Jednostka', span: 1, field: 'unit'},
    {key: 'Cena', span: 1, field: 'pricePerUnit', valueSuffix: 'zł'}
  ];

}
