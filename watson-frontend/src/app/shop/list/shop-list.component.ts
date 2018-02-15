import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../../product/product-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemSave} from '../../widgets/crud-list/crud-item-save';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {ShopSummary} from '../shop-summary';

@Component({
  selector: 'ws-shop-list',
  template: `
    <h1>Sklepy</h1>
    <ng-container *ngIf="shops">
      <ws-panel>
        <h2>Dodaj sklep</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="newShopSaved($event)">
            <ng-template let-shop #itemEdit>
              <input [(ngModel)]="shop.name"/>
            </ng-template>
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista sklepów</h2>
        <ws-crud-list-component [data]="shops" (itemSave)="editShopSaved($event)">
          <ng-template let-shop #itemSummary>
            {{shop.name}}
          </ng-template>
          <ng-template let-shop #itemEdit>
            <input [(ngModel)]="shop.name"/>
          </ng-template>
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `,
  styleUrls: [
    'shop-list.component.scss'
  ]
})
export class ShopListComponent implements OnInit {

  shops: ShopSummary[];
  State = CrudItemState;

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.fetchShops();
  }

  newShopSaved(crudItemSave: CrudItemSave<ShopSummary>) {
    this.httpClient
      .post(`/api/v1/shop`, {
        name: crudItemSave.changed.name
      })
      .subscribe(
        () => {
          crudItemSave.commit({
            value: {},
            state: CrudItemState.EDIT
          });
          this.fetchShops();
        },
        response => crudItemSave.rollback({
          message: response.error.errors.map(error => `${error.field} ${error.defaultMessage}`)
        })
      );
  }

  editShopSaved(crudItemSave: CrudItemSave<ProductSummary>) {
    this.httpClient
      .put(
        `/api/v1/shop/${crudItemSave.item.uuid}`,
        DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
          name: 'name'
        })
      )
      .subscribe(
        () => {
          crudItemSave.commit();
          this.fetchShops();
        },
        response => crudItemSave.rollback({
          message: response.error.message
        })
      );
  }

  private fetchShops() {
    return this.httpClient
      .get<ProductSummary[]>('/api/v1/shop')
      .subscribe(data => this.shops = data);
  }

}
