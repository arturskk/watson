import {Component, Input} from '@angular/core';
import {CategorySummary} from '../../category/category-summary';
import {ProducerSummary} from '../../producer/producer-summary';
import {ProductSummary} from '../product-summary';

@Component({
  selector: 'ws-product-edit',
  template: `
    <input class="product-name" [(ngModel)]="product.name" placeholder="Nazwa"/>
    <ws-select
      class="product-category"
      [(ngModel)]="product.category"
      [data]="categories"
      [displayField]="'name'"
      [allowNewValues]="false"
      [placeholder]="'Kategoria'">
      <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
        <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
        <div *ngIf="item.path" class="select-item-description">
          {{item.path | joinArray: ' > '}}
        </div>
      </ng-template>
    </ws-select>
    <ws-select
      class="product-producer"
      [(ngModel)]="product.producer"
      [data]="producers"
      [displayField]="'name'"
      [allowNewValues]="false"
      [placeholder]="'Producent'">
      <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
        <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
      </ng-template>
    </ws-select>
    <select [(ngModel)]="product.defaultUnit">
      <option></option>
      <option>op</option>
      <option>szt</option>
      <option>kg</option>
      <option>l</option>
    </select>
  `,
  styleUrls: [
    'product-edit.component.scss'
  ]
})
export class ProductEditComponent {

  @Input() categories: CategorySummary[] = [];
  @Input() producers: ProducerSummary[] = [];
  @Input() product: Partial<ProductSummary> = {};

}
