import {Component} from '@angular/core';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudConfig} from '../../widgets/crud/crud-config';
import {ProductSummary} from '../../product/product-summary';

@Component({
  selector: 'ws-account-list',
  template: `
    <h1>Konta</h1>
    <ws-crud [config]="crudConfig">
      <ng-template let-account #itemSummary>
        {{account.name}}
        <ng-container *ngIf="account.useDefault">(domyślne)</ng-container>
      </ng-template>
      <ng-template let-account #itemEdit>
        <input [(ngModel)]="account.name"/>
        <div class="use-as-default-checkbox">
          <span>domyślne</span>
          <input type="checkbox" [(ngModel)]="account.useDefault"/>
        </div>
      </ng-template>
    </ws-crud>
  `,
  styleUrls: [
    'account-list.component.scss'
  ]
})
export class AccountListComponent {

  readonly crudConfig: CrudConfig<ProductSummary> = {
    keys: {
      itemAddKey: 'Dodaj konto',
      itemListKey: 'Lista kont'
    },
    api: {
      endpoint: '/api/v1/account'
    },
    model: {
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name: '',
        useDefault: ''
      })
    }
  };

}
