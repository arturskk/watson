import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {CategorySummary} from '../category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemSave} from '../../widgets/crud-list/crud-item-save';

@Component({
  selector: 'ws-category-list',
  template: `
    <h1>Kategorie</h1>
    <ws-panel>
      <h2>Dodaj kategorię</h2>
      <div>
        <ws-category-edit
          [cancelable]="false"
          [resettable]="true"
          (onSave)="onAddItem($event)"
          [categories]="categories">
        </ws-category-edit>
      </div>
    </ws-panel>
    <ws-panel>
      <h2>Lista kategorii</h2>
      <ws-crud-list-component [data]="categories" (itemSave)="categorySave($event)">
        <ng-template let-category #itemSummary>
          {{category.name}} ({{category.path}})
        </ng-template>
        <ng-template let-category #itemEdit>
          <input [(ngModel)]="category.name" placeholder="Nazwa"/>
          <!--<ws-select-->
            <!--*ngIf="category.uuid !== 'root'"-->
            <!--[(ngModel)]="parentCategory"-->
            <!--[data]="filteredCategories || categories"-->
            <!--[displayField]="'name'"-->
            <!--[allowNewValues]="false"-->
            <!--[placeholder]="'Kategoria nadrzędna'">-->
            <!--<ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>-->
              <!--<span [innerHTML]="markSearchText.call(undefined, item.name)"></span>-->
            <!--</ng-template>-->
          <!--</ws-select>-->
        </ng-template>
      </ws-crud-list-component>
    </ws-panel>
  `
})
export class CategoryListComponent implements OnInit {

  categories: CategorySummary[];
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
      .mergeMap(type => this.httpClient.get<CategorySummary[]>(`/api/v1/category/_category_${type}`))
      .subscribe(data => this.categories = data);
  }

  onAddItem(category: CategorySummary) {
    this.httpClient
      .post(`/api/v1/category`, {
        type: this.type,
        name: category.name,
        parentUuid: category.parentUuid
      })
      .subscribe(() => {
        window.location.reload();
      });
  }

  categorySave(crudItemSave: CrudItemSave<CategorySummary>) {
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
      .subscribe(crudItemSave.commit);
  }

}
