import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {RetailChainSummary} from '../../retailchain/retail-chain-summary';
import {CrudConfig} from '../../widgets/crud/crud-config';
import {DiffsUtil} from '../../util/diffs-util';
import {ProductSummary} from '../../product/product-summary';

@Component({
  selector: 'ws-shop-list',
  template: `
    <h1>Sklepy</h1>
    <ws-crud [config]="crudConfig" *ngIf="retailChains; else spinner">
      <ng-template let-shop #itemSummary>
        <ng-container *ngIf="shop.retailChain">{{shop.retailChain.name}} -</ng-container>
        {{shop.name}}
      </ng-template>
      <ng-template let-shop #itemEdit>
        <input [(ngModel)]="shop.name"/>
        <ws-select
          [(ngModel)]="shop.retailChain"
          [data]="retailChains"
          [displayField]="'name'"
          [allowNewValues]="false"
          [placeholder]="'Sieć'">
          <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
            <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
          </ng-template>
        </ws-select>
      </ng-template>
    </ws-crud>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'shop-list.component.scss'
  ]
})
export class ShopListComponent implements OnInit {

  readonly crudConfig: CrudConfig<ProductSummary> = {
    keys: {
      itemAddKey: 'Dodaj sklep',
      itemListKey: 'Lista sklepów'
    },
    api: {
      endpoint: '/api/v1/shop'
    },
    model: {
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name: undefined,
        retailChainUuid: 'retailChain.uuid'
      })
    }
  };

  retailChains: RetailChainSummary[];

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<RetailChainSummary[]>('/api/v1/retailchain')
      .subscribe(data => this.retailChains = data);
  }

}
