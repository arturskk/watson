import {Component, ContentChild, EventEmitter, Input, Output, TemplateRef} from '@angular/core';
import {CrudItemCanceled} from './crud-item-canceled';
import {CrudItemSave} from './crud-item-save';

@Component({
  selector: 'ws-crud-list-component',
  template: `
    <div *ngFor="let item of data" class="list-item">
      <ws-crud-item-component
        [item]="item"
        (itemCanceled)="itemCanceled.next($event)"
        (itemSave)="itemSave.next($event)"
        [itemSummaryTemplate]="itemSummaryTemplate || itemSummaryChildTemplate"
        [itemEditTemplate]="itemEditTemplate || itemEditChildTemplate">
      </ws-crud-item-component>
    </div>
  `,
  styleUrls: [
    'crud-list.component.scss'
  ]
})
export class CrudListComponent<T> {

  @Input() data: T[];
  @Input() itemSummaryTemplate: TemplateRef<any>;
  @Input() itemEditTemplate: TemplateRef<any>;
  @Output() itemCanceled: EventEmitter<CrudItemCanceled<T>> = new EventEmitter<CrudItemCanceled<T>>();
  @Output() itemSave: EventEmitter<CrudItemSave<T>> = new EventEmitter<CrudItemSave<T>>();
  @ContentChild('itemSummary') itemSummaryChildTemplate: TemplateRef<any>;
  @ContentChild('itemEdit') itemEditChildTemplate: TemplateRef<any>;

}
