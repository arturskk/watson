import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../product-summary';
import {CategorySummary} from '../../category/category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemSave} from '../../widgets/crud-list/crud-item-save';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';

@Component({
  selector: 'ws-product-list',
  template: `
    <h1>Produkty</h1>
    <ws-panel>
      <h2>Dodaj produkt</h2>
      <div *ngIf="categories">
        <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="newProductSave($event)">
          <ng-template let-product #itemEdit>
            <ws-product-edit [product]="product" [categories]="categories"></ws-product-edit>
          </ng-template>
        </ws-crud-item-component>
      </div>
    </ws-panel>
    <ws-panel>
      <h2>Lista produktów</h2>
      <ws-crud-list-component [data]="products" (itemSave)="editProductSave($event)">
        <ng-template let-product #itemSummary>
          {{product.name}}
          <span class="category">&nbsp;({{product.category.path}})</span>
        </ng-template>
        <ng-template let-product #itemEdit>
          <ws-product-edit [product]="product" [categories]="categories"></ws-product-edit>
        </ng-template>
      </ws-crud-list-component>
    </ws-panel>
  `,
  styleUrls: [
    'product-list.component.scss'
  ]
})
export class ProductListComponent implements OnInit {

  products: ProductSummary[];
  categories: CategorySummary[];
  State = CrudItemState;

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<CategorySummary[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
    this.fetchProducts();
  }

  newProductSave(crudItemSave: CrudItemSave<ProductSummary>) {
    this.httpClient
      .post(`/api/v1/product`, {
        name: crudItemSave.changed.name,
        categoryUuid: crudItemSave.changed.category ? crudItemSave.changed.category.uuid : undefined
      })
      .subscribe(
        () => {
          crudItemSave.commit({
            value: {},
            state: CrudItemState.EDIT
          });
          this.fetchProducts();
        },
        response => crudItemSave.rollback({
          message: response.error.errors.map(error => `${error.field} ${error.defaultMessage}`)
        })
      );
  }

  editProductSave(crudItemSave: CrudItemSave<ProductSummary>) {
    this.httpClient
      .put(
        `/api/v1/product/${crudItemSave.item.uuid}`,
        DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
          name: 'name',
          categoryUuid: 'category.uuid'
        })
      )
      .subscribe(
        crudItemSave.commit,
        response => crudItemSave.rollback({
          message: response.error.message
        })
      );
  }

  private fetchProducts() {
    return this.httpClient
      .get<ProductSummary[]>('/api/v1/product')
      .subscribe(data => this.products = data);
  }

}
