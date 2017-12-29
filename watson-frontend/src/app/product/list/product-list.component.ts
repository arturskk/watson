import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../product-summary';
import {CategorySummary} from '../../category/category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemSave} from '../../widgets/crud-list/crud-item-save';

@Component({
  selector: 'ws-product-list',
  template: `
    <h1>Produkty</h1>
    <ws-panel>
      <h2>Dodaj produkt</h2>
      <div *ngIf="categories">
        <ws-product-edit
          [cancelable]="false"
          [resettable]="true"
          (onSave)="onAddItem($event)"
          [categories]="categories">
        </ws-product-edit>
      </div>
    </ws-panel>
    <ws-panel>
      <h2>Lista produktów</h2>
      <ws-crud-list-component [data]="products" (itemSave)="productSave($event)">
        <ng-template let-product #itemSummary>
          {{product.name}} ({{product.category.path}})
        </ng-template>
        <ng-template let-product #itemEdit>
          <input class="product-name" [(ngModel)]="product.name" placeholder="Nazwa"/>
          <ws-select
            class="product-category"
            [(ngModel)]="product.category"
            [data]="categories"
            [displayField]="'name'"
            [allowNewValues]="false"
            [placeholder]="'Kategoria'">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
            </ng-template>
          </ws-select>
        </ng-template>
      </ws-crud-list-component>
    </ws-panel>
  `
})
export class ProductListComponent implements OnInit {

  products: ProductSummary[];
  categories: CategorySummary[];

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<CategorySummary[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
    this.httpClient
      .get<ProductSummary[]>('/api/v1/product')
      .subscribe(data => this.products = data);
  }

  onAddItem(product: ProductSummary) {
    this.httpClient
      .post(`/api/v1/product`, {
        name: product.name,
        categoryUuid: product.category ? product.category.uuid : undefined
      })
      .subscribe(() => {
        window.location.reload();
      });
  }

  productSave(crudItemSave: CrudItemSave<ProductSummary>) {
    this.httpClient
      .put(
        `/api/v1/product/${crudItemSave.item.uuid}`,
        DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
          name: 'name',
          categoryUuid: 'category.uuid'
        })
      )
      .subscribe(crudItemSave.commit);
  }

}
