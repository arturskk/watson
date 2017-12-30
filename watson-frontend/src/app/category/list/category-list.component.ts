import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {CategorySummary} from '../category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemSave} from '../../widgets/crud-list/crud-item-save';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {CategoryTreeDto, CategoryTreeDtoData} from './category-tree-dto';

@Component({
  selector: 'ws-category-list',
  template: `
    <h1>Kategorie</h1>
    <ng-container *ngIf="categories">
      <ws-panel>
        <h2>Dodaj kategorię</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="newCategorySave($event)">
            <ng-template let-category #itemEdit>
              <ws-category-edit [category]="category" [categories]="categories"></ws-category-edit>
            </ng-template>
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista kategorii</h2>
        <ws-crud-list-component [data]="categories" (itemSave)="editCategorySave($event)">
          <ng-template let-category #itemSummary>
            <span *ngFor="let divider of category.path" class="depth-divider"></span>
            {{category.name}}
            <span class="category-path" *ngIf="category.path.length > 0">&nbsp;({{category.path | joinArray:' > '}})</span>
          </ng-template>
          <ng-template let-category #itemEdit>
            <ws-category-edit [category]="category" [categories]="categories"></ws-category-edit>
          </ng-template>
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `
})
export class CategoryListComponent implements OnInit {

  categories: CategorySummary[];
  State = CrudItemState;
  private type: string;

  constructor(private httpClient: HttpClient, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.route
      .paramMap
      .map(map => map.get('type'))
      .subscribe(data => this.type = `_category_${data}`);
    this.route
      .paramMap
      .map(map => map.get('type'))
      .map(type => `_category_${type}`)
      .subscribe(this.fetchCategories.bind(this));
  }

  newCategorySave(crudItemSave: CrudItemSave<CategorySummary>) {
    this.httpClient
      .post(`/api/v1/category`, {
        type: this.type,
        name: crudItemSave.changed.name,
        parentUuid: crudItemSave.changed.parentUuid
      })
      .subscribe(
        () => {
          crudItemSave.commit({
            value: {},
            state: CrudItemState.EDIT
          });
          this.fetchCategories(this.type);
        },
        response => crudItemSave.rollback({
          message: response.error.errors.map(error => `${error.field} ${error.defaultMessage}`)
        })
      );
  }

  editCategorySave(crudItemSave: CrudItemSave<CategorySummary>) {
    this.httpClient
      .put(
        `/api/v1/category/${crudItemSave.item.uuid}`, {
          type: this.type,
          ...DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
            name: 'name',
            parentUuid: 'parentUuid'
          })
        }
      )
      .subscribe(
        () => {
          crudItemSave.commit();
          this.fetchCategories(this.type);
        },
        response => crudItemSave.rollback({
          message: response.error.message
        })
      );
  }

  private fetchCategories(type) {
    this.httpClient
      .get<CategoryTreeDtoData>(`/api/v1/category/tree/${type}`)
      .map(tree => new CategoryTreeDto(tree))
      .subscribe(tree => {
        const categories = [];
        tree.accept(element => categories.push(element));
        this.categories = categories;
      });
  }

}
