import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {ShopSummary} from '../shop-summary';
import {CrudHelper, CrudResource} from '../../util/crud-helper';
import {RetailChainSummary} from '../../retailchain/retail-chain-summary';

@Component({
  selector: 'ws-shop-list',
  template: `
    <h1>Sklepy</h1>
    <ng-container *ngIf="shops && retailChains">
      <ws-panel>
        <h2>Dodaj sklep</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="resource.added($event)">
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
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista sklepów</h2>
        <ws-crud-list-component [data]="shops" (itemSave)="resource.edited($event)">
          <ng-template let-shop #itemSummary>
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
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `,
  styleUrls: [
    'shop-list.component.scss'
  ],
  providers: [
    CrudHelper
  ]
})
export class ShopListComponent implements OnInit {

  shops: ShopSummary[];
  retailChains: RetailChainSummary[];
  State = CrudItemState;
  resource: CrudResource<ShopSummary>;

  constructor(private httpClient: HttpClient, crudHelper: CrudHelper<ShopSummary>) {
    this.resource = crudHelper.asResource({
      api: '/api/v1/shop',
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name: undefined,
        retailChainUuid: 'retailChain.uuid'
      }),
      onSuccess: this.fetchShops.bind(this)
    });
  }

  ngOnInit(): void {
    this.httpClient
      .get<RetailChainSummary[]>('/api/v1/retailchain')
      .subscribe(data => this.retailChains = data);
    this.fetchShops();
  }

  private fetchShops() {
    return this.httpClient
      .get<ShopSummary[]>('/api/v1/shop')
      .subscribe(data => this.shops = data);
  }

}
