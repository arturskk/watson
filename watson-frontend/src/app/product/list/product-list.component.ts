import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../product-summary';
import {CategorySummary} from '../../category/category-summary';
import {ModifyEvent} from '../modify-event';
import {DiffsUtil} from '../../util/diffs-util';

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
      <div>
        <div *ngFor="let product of products">
          <ws-product-list-item
            (onSave)="onItemChange($event)"
            [categories]="categories"
            [product]="product">
          </ws-product-list-item>
        </div>
      </div>
    </ws-panel>
  `,
  styles: []
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

  onItemChange(change: ModifyEvent<ProductSummary>) {
    const diff = DiffsUtil.diff(change.newValue, change.oldValue, {
      name: 'name',
      categoryUuid: 'category.uuid'
    });
    this.httpClient
      .put(`/api/v1/product/${change.oldValue.uuid}`, diff)
      .subscribe(() => {
        window.location.reload();
      });
  }

}
