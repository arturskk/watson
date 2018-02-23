import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../product-summary';
import {CategorySummary} from '../../category/category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudConfig} from '../../widgets/crud/crud-config';

@Component({
  selector: 'ws-product-list',
  template: `
    <h1>Produkty</h1>
    <ws-crud [config]="crudConfig" *ngIf="categories; else spinner">
      <ng-template let-product #itemSummary>
        {{product.name}}
        <span class="category">&nbsp;({{product.category.path | joinArray:' > '}})</span>
      </ng-template>
      <ng-template let-product #itemEdit>
        <ws-product-edit [product]="product" [categories]="categories"></ws-product-edit>
      </ng-template>
    </ws-crud>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'product-list.component.scss'
  ]
})
export class ProductListComponent implements OnInit {

  readonly crudConfig: CrudConfig<ProductSummary> = {
    keys: {
      itemAddKey: 'Dodaj produkt',
      itemListKey: 'Lista produktów'
    },
    api: {
      endpoint: '/api/v1/product'
    },
    model: {
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name: 'name',
        categoryUuid: 'category.uuid',
        defaultUnit: 'defaultUnit'
      })
    }
  };

  categories: CategorySummary[];

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<CategorySummary[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
  }

}
