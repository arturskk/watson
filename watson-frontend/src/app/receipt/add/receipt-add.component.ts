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
        <ws-add-receipt-details [accounts]="accounts"
                                [shops]="shops"
                                [categories]="categoriesReceipt"
                                [receipt]="receipt"
                                [description]="receipt.description"
                                [date]="receipt.date"
                                [account]="receipt.account"
                                [shop]="receipt.shop"
                                [category]="receipt.category"
                                (descriptionChange)="receipt.description = $event"
                                (dateChange)="receipt.date = $event"
                                (accountChange)="receipt.account = $event"
                                (shopChange)="receipt.shop = $event"
                                (categoryChange)="receipt.category = $event">
        </ws-add-receipt-details>
      </ws-panel>
      <ws-panel>
        <div class="row header">
          <div *ngFor="let column of columns" [class]="'column column-' + column.span">
            {{column.textKey}}
          </div>
          <div class="actions-column">
          </div>
        </div>
        <div class="items">
          <div *ngFor="let item of receipt.items" class="row">
            <div *ngFor="let column of columns" [class]="'column column-' + column.span">
              <ws-add-receipt-item-product *ngIf="column.type === 'product'"
                                           [products]="products"
                                           [categories]="categoriesItem"
                                           (productChange)="onProductSelected(item, $event)"
                                           (productCategoryChange)="onProductCategorySelected($event)">
              </ws-add-receipt-item-product>
              <ws-add-receipt-item-amount *ngIf="column.type === 'amount'"
                                          [amount]="item.amount"
                                          (amountChange)="item.amount = $event">
              </ws-add-receipt-item-amount>
              <ws-add-receipt-item-cost *ngIf="column.type === 'cost'"
                                        [cost]="item.cost"
                                        (costChange)="item.cost = $event">
              </ws-add-receipt-item-cost>
            </div>
            <div class="actions-column">
              <ws-button-flat class="remove-item-button-desktop" (clicked)="removeItem(item)">(-)</ws-button-flat>
              <ws-button-flat class="remove-item-button-mobile" (clicked)="removeItem(item)">USUŃ PRODUKT</ws-button-flat>
            </div>
          </div>
        </div>
        <div>
          <ws-button-flat class="add-new-rows-button" (clicked)="addItems()">dodaj kolejne</ws-button-flat>
        </div>
        <div class="receipt-body-footer">
          <ws-add-receipt-items-summary [items]="getNonEmptyReceiptItems()"></ws-add-receipt-items-summary>
        </div>
        <ws-button class="save-button" (clicked)="save()">Zapisz</ws-button>
      </ws-panel>
    </ng-container>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'receipt-add.component.scss'
  ]
})
export class ReceiptAddComponent implements OnInit {

  receipt: Receipt;
  categoriesReceipt;
  categoriesItem;
  shops;
  products;
  accounts;
  columns = [
    {
      type: 'product',
      textKey: 'Produkt',
      span: 4
    },
    {
      type: 'amount',
      textKey: 'Ilość',
      span: 1
    },
    {
      type: 'cost',
      textKey: 'Koszt',
      span: 1
    },
  ];

  constructor(private httpClient: HttpClient) {
  }

  static itemsBatch(): Partial<ReceiptItem>[] {
    return ArraysUtil.fillWithCopies(5, {
      amount: {
        unit: 'op'
      }
    });
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

  addItems() {
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
    item.product = product;
    if (product.newValue && !product.createdWithinReceipt) {
      product.category = this.categoriesItem.find(category => category.uuid === 'root');
      console.log('Adding newly created product as available product [product=%o]', product);
      this.products.push({
        name: product.name,
        createdWithinReceipt: true
      });
    }
    if (product.defaultUnit) {
      item.amount.unit = product.defaultUnit;
    }
  }

  getNonEmptyReceiptItems() {
    return this.receipt.items.filter(item => !!item['product']);
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
        this.categoriesReceipt = (result[3] as any[]).map(category => ({
          name: category.name,
          uuid: category.uuid,
          path: category.path
        }));
        this.categoriesItem = (result[4] as any[]).map(category => ({
          name: category.name,
          uuid: category.uuid,
          path: category.path
        }));
        this.receipt = ReceiptAddComponent.newReceipt(this.categoriesReceipt.find(category => category.uuid === 'root'));
        const defaultAccount = this.accounts.find(acc => acc.useDefault === true);
        if (defaultAccount) {
          this.receipt.account = defaultAccount;
        }
      }
    );
  }

}
