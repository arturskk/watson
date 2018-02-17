import {Component, OnInit} from '@angular/core';
import {ProductSummary} from '../../product/product-summary';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {AccountSummary} from '../account-summary';
import {CrudHelper, CrudResource} from '../../util/crud-helper';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'ws-account-list',
  template: `
    <h1>Konta</h1>
    <ng-container *ngIf="accounts">
      <ws-panel>
        <h2>Dodaj konto</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="resource.added($event)">
            <ng-template let-account #itemEdit>
              <input [(ngModel)]="account.name"/>
            </ng-template>
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista kont</h2>
        <ws-crud-list-component [data]="accounts" (itemSave)="resource.edited($event)">
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
  ],
  providers: [
    CrudHelper
  ]
})
export class AccountListComponent implements OnInit {

  accounts: AccountSummary[];
  State = CrudItemState;
  resource: CrudResource<AccountSummary>;

  constructor(private httpClient: HttpClient, crudHelper: CrudHelper<AccountSummary>) {
    this.resource = crudHelper.asResource({
      api: '/api/v1/account',
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name
      }),
      onSuccess: this.fetchAccounts.bind(this)
    });
  }

  ngOnInit(): void {
    this.fetchAccounts();
  }

  private fetchAccounts() {
    return this.httpClient
      .get<ProductSummary[]>('/api/v1/account')
      .subscribe(data => this.accounts = data);
  }

}
