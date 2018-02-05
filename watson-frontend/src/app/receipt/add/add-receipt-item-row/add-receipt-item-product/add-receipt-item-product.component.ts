import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'ws-add-receipt-item-product',
  template: `
    <div>
      <ws-select [data]="products"
                 [displayField]="'name'"
                 (onChange)="onProductChange($event)">
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
    </div>
    <div *ngIf="product && product.newValue && !product.createdWithinReceipt" class="new-product-category-row">
      <div>Kategoria nowego produktu:</div>
      <ws-select [data]="categories" 
                 [displayField]="'name'" 
                 (onChange)="productCategoryChange.next($event)"
                 [(ngModel)]="product.category">
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
  `,
  styleUrls: [
    'add-receipt-item-product.component.scss'
  ]
})
export class AddReceiptItemProductComponent {

  @Input() products;
  @Input() categories;
  @Input() product;
  @Output() productChange = new EventEmitter();
  @Output() productCategoryChange = new EventEmitter();

  onProductChange(product) {
    this.product = product;
    this.productChange.next(product);
  }

}
