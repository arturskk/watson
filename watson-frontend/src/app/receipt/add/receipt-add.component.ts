import {HttpClient} from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {Observable} from 'rxjs/Observable';
import {ArraysUtil} from '../../util/arrays-util';
import {ReceiptItem} from '../receipt-item';

@Component({
  selector: 'ws-receipt-add',
  template: `
    <ng-container *ngIf="receipt; else spinner">
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
        <div class="receipt-summary">
          <div class="receipt-summary-cell">Kwota: {{receiptSummaryCost()}}zł</div>
          <div class="receipt-summary-cell">Pozycje: {{nonEmptyReceiptItemCount()}}</div>
          <div class="receipt-header"><h2>Produkty</h2></div>
        </div>
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
                <option>op</option>
                <option>szt</option>
                <option>kg</option>
                <option>l</option>
              </select>
            </div>
            <div><input placeholder="Kwota" [(ngModel)]="item.cost"/></div>
            <div><a (click)="removeItem(item)">(-)</a></div>
          </div>
          <a (click)="addItem()">(+) dodaj produkt</a>
        </div>
      </ws-panel>
      <button (click)="save()">zapisz</button>
    </ng-container>
    <ng-template #spinner>
      Ładowanie...
    </ng-template>
  `
})
export class ReceiptAddComponent implements OnInit {

  receipt: Receipt;
  categoriesReceipt;
  categoriesItem;
  shops;
  products;
  accounts;
  filterByName = (item, searchText) => item.name.toLocaleLowerCase().indexOf(searchText.toLocaleLowerCase()) >= 0;

  constructor(private httpClient: HttpClient) {
  }

  static itemsBatch(): Partial<ReceiptItem>[] {
    const itemPrototype: Partial<ReceiptItem> = {
      amount: {
        unit: 'op'
      }
    };
    return ArraysUtil.fillWithCopies(5, itemPrototype);
  }

  static newReceipt(category): Receipt {
    return {
      date: new Date().toISOString().substr(0, 10),
      items: ReceiptAddComponent.itemsBatch(),
      category: category
    };
  }

  ngOnInit(): void {
    this.fetchDependencies();
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
    this.receipt.items = this.getNonEmptyReceiptItems();
    this.httpClient
      .post('/api/v1/receipt', this.receipt)
      .subscribe(
        () => {
          this.receipt = undefined;
          alert('Dodane');
          this.fetchDependencies();
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
      product.category = this.categoriesItem.find(category => category.uuid === 'root');
      console.log('Adding newly created product as available product [product=%o]', product);
      this.products.push({
        name: product.name,
        createdWithinReceipt: true
      });
    }
  }

  nonEmptyReceiptItemCount(): number {
    return this.getNonEmptyReceiptItems().length;
  }

  receiptSummaryCost(): number {
    return this.getNonEmptyReceiptItems().map(item => item.cost).reduce((prev, current) => ((parseFloat(current) || 0) + (parseFloat(prev) || 0)), 0);
  }

  private fetchDependencies() {
    Observable.forkJoin(
      this.httpClient.get('/api/v1/account'),
      this.httpClient.get('/api/v1/product'),
      this.httpClient.get('/api/v1/shop'),
      this.httpClient.get('/api/v1/category/_category_receipt'),
      this.httpClient.get('/api/v1/category/_category_receipt_item')
    ).subscribe(
      result => {
        this.accounts = result[0];
        this.products = result[1];
        this.shops = result[2];
        this.categoriesReceipt = this.categoriesReceipt = (result[3] as any[]).map(category => ({
          name: category.name,
          uuid: category.uuid
        }));
        this.categoriesItem = this.categoriesReceipt = (result[4] as any[]).map(category => ({
          name: category.name,
          uuid: category.uuid
        }));
        this.receipt = ReceiptAddComponent.newReceipt(this.categoriesReceipt.find(category => category.uuid === 'root'));
      }
    );
  }

  private getNonEmptyReceiptItems() {
    return this.receipt.items.filter(item => !!item['product']);
  }

}
