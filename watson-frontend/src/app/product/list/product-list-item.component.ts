import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CategorySummary} from '../../category/category-summary';
import {ProductSummary} from '../product-summary';
import {ModifyEvent} from '../modify-event';

@Component({
  selector: 'ws-product-list-item',
  template: `
    <ng-container *ngIf="state == ItemState.SUMMARY">
      <div class="item-summary">{{product.name}} ({{product.category.path}})</div>
      <div class="item-actions">
        <ws-button-flat (click)="editClicked()">edytuj</ws-button-flat>
        <!--<ws-button-flat (click)="deleteClicked()">usuń</ws-button-flat>-->
      </div>
    </ng-container>
    <ng-container *ngIf="state == ItemState.EDIT">
      <ws-product-edit
        [categories]="categories"
        [product]="product"
        (onCancel)="cancelClicked()"
        (onSave)="saveClicked($event)">
      </ws-product-edit>
    </ng-container>
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
