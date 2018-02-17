import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {DiffsUtil} from '../../util/diffs-util';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {RetailChainSummary} from '../retail-chain-summary';
import {CrudHelper, CrudResource} from '../../util/crud-helper';

@Component({
  selector: 'ws-retail-chain-list',
  template: `
    <h1>Sieci handlowe</h1>
    <ng-container *ngIf="retailChains">
      <ws-panel>
        <h2>Dodaj sieć handlową</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT" [cancelable]="false" (itemSave)="resource.added($event)">
            <ng-template let-retailChain #itemEdit>
              <input [(ngModel)]="retailChain.name"/>
            </ng-template>
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>Lista sieci handlowych</h2>
        <ws-crud-list-component [data]="retailChains" (itemSave)="resource.edited($event)">
          <ng-template let-retailChain #itemSummary>
            {{retailChain.name}}
          </ng-template>
          <ng-template let-retailChain #itemEdit>
            <input [(ngModel)]="retailChain.name"/>
          </ng-template>
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `,
  styleUrls: [
    'retail-chain-list.component.scss'
  ],
  providers: [
    CrudHelper
  ]
})
export class RetailChainListComponent implements OnInit {

  retailChains: RetailChainSummary[];
  State = CrudItemState;
  resource: CrudResource<RetailChainSummary>;

  constructor(private httpClient: HttpClient, crudHelper: CrudHelper<RetailChainSummary>) {
    this.resource = crudHelper.asResource({
      api: '/api/v1/retailchain',
      mapper: crudItemSave => DiffsUtil.diff(crudItemSave.changed, crudItemSave.item, {
        name
      }),
      onSuccess: this.fetchRetailChains.bind(this)
    });
  }

  ngOnInit(): void {
    this.fetchRetailChains();
  }

  private fetchRetailChains() {
    return this.httpClient
      .get<RetailChainSummary[]>('/api/v1/retailchain')
      .subscribe(data => this.retailChains = data);
  }

}
