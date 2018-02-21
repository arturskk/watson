import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'ws-add-receipt-details',
  template: `
    <div class="item">
      <div class="label">
        Opis
      </div>
      <div class="input-container">
        <input [value]="description || ''" (change)="descriptionChange.next($event.target.value)"/>
      </div>
    </div>
    <div class="item">
      <div class="label">
        Data
      </div>
      <div class="input-container">
        <input [value]="date || ''" (change)="dateChange.next($event.target.value)"/>
      </div>
    </div>
    <div class="item">
      <div class="label">
        Konto
      </div>
      <div>
        <ws-select [data]="accounts"
                   [value]="account"
                   [displayField]="'name'"
                   (onChange)="accountChange.next($event)">
          <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
            <span *ngIf="newItem">Dodaj: </span>
            <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
          </ng-template>
        </ws-select>
      </div>
    </div>
    <div class="item">
      <div class="label">
        Sklep
      </div>
      <div>
        <ws-select [data]="shops"
                   [displayField]="'name'"
                   (onChange)="shopChange.next($event)">
          <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
            <div>
            <span *ngIf="newItem">Dodaj: </span>
            <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
            </div>
            <div *ngIf="item.retailChain" class="select-item-description">
              <span>{{item.retailChain.name}}</span>
            </div>
          </ng-template>
        </ws-select>
      </div>
    </div>
    <div class="item">
      <div class="label">
        Kategoria
      </div>
      <div>
        <ws-select [data]="categories"
                   [displayField]="'name'"
                   (onChange)="categoryChange.next($event)">
          <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
            <span *ngIf="newItem">Dodaj: </span>
            <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
          </ng-template>
        </ws-select>
      </div>
    </div>
  `,
  styleUrls: [
    'add-receipt-details.component.scss'
  ]
})
export class AddReceiptDetailsComponent {

  @Input() accounts;
  @Input() shops;
  @Input() categories;
  @Input() receipt;
  @Input() description;
  @Input() date;
  @Input() account;
  @Input() shop;
  @Input() category;
  @Output() descriptionChange = new EventEmitter();
  @Output() dateChange = new EventEmitter();
  @Output() accountChange = new EventEmitter();
  @Output() shopChange = new EventEmitter();
  @Output() categoryChange = new EventEmitter();

}
