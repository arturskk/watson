import {Component, EventEmitter, Input, Output, TemplateRef} from '@angular/core';
import {ObjectsUtil} from '../../util/objects-util';
import {CrudItemCanceled} from './crud-item-canceled';
import {CrudItemSave} from './crud-item-save';

enum State {
  SUMMARY, EDIT, SAVING
}

@Component({
  selector: 'ws-crud-item-component',
  template: `
    <div class="item">
      <div class="item-summary">
        <ng-template
          *ngIf="state === State.SUMMARY"
          [ngTemplateOutlet]="itemSummaryTemplate"
          [ngTemplateOutletContext]="{$implicit: summaryValue}">
        </ng-template>
        <ng-template
          *ngIf="state === State.EDIT"
          [ngTemplateOutlet]="itemEditTemplate"
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
          <ws-button-flat (click)="onCancelClicked()">anuluj</ws-button-flat>
        </ng-container>
      </div>
    </div>
    <div class="action-message" *ngIf="actionMessage">
      {{actionMessage}}
    </div>
  `
})
export class CrudItemComponent<T> {

  summaryValue: T;
  editValue: T;
  actionMessage: string = null;
  @Input() itemSummaryTemplate: TemplateRef<any>;
  @Input() itemEditTemplate: TemplateRef<any>;
  @Output() itemCanceled: EventEmitter<CrudItemCanceled<T>> = new EventEmitter<CrudItemCanceled<T>>();
  @Output() itemSave: EventEmitter<CrudItemSave<T>> = new EventEmitter<CrudItemSave<T>>();
  state: State = State.SUMMARY;
  // noinspection JSUnusedGlobalSymbols - used in template
  State = State;

  @Input() set item(item: any) {
    this.summaryValue = item;
    this.resetEditValue(this.summaryValue);
  }

  onEditClicked() {
    this.setState(State.EDIT);
    this.resetEditValue(this.summaryValue);
  }

  onCancelClicked() {
    this.setState(State.SUMMARY);
    this.itemCanceled.next({
      item: this.summaryValue,
      changed: this.editValue
    });
  }

  onSaveClicked() {
    this.setState(State.SAVING);
    this.itemSave.next({
      item: this.summaryValue,
      changed: this.editValue,
      commit: () => {
        this.item = this.editValue;
        this.setState(State.SUMMARY);
      },
      rollback: (message: string) => {
        this.setState(State.EDIT);
        this.actionMessage = message;
      }
    });
  }

  private setState(state: State, config: {clearActionMessages?: boolean} = {}) {
    this.state = state;
    if (config.clearActionMessages === undefined || config.clearActionMessages) {
      this.actionMessage = null;
    }
  }

  private resetEditValue(item: any) {
    this.editValue = ObjectsUtil.deepCopy(item);
  }

}
