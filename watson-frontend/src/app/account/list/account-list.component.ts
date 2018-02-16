import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../../product/product-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemSave} from '../../widgets/crud-list/crud-item-save';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {AccountSummary} from '../account-summary';

@Component({
  selector: 'ws-account-list',
  template: `
    <h1>Konta</h1>
    <ng-container *ngIf="accounts">
      <ws-panel>
        <h2>Dodaj konto</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="newAccountSaved($event)">
            <ng-template let-account #itemEdit>
              <input [(ngModel)]="account.name"/>
            </ng-template>
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista kont</h2>
        <ws-crud-list-component [data]="accounts" (itemSave)="editAccountSaved($event)">
          <ng-template let-account #itemSummary>
            {{account.name}}
          </ng-template>
          <ng-template let-account #itemEdit>
            <input [(ngModel)]="account.name"/>
          </ng-template>
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `,
  styleUrls: [
    'account-list.component.scss'
  ]
})
export class AccountListComponent implements OnInit {

  accounts: AccountSummary[];
  State = CrudItemState;

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.fetchShops();
  }

  newAccountSaved(crudItemSave: CrudItemSave<AccountSummary>) {
    this.httpClient
      .post(`/api/v1/account`, {
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

  editAccountSaved(crudItemSave: CrudItemSave<ProductSummary>) {
    this.httpClient
      .put(
        `/api/v1/account/${crudItemSave.item.uuid}`,
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
      .get<ProductSummary[]>('/api/v1/account')
      .subscribe(data => this.accounts = data);
  }

}
