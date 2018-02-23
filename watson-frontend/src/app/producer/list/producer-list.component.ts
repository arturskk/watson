import {Component} from '@angular/core';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudConfig} from '../../widgets/crud/crud-config';
import {ProductSummary} from '../../product/product-summary';

@Component({
  selector: 'ws-producer-list',
  template: `
    <h1>Producenci</h1>
    <ws-crud [config]="crudConfig">
      <ng-template let-item #itemSummary>
        {{item.name}}
      </ng-template>
      <ng-template let-item #itemEdit>
        <input [(ngModel)]="item.name"/>
      </ng-template>
    </ws-crud>
  `,
  styleUrls: [
    'producer-list.component.scss'
  ]
})
export class ProducerListComponent {

  readonly crudConfig: CrudConfig<ProductSummary> = {
    keys: {
      itemAddKey: 'Dodaj producenta',
      itemListKey: 'Lista producentów'
    },
    api: {
      endpoint: '/api/v1/producer'
    },
    model: {
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name
      })
    }
  };

}
