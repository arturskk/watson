import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../product-summary';
import {CategorySummary} from '../../category/category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {CrudHelper, CrudResource} from '../../util/crud-helper';

@Component({
  selector: 'ws-product-list',
  template: `
    <h1>Produkty</h1>
    <ws-panel>
      <h2>Dodaj produkt</h2>
      <div *ngIf="categories">
        <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="resource.added($event)">
          <ng-template let-product #itemEdit>
            <ws-product-edit [product]="product" [categories]="categories"></ws-product-edit>
          </ng-template>
        </ws-crud-item-component>
      </div>
    </ws-panel>
    <ws-panel>
      <h2>Lista produktów</h2>
      <ws-crud-list-component [data]="products" (itemSave)="resource.edited($event)">
        <ng-template let-product #itemSummary>
          {{product.name}}
          <span class="category">&nbsp;({{product.category.path | joinArray:' > '}})</span>
        </ng-template>
        <ng-template let-product #itemEdit>
          <ws-product-edit [product]="product" [categories]="categories"></ws-product-edit>
        </ng-template>
      </ws-crud-list-component>
    </ws-panel>
  `,
  styleUrls: [
    'product-list.component.scss'
  ],
  providers: [
    CrudHelper
  ]
})
export class ProductListComponent implements OnInit {

  products: ProductSummary[];
  categories: CategorySummary[];
  State = CrudItemState;
  resource: CrudResource<CategorySummary>;

  constructor(private httpClient: HttpClient, crudHelper: CrudHelper<CategorySummary>) {
    this.resource = crudHelper.asResource({
      api: '/api/v1/product',
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name: 'name',
        categoryUuid: 'category.uuid',
        defaultUnit: 'defaultUnit'
      }),
      onSuccess: this.fetchProducts.bind(this)
    });
  }

  ngOnInit(): void {
    this.httpClient
      .get<CategorySummary[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
    this.fetchProducts();
  }

  private fetchProducts() {
    return this.httpClient
      .get<ProductSummary[]>('/api/v1/product')
      .subscribe(data => this.products = data);
  }

}
