import {Component} from '@angular/core';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudConfig} from '../../widgets/crud/crud-config';
import {ProductSummary} from '../../product/product-summary';

@Component({
  selector: 'ws-retail-chain-list',
  template: `
    <h1>Sieci handlowe</h1>
    <ws-crud [config]="crudConfig">
      <ng-template let-retailChain #itemSummary>
        {{retailChain.name}}
      </ng-template>
      <ng-template let-retailChain #itemEdit>
        <input [(ngModel)]="retailChain.name"/>
      </ng-template>
    </ws-crud>
  `,
  styleUrls: [
    'retail-chain-list.component.scss'
  ]
})
export class RetailChainListComponent {

  readonly crudConfig: CrudConfig<ProductSummary> = {
    keys: {
      itemAddKey: 'Dodaj producenta',
      itemListKey: 'Lista producentów'
    },
    api: {
      endpoint: '/api/v1/retailchain'
    },
    model: {
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name
      })
    }
  };

}
