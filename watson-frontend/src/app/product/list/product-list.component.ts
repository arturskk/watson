import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProducerSummary} from '../../producer/producer-summary';
import {ProductSummary} from '../product-summary';
import {CategorySummary} from '../../category/category-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudConfig} from '../../widgets/crud/crud-config';

@Component({
  selector: 'ws-product-list',
  template: `
    <h1>Produkty</h1>
    <ws-crud [config]="crudConfig" *ngIf="categories; else spinner">
      <ng-template let-product #itemSummary>
        {{product.name}} <ng-container *ngIf="product.producer">({{product.producer.name}})</ng-container>
        <span class="category">&nbsp;({{product.category.path | joinArray:' > '}})</span>
      </ng-template>
      <ng-template let-product #itemEdit>
        <ws-product-edit [product]="product" [categories]="categories" [producers]="producers"></ws-product-edit>
      </ng-template>
    </ws-crud>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'product-list.component.scss'
  ]
})
export class ProductListComponent implements OnInit {

  readonly crudConfig: CrudConfig<ProductSummary> = {
    keys: {
      itemAddKey: 'Dodaj produkt',
      itemListKey: 'Lista produktów'
    },
    api: {
      endpoint: '/api/v1/product'
    },
    model: {
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name: 'name',
        categoryUuid: 'category.uuid',
        producerUuid: 'producer.uuid',
        defaultUnit: 'defaultUnit'
      })
    }
  };

  categories: CategorySummary[];
  producers: ProducerSummary[];

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<CategorySummary[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
    this.httpClient
      .get<CategorySummary[]>('/api/v1/producer')
      .subscribe(data => this.producers = data);
  }

}
