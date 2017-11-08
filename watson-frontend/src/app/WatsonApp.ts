import {AfterViewInit, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'watson-app',
  template: `
    <nav>
      <div>Dashboard</div>
      <div>Budżet</div>
      <div>Zakupy</div>
      <div>Dieta</div>
    </nav>
    <main>
      <h1>Rachunek</h1>
      <div class="receipt-main">
        <div><input placeholder="Opis" [(ngModel)]="receipt.description" /></div>
        <div><input placeholder="Data" [(ngModel)]="receipt.date"/></div>
        <div>
          <select-component
            [data]="accounts"
            [displayField]="'name'"
            [filter]="filterByName"
            [placeholder]="'Konto'"
            [(ngModel)]="receipt.account">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span *ngIf="newItem">Dodaj: </span>
              <span [innerHTML]="markSearchText(item.name)"></span>
            </ng-template>
          </select-component>
        </div>
        <div>
          <select-component
            [data]="shops"
            [displayField]="'name'"
            [filter]="filterByName"
            [placeholder]="'Sklep'"
            [(ngModel)]="receipt.shop">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span *ngIf="newItem">Dodaj: </span>
              <span [innerHTML]="markSearchText(item.name)"></span>
            </ng-template>
          </select-component>
        </div>
        <div>
          <select-component
            [data]="categoriesReceipt"
            [displayField]="'name'"
            [filter]="filterByName"
            [placeholder]="'Kategoria'"
            [(ngModel)]="receipt.category">
            <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
              <span *ngIf="newItem">Dodaj: </span>
              <span [innerHTML]="markSearchText(item.name)"></span>
            </ng-template>
          </select-component>
        </div>
        <div><input placeholder="Tagi" [(ngModel)]="receipt.tags" /></div>
      </div>
      <h2>Produkty</h2>
      <div class="receipt-items">
        <div class="receipt-item" *ngFor="let item of receipt.items">
          <div><input placeholder="Opis" [(ngModel)]="item.description" /></div>
          <div>
            <select-component
              [data]="categoriesReceiptItems"
              [displayField]="'name'"
              [filter]="filterByName"
              [placeholder]="'Kategoria'"
              [(ngModel)]="item.category">
              <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
                <span *ngIf="newItem">Dodaj: </span>
                <span [innerHTML]="markSearchText(item.name)"></span>
              </ng-template>
            </select-component>
          </div>
          <div>
            <select-component
              [data]="products"
              [displayField]="'name'"
              [filter]="filterByName"
              [placeholder]="'Produkt'"
              [(ngModel)]="item.product">
              <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
                <span *ngIf="newItem">Dodaj: </span>
                <span [innerHTML]="markSearchText(item.name)"></span>
              </ng-template>
            </select-component>
          </div>
          <div><input placeholder="Tagi" [(ngModel)]="item.tags" /></div>
          <div><input placeholder="Ilość" [(ngModel)]="item.amount.count" /></div>
          <div>
            <select [(ngModel)]="item.amount.unit">
              <option>kg</option>
              <option>l</option>
              <option>szt</option>
            </select>
          </div>
          <div><input placeholder="Kwota" [(ngModel)]="item.cost" /></div>
          <div><a (click)="removeItem(item)">(-)</a></div>
        </div>
        <a (click)="addItem()">(+) dodaj produkt</a>
      </div>
      <button (click)="save()">zapisz</button>
    </main>
    <footer>
      Watson footer
    </footer>
  `,
  styles: [
      `
      :host {
        display: block;
      }

      nav {
        display: flex;
      }

      aside {
        display: none;
      }

      main {
        border: 1px solid gray;
      }

      footer {
        font-size: 0.8em;
        font-style: italic;
      }

      .receipt-main, .receipt-items {
        margin-bottom: 15px;
      }

      .receipt-item {
        display: flex;
        flex-direction: row;
      }

    `
  ]
})
export class WatsonApp implements OnInit, AfterViewInit {

  receipt = {
    items: this.itemsBatch()
  };

  categoriesReceipt;
  categoriesReceiptItems;
  shops;
  products;
  accounts;
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
      .get('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categoriesReceiptItems = data);
    this.httpClient
      .get('/api/v1/category/_category_receipt')
      .subscribe(data => this.categoriesReceipt = data);
  }

  ngAfterViewInit(): void {
  }

  addItem() {
    this.receipt.items.push(
      ...this.itemsBatch()
    );
  }

  removeItem(item) {
    this.receipt.items.splice(this.receipt.items.indexOf(item), 1);
  }

  save() {
    const receipt = {
      ...this.receipt
    };
    receipt.items = receipt.items.filter(
      item => item['category'] || item['product']
    );
    console.log(receipt);
  }

  itemsBatch() {
    return [{amount: {}}, {amount: {}}, {amount: {}}, {amount: {}}, {amount: {}}];
  }

}
