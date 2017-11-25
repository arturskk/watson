import {Component, EventEmitter, Input, Output} from '@angular/core';
import {ObjectUtil} from '../../util/ObjetUtil';
import {CategorySummary} from '../../category/CategorySummary';
import {ProductSummary} from '../ProductSummary';

@Component({
  selector: 'edit-product-component',
  template: `
    <input [(ngModel)]="value.name" placeholder="Nazwa"/>
    <select-component
      [(ngModel)]="value.category"
      [data]="categories"
      [displayField]="'name'"
      [allowNewValues]="false"
      [placeholder]="'Kategoria'">
      <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
        <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
      </ng-template>
    </select-component>
    <a (click)="resetClicked()" *ngIf="resettable">(wyczyść)</a>
    <a (click)="cancelClicked()" *ngIf="cancelable">(anuluj)</a>
    <a (click)="saveClicked()">(zapisz)</a>
  `,
  styles: [
      `
      :host {
        display: flex;
      }
    `
  ]
})
export class EditProductComponent {

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
    this.value = ObjectUtil.deepCopy(source);
  }

}
