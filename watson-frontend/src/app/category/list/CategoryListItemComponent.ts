import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CategorySummary} from '../../category/CategorySummary';
import {ModifyEvent} from '../../product/ModifyEvent';

@Component({
  selector: 'category-list-item-component',
  template: `
    <div *ngIf="state == ItemState.SUMMARY">
      {{category.name}} ({{category.path}})
      <a (click)="editClicked()">(edytuj)</a>
      <!--<a (click)="deleteClicked()">(usuń)</a>-->
    </div>
    <div *ngIf="state == ItemState.EDIT">
      <edit-category-component
        [categories]="categories"
        [category]="category"
        (onCancel)="cancelClicked()"
        (onSave)="saveClicked($event)">
      </edit-category-component>
    </div>
  `
})
export class CategoryListItemComponent {

  @Input() categories: CategorySummary[];
  @Input() category: CategorySummary;
  @Output() onSave: EventEmitter<ModifyEvent<CategorySummary>> = new EventEmitter<ModifyEvent<CategorySummary>>();
  @Output() onCancel: EventEmitter<CategorySummary> = new EventEmitter<CategorySummary>();
  @Output() onDelete: EventEmitter<CategorySummary> = new EventEmitter<CategorySummary>();
  ItemState = { SUMMARY: 'summary', EDIT: 'edit' };
  state = this.ItemState.SUMMARY;

  editClicked() {
    this.state = this.ItemState.EDIT;
  }

  cancelClicked() {
    this.state = this.ItemState.SUMMARY;
    this.onCancel.next(this.category);
  }

  saveClicked(category: CategorySummary) {
    this.state = this.ItemState.SUMMARY;
    this.onSave.next({
      newValue: category,
      oldValue: this.category
    });
  }

  deleteClicked() {
    this.onDelete.next(this.category);
  }

}
