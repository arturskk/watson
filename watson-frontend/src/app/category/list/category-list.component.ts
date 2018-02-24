import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {CategorySummary} from '../category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {CrudHelper, CrudResource} from '../../util/crud-helper';

@Component({
  selector: 'ws-category-list',
  template: `
    <h1>Kategorie</h1>
    <ng-container *ngIf="categories">
      <ws-panel>
        <h2>Dodaj kategorię</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="resource.added($event)">
            <ng-template let-category #itemEdit>
              <ws-category-edit [category]="category" [categories]="categories"></ws-category-edit>
            </ng-template>
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista kategorii</h2>
        <ws-crud-list-component [data]="categories" (itemSave)="resource.edited($event)">
          <ng-template let-category #itemSummary>
            {{category.name}}
            <span class="category-path" *ngIf="category.path.length > 0">&nbsp;({{category.path | joinArray:' > '}})</span>
          </ng-template>
          <ng-template let-category #itemEdit>
            <ws-category-edit [category]="category" [categories]="categories"></ws-category-edit>
          </ng-template>
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `,
  styleUrls: [
    'category-list.component.scss'
  ],
  providers: [
    CrudHelper
  ]
})
export class CategoryListComponent implements OnInit {

  categories: CategorySummary[];
  State = CrudItemState;
  resource: CrudResource<CategorySummary>;
  private type: string;

  constructor(private httpClient: HttpClient, private route: ActivatedRoute, crudHelper: CrudHelper<CategorySummary>) {
    this.resource = crudHelper.asResource({
      api: '/api/v1/category',
      mapper: crudItemSave => ({
        type: this.type,
        ...DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
          name: 'name',
          parentUuid: 'parentUuid'
        })
      }),
      onSuccess: () => this.fetchCategories(this.type)
    });
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

  private fetchCategories(type) {
    this.httpClient
      .get<CategorySummary[]>(`/api/v1/category/${type}`)
      .subscribe(categories => this.categories = categories);
  }

}
