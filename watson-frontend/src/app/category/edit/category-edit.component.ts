import {Component, Input} from '@angular/core';
import {CategorySummary} from '../category-summary';

@Component({
  selector: 'ws-category-edit',
  template: `
    <input [(ngModel)]="_category.name" placeholder="Nazwa" class="category-name"/>
    <ws-select
      *ngIf="_category.uuid !== 'root'"
      [(ngModel)]="parentCategory"
      (onChange)="onParentCategoryChange($event)"
      [data]="filteredCategories || _categories"
      [displayField]="'name'"
      [allowNewValues]="false"
      [placeholder]="'Kategoria nadrzędna'"
      class="category-parent-category">
      <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
        <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
        <div *ngIf="item.path" class="select-item-description">
          {{item.path | joinArray: ' > '}}
        </div>
      </ng-template>
    </ws-select>
  `,
  styleUrls: [
    'category-edit.component.scss'
  ]
})
export class CategoryEditComponent {

  _categories: CategorySummary[] = [];
  _category: Partial<CategorySummary> = {};
  filteredCategories: CategorySummary[] = null;
  parentCategory: Partial<CategorySummary> = null;

  @Input() set category(category: CategorySummary) {
    this._category = category;
    if (this._categories && this._categories.length > 0) {
      this.afterCategoryAndCategories();
    }
  }

  @Input() set categories(categories: CategorySummary[]) {
    this._categories = categories;
    if (this._category) {
      this.afterCategoryAndCategories();
    }
  }

  private afterCategoryAndCategories() {
    this.filteredCategories = this._categories.filter(category => category.uuid !== this._category.uuid);
    if (this._category.parentUuid) {
      this.parentCategory = this._categories.find(category => category.uuid === this._category.parentUuid);
    } else {
      this.parentCategory = null;
    }
  }

  onParentCategoryChange(parentCategory: CategorySummary) {
    this._category.parentUuid = parentCategory.uuid;
  }

}
