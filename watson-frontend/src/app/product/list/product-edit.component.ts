import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CategorySummary} from '../../category/category-summary';
import {ProductSummary} from '../product-summary';
import {ObjectsUtil} from '../../util/objects-util';

@Component({
  selector: 'ws-product-edit',
  template: `
    <div class="edit">
      <input class="product-name" [(ngModel)]="value.name" placeholder="Nazwa"/>
      <ws-select
        class="product-category"
        [(ngModel)]="value.category"
        [data]="categories"
        [displayField]="'name'"
        [allowNewValues]="false"
        [placeholder]="'Kategoria'">
        <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
          <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
        </ng-template>
      </ws-select>
    </div>
    <div class="actions">
      <ws-button-flat (click)="resetClicked()" *ngIf="resettable">wyczyść</ws-button-flat>
      <ws-button-flat (click)="cancelClicked()" *ngIf="cancelable">anuluj</ws-button-flat>
      <ws-button-flat (click)="saveClicked()">zapisz</ws-button-flat>
    </div>
  `
})
export class ProductEditComponent {

  @Input() cancelable = true;
  @Input() resettable = false;
  @Input() categories: CategorySummary[] = [];
  @Output() onSave = new EventEmitter<any>();
  @Output() onCancel = new EventEmitter<void>();
  value: Partial<ProductSummary> = {};
  original: Partial<ProductSummary> = {};

  @Input() set product(product: ProductSummary) {
    this.original = product;
    this.reset(this.original);
  }

  saveClicked() {
    this.onSave.next(this.value);
  }

  cancelClicked() {
    this.onCancel.next();
  }

  resetClicked() {
    this.reset(this.original);
  }

  private reset(source: Partial<ProductSummary>) {
    this.value = ObjectsUtil.deepCopy(source);
  }

}
