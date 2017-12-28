import {Component, EventEmitter, Input, Output} from '@angular/core';
import {CategorySummary} from '../category-summary';
import {ObjectsUtil} from '../../util/objects-util';

@Component({
  selector: 'ws-category-edit',
  template: `
    <input [(ngModel)]="value.name" placeholder="Nazwa"/>
    <ws-select
      *ngIf="value.uuid !== 'root'"
      [(ngModel)]="parentCategory"
      [data]="filteredCategories || categories"
      [displayField]="'name'"
      [allowNewValues]="false"
      [placeholder]="'Kategoria nadrzędna'">
      <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
        <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
      </ng-template>
    </ws-select>
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
export class CategoryEditComponent {

  @Input() cancelable = true;
  @Input() resettable = false;
  @Input() categories: CategorySummary[] = [];
  @Output() onSave = new EventEmitter<any>();
  @Output() onCancel = new EventEmitter<void>();
  value: Partial<CategorySummary> = {};
  original: Partial<CategorySummary> = {};
  parentCategory: Partial<CategorySummary>;
  filteredCategories: CategorySummary[];

  @Input() set category(category: CategorySummary) {
    this.original = category;
    this.reset(this.original);
  }

  saveClicked() {
    if (this.parentCategory) {
      this.value.parentUuid = this.parentCategory.uuid;
    }
    this.onSave.next(this.value);
  }

  cancelClicked() {
    this.onCancel.next();
  }

  resetClicked() {
    this.reset(this.original);
  }

  private reset(source: Partial<CategorySummary>) {
    this.value = ObjectsUtil.deepCopy(source);
    this.filteredCategories = this.categories.filter(category => category.uuid || category.uuid !== source.uuid);
    if (source.parentUuid) {
      this.parentCategory = this.categories.find(category => category.uuid === source.parentUuid);
    } else {
      this.parentCategory = null;
    }
  }

}
