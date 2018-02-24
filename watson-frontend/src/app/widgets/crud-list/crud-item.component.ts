import {Component, ContentChild, EventEmitter, Input, Output, TemplateRef} from '@angular/core';
import {ObjectsUtil} from '../../util/objects-util';
import {CrudItemCanceled} from './crud-item-canceled';
import {CrudItemSave, CrudItemSaveCommitConfig, CrudItemSaveRollbackConfig} from './crud-item-save';
import {CrudItemState} from './crut-item-state';

@Component({
  selector: 'ws-crud-item-component',
  template: `
    <div class="item" [attr.uuid]="summaryValue['uuid'] || 'n/a'">
      <div class="item-renderer">
        <ng-template
          *ngIf="state === State.SUMMARY"
          [ngTemplateOutlet]="itemSummaryTemplate || itemSummaryChildTemplate"
          [ngTemplateOutletContext]="{$implicit: summaryValue}">
        </ng-template>
        <ng-template
          *ngIf="state === State.EDIT"
          [ngTemplateOutlet]="itemEditTemplate || itemEditChildTemplate"
          [ngTemplateOutletContext]="{$implicit: editValue}">
        </ng-template>
        <ng-container *ngIf="state === State.SAVING">
          Zapisywanie...
        </ng-container>
      </div>
      <div class="item-actions">
        <ng-container *ngIf="state === State.SUMMARY">
          <!--<ws-button-flat>usuń</ws-button-flat>-->
          <ws-button-flat (click)="onEditClicked()">edytuj</ws-button-flat>
        </ng-container>
        <ng-container *ngIf="state === State.EDIT">
          <ws-button-flat (click)="onSaveClicked()">zapisz</ws-button-flat>
          <ws-button-flat (click)="onCancelClicked()" *ngIf="cancelable">anuluj</ws-button-flat>
        </ng-container>
      </div>
    </div>
    <div class="action-message" *ngIf="actionMessage">
      {{actionMessage}}
    </div>
  `,
  styleUrls: [
    'crud-item.component.scss'
  ]
})
export class CrudItemComponent<T> {

  @Input() itemEditTemplate: TemplateRef<any>;
  @Input() state: CrudItemState = CrudItemState.SUMMARY;
  @Input() cancelable = true;
  @Input() itemSummaryTemplate: TemplateRef<any>;
  @Output() itemCanceled: EventEmitter<CrudItemCanceled<T>> = new EventEmitter<CrudItemCanceled<T>>();
  @Output() itemSave: EventEmitter<CrudItemSave<T>> = new EventEmitter<CrudItemSave<T>>();
  @ContentChild('itemEdit') itemEditChildTemplate: TemplateRef<any>;
  @ContentChild('itemSummary') itemSummaryChildTemplate: TemplateRef<any>;
  uuid: string;
  summaryValue: Partial<T> = {};
  editValue: Partial<T> = {};
  actionMessage: string = null;
  // noinspection JSUnusedGlobalSymbols - used in template
  State = CrudItemState;

  @Input() set item(item: any) {
    this.summaryValue = item;
    this.uuid = item['uuid'] || 'no-uuid';
    this.resetEditValue(this.summaryValue);
  }

  onEditClicked() {
    this.setState(CrudItemState.EDIT);
    this.resetEditValue(this.summaryValue);
  }

  onCancelClicked() {
    this.setState(CrudItemState.SUMMARY);
    this.itemCanceled.next({
      item: this.summaryValue,
      changed: this.editValue
    });
  }

  onSaveClicked() {
    this.setState(CrudItemState.SAVING);
    this.itemSave.next({
      item: this.summaryValue,
      changed: this.editValue,
      commit: (config: CrudItemSaveCommitConfig<T> = {}) => {
        ObjectsUtil.ifProvided(
          config.value,
          value => this.item = value,
          () => this.item = this.editValue
        );
        ObjectsUtil.ifProvided(
          config.state,
          this.setState.bind(this),
          () => this.state = CrudItemState.SUMMARY
        );
      },
      rollback: (config: CrudItemSaveRollbackConfig<T> = {}) => {
        ObjectsUtil.ifProvided(
          config.state,
          this.setState.bind(this),
          () => this.state = CrudItemState.EDIT
        );
        ObjectsUtil.ifProvided(
          config.message,
          message => this.actionMessage = message
        );
      }
    });
  }

  private setState(state: CrudItemState, config: { clearActionMessages?: boolean } = {}) {
    this.state = state;
    if (config.clearActionMessages === undefined || config.clearActionMessages) {
      this.actionMessage = null;
    }
  }

  private resetEditValue(item: any) {
    this.editValue = ObjectsUtil.deepCopy(item);
  }

}
