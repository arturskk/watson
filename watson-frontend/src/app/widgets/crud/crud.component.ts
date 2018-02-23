import {HttpClient} from '@angular/common/http';
import {Component, ContentChild, Input, OnInit, TemplateRef} from '@angular/core';
import {CrudItemState} from '../../widgets/crud-list/crut-item-state';
import {CrudHelper, CrudResource} from '../../util/crud-helper';
import {CrudConfig} from './crud-config';

@Component({
  selector: 'ws-crud',
  template: `
    <ng-container *ngIf="items">
      <ws-panel>
        <h2>{{config.keys.itemAddKey}}</h2>
        <div>
          <ws-crud-item-component [state]="State.EDIT"
                                  [cancelable]="false"
                                  [itemEditTemplate]="itemEditTemplate"
                                  (itemSave)="resource.added($event)">
          </ws-crud-item-component>
        </div>
      </ws-panel>
      <ws-panel>
        <h2>{{config.keys.itemListKey}}</h2>
        <ws-crud-list-component [data]="items"
                                [itemEditTemplate]="itemEditTemplate"
                                [itemSummaryTemplate]="itemSummaryTemplate"
                                (itemSave)="resource.edited($event)">
        </ws-crud-list-component>
      </ws-panel>
    </ng-container>
  `,
  styleUrls: [
    'crud.component.scss'
  ],
  providers: [
    CrudHelper
  ]
})
export class CrudComponent<T> implements OnInit {

  @Input() config: CrudConfig<T>;
  @ContentChild('itemEdit') itemEditTemplate: TemplateRef<any>;
  @ContentChild('itemSummary') itemSummaryTemplate: TemplateRef<any>;
  items: T[];
  State = CrudItemState;
  resource: CrudResource<T>;

  constructor(private httpClient: HttpClient, private crudHelper: CrudHelper<T>) {
  }

  ngOnInit(): void {
    this.resource = this.crudHelper.asResource({
      api: this.config.api.endpoint,
      mapper: this.config.model.mapper,
      onSuccess: this.fetchItems.bind(this)
    });
    this.fetchItems();
  }

  private fetchItems() {
    return this.httpClient
      .get<T[]>(this.config.api.endpoint)
      .subscribe(items => this.items = items);
  }

}
