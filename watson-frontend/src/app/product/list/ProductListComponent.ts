import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../ProductSummary';
import {CategorySummary} from '../../category/CategorySummary';
import {DiffUtil} from '../../util/DiffUtil';
import {ModifyEvent} from '../ModifyEvent';

@Component({
  selector: 'product-list-component',
  template: `
    <h1>Produkty</h1>
    <h2>Dodaj produkt</h2>
    <div *ngIf="categories" class="add-new-category">
      <edit-product-component
        [cancelable]="false"
        [resettable]="true"
        (onSave)="onAddItem($event)"
        [categories]="categories">
      </edit-product-component>
    </div>
    <h2>Lista produktów</h2>
    <div>
      <div *ngFor="let product of products">
        <product-list-item-component
          (onSave)="onItemChange($event)"
          [categories]="categories"
          [product]="product">
        </product-list-item-component>
      </div>
    </div>
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
    console.log({
      product
    });
  }

  onItemChange(change: ModifyEvent<ProductSummary>) {
    const diff = DiffUtil.diff(change.newValue, change.oldValue, {
      name: 'name',
      categoryUuid: 'category.uuid'
    });
    this.httpClient
      .put(`/api/v1/product/${change.oldValue.uuid}`, diff)
      .subscribe(() => {
        if (diff.name) {
          change.oldValue.name = change.newValue.name;
        }
        if (diff.categoryUuid) {
          change.oldValue.category = change.newValue.category;
        }
        alert('Zmodyfikowano!');
      });
  }

}
