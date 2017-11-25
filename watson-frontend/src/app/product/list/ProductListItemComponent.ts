import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CategorySummary} from '../../category/CategorySummary';
import {ProductSummary} from '../ProductSummary';
import {ModifyEvent} from '../ModifyEvent';

@Component({
  selector: 'product-list-item-component',
  template: `
    <div *ngIf="state == ItemState.SUMMARY">
      {{product.name}} ({{product.category.path}})
      <a (click)="editClicked()">(edytuj)</a>
      <a (click)="deleteClicked()">(usuń)</a>
    </div>
    <div *ngIf="state == ItemState.EDIT">
      <edit-product-component
        [categories]="categories"
        [product]="product"
        (onCancel)="cancelClicked()"
        (onSave)="saveClicked($event)">
      </edit-product-component>
    </div>
  `
})
export class ProductListItemComponent {

  @Input() categories: CategorySummary[];
  @Input() product: ProductSummary;
  @Output() onSave: EventEmitter<ModifyEvent<ProductSummary>> = new EventEmitter<ModifyEvent<ProductSummary>>();
  @Output() onCancel: EventEmitter<ProductSummary> = new EventEmitter<ProductSummary>();
  @Output() onDelete: EventEmitter<ProductSummary> = new EventEmitter<ProductSummary>();
  ItemState = { SUMMARY: 'summary', EDIT: 'edit' };
  state = this.ItemState.SUMMARY;

  editClicked() {
    this.state = this.ItemState.EDIT;
  }

  cancelClicked() {
    this.state = this.ItemState.SUMMARY;
    this.onCancel.next(this.product);
  }

  saveClicked(product: ProductSummary) {
    this.state = this.ItemState.SUMMARY;
    this.onSave.next({
      newValue: product,
      oldValue: this.product
    });
  }

  deleteClicked() {
    this.onDelete.next(this.product);
  }

}
