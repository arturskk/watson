import {Component, Input} from '@angular/core';
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
        <div *ngFor="let column of columns" [class]="'column column-' + column.span">
          {{column.renderer(item)}}
        </div>
      </div>
    </div>
  `,
  styleUrls: ['./product-price-report-panel-data-table.component.scss']
})
export class ProductPriceReportPanelDataTableComponent {

  readonly byProperty = ObjectsUtil.getByProperty;
  @Input() report: ProductPriceReport;
  columns = [
    {key: 'Produkt', span: 3, renderer: item => this.byProperty(item, 'product.name')},
    {key: 'Producent', span: 2, renderer: item => this.byProperty(item, 'product.producerName' || '')},
    {key: 'Sieć', span: 2, renderer: item => (this.byProperty(item, 'shop.retailChainName') || '')},
    {key: 'Sklep', span: 2, renderer: item => this.byProperty(item, 'shop.name')},
    {key: 'Data', span: 2, renderer: item => this.byProperty(item, 'date')},
    {key: 'Jedn.', span: 1, renderer: item => this.byProperty(item, 'unit')},
    {key: 'Cena', span: 1, renderer: item => this.byProperty(item, 'pricePerUnit') + 'zł'}
  ];

}
