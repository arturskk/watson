import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {ReceiptItem} from '../receipt-item';
import {ArraysUtil} from '../../util/arrays-util';

@Component({
  selector: 'ws-receipt-add',
  template: `
    <h1>Dodaj rachunek</h1>
    <ws-panel>
      <div class="receipt-main">
        <div><input placeholder="Opis" [(ngModel)]="receipt.description"/></div>
        <div><input placeholder="Data" [(ngModel)]="receipt.date"/></div>
        <div>
          <ws-select
            [data]="accounts"
            [displayField]="'name'"
            [filter]="filterByName"
            [placeholder]="'Konto'"
            [(ngModel)]="receipt.account">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span *ngIf="newItem">Dodaj: </span>
              <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
            </ng-template>
          </ws-select>
        </div>
        <div>
          <ws-select
            [data]="shops"
            [displayField]="'name'"
            [filter]="filterByName"
            [placeholder]="'Sklep'"
            [(ngModel)]="receipt.shop">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span *ngIf="newItem">Dodaj: </span>
              <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
            </ng-template>
          </ws-select>
        </div>
        <div>
          <ws-select
            [data]="categoriesReceipt"
            [displayField]="'name'"
            [filter]="filterByName"
            [placeholder]="'Kategoria'"
            [(ngModel)]="receipt.category">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span *ngIf="newItem">Dodaj: </span>
              <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
            </ng-template>
          </ws-select>
        </div>
        <!--<div><input placeholder="Tagi" [(ngModel)]="receipt.tags"/></div>-->
      </div>
    </ws-panel>
    <ws-panel>
      <h2>Produkty</h2>
      <div class="receipt-items">
        <div class="receipt-item" *ngFor="let item of receipt.items">
          <div class="select-product-cell">
            <ws-select
              [data]="products"
              [displayField]="'name'"
              [filter]="filterByName"
              [placeholder]="'Produkt'"
              (onChange)="onProductSelected(item, $event)"
              [(ngModel)]="item.product">
              <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
                <div>
                  <span *ngIf="newItem">Dodaj: </span>
                  <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
                </div>
                <div *ngIf="item.createdWithinReceipt" class="dynamic-select-item">
                  Nowo utworzony produkt
                </div>
                <div *ngIf="item.categoryPath" class="select-item-description">
                  {{item.categoryPath}}
                </div>
              </ng-template>
            </ws-select>
            <ws-select
              *ngIf="item.product && item.product.newValue && !item.product.createdWithinReceipt"
              [data]="categoriesItem"
              [displayField]="'name'"
              [filter]="filterByName"
              (onChange)="onProductCategorySelected($event)"
              [placeholder]="'Kategoria nowego produktu'"
              [(ngModel)]="item.product.category">
              <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
                <div>
                  <span *ngIf="newItem">Dodaj: </span>
                  <span [innerHTML]="markSearchText.call(undefined, item.name)"></span>
                </div>
                <div *ngIf="item.createdWithinReceipt" class="dynamic-select-item">
                  Nowo utworzona kategoria produktu
                </div>
                <div *ngIf="item.categoryPath" class="select-item-description">
                  {{item.categoryPath}}
                </div>
              </ng-template>
            </ws-select>
          </div>
          <div><input placeholder="Ilość" [(ngModel)]="item.amount.count"/></div>
          <div>
            <select [(ngModel)]="item.amount.unit">
              <option>kg</option>
              <option>l</option>
              <option>szt</option>
            </select>
          </div>
          <div><input placeholder="Kwota" [(ngModel)]="item.cost"/></div>
          <div><a (click)="removeItem(item)">(-)</a></div>
        </div>
        <a (click)="addItem()">(+) dodaj produkt</a>
      </div>
    </ws-panel>
    <button (click)="save()">zapisz</button>
  `,
  styles: [
      `
      .receipt-main, .receipt-items {
        margin-bottom: 15px;
      }

      .receipt-item {
        display: flex;
        flex-direction: row;
      }

      .select-item-description, .dynamic-select-item {
        font-style: italic;
        font-size: 0.9em;
        color: darkgray;
      }

      .dynamic-select-item {
        color: darkred;
      }

      .select-product-cell {
        display: flex;
      }
    `
  ]
})
export class ReceiptAddComponent implements OnInit {

  receipt: Receipt = ReceiptAddComponent.newReceipt();
  categoriesReceipt;
  categoriesItem;
  shops;
  products;
  accounts;

  static itemsBatch(): Partial<ReceiptItem>[] {
    const itemPrototype: Partial<ReceiptItem> = {amount: {unit: 'kg'}};
    return ArraysUtil.fillWithCopies(5, itemPrototype);
  }

  static newReceipt(): Receipt {
    return {
      date: new Date().toISOString().substr(0, 10),
      items: ReceiptAddComponent.itemsBatch()
    };
  }

  filterByName = (item, searchText) => item.name.toLocaleLowerCase().indexOf(searchText.toLocaleLowerCase()) >= 0;

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get('/api/v1/account')
      .subscribe(data => this.accounts = data);
    this.httpClient
      .get('/api/v1/product')
      .subscribe(data => this.products = data);
    this.httpClient
      .get('/api/v1/shop')
      .subscribe(data => this.shops = data);
    this.httpClient
      .get('/api/v1/category/_category_receipt')
      .subscribe((data: any[]) => this.categoriesReceipt = data.map(category => ({
        name: category.name,
        uuid: category.uuid
      })));
    this.httpClient
      .get('/api/v1/category/_category_receipt_item')
      .subscribe((data: any[]) => this.categoriesItem = data.map(category => ({
        name: category.name,
        uuid: category.uuid
      })));
  }

  addItem() {
    this.receipt.items.push(
      ...ReceiptAddComponent.itemsBatch()
    );
  }

  removeItem(item) {
    this.receipt.items.splice(this.receipt.items.indexOf(item), 1);
  }

  save() {
    this.receipt.items = this.receipt.items.filter(
      item => !!item['product']
    );
    this.httpClient
      .post('/api/v1/receipt', this.receipt)
      .subscribe(
        () => {
          this.receipt = ReceiptAddComponent.newReceipt();
          alert('Dodane');
        },
        (err) => {
          console.log(err.error);
          alert(err.error.message);
        }
      );
  }

  onProductCategorySelected(category) {
    if (category.newValue && !category.createdWithinReceipt) {
      console.log('Adding newly created product category as available category [category=%o]', category);
      this.categoriesItem.push({
        name: category.name,
        createdWithinReceipt: true
      });
    }
  }

  onProductSelected(item, product) {
    if (product.newValue && !product.createdWithinReceipt) {
      console.log('Adding newly created product as available product [product=%o]', product);
      this.products.push({
        name: product.name,
        createdWithinReceipt: true
      });
    }
  }

}
