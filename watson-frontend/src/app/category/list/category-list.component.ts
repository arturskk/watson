import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {CategorySummary} from '../category-summary';
import {ModifyEvent} from '../../product/modify-event';
import {DiffsUtil} from '../../util/diffs-util';

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
      <div>
        <div *ngFor="let category of categories">
          <ws-category-list-item
            (onSave)="onItemChange($event)"
            [categories]="categories"
            [category]="category">
          </ws-category-list-item>
        </div>
      </div>
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

  onItemChange(change: ModifyEvent<CategorySummary>) {
    const diff = DiffsUtil.diff(change.newValue, change.oldValue, {
      name: 'name',
      parentUuid: 'parentUuid'
    });
    this.httpClient
      .put(`/api/v1/category/${change.oldValue.uuid}`, {
        type: this.type,
        ...diff
      })
      .subscribe(() => {
        window.location.reload();
      });
  }

}
