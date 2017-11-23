import {HttpClient} from "@angular/common/http";
import {Component, OnInit} from "@angular/core";
import {Category} from "../../category/Category";
import {Product} from "../Product";
import {ProductListWrapper} from "./ProductListWrapper";

@Component({
  selector: 'product-list-component',
  template: `
    <h1>Produkty</h1>
    <h2>Dodaj produkt</h2>
    <div *ngIf="categories" class="add-new-category">
      <input [(ngModel)]="newProduct.name" placeholder="Nazwa"/>
      <select-component 
        [(ngModel)]="newProduct.category" 
        [data]="categories" 
        [displayField]="'name'" 
        [allowNewValues]="false" 
        [placeholder]="'Kategoria'">
        <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
          <span [innerHTML]="markSearchText(item.name)"></span>
        </ng-template>
      </select-component>
      <a (click)="addProduct()">(dodaj)</a>
    </div>
    <h2>Lista produktów</h2>
    <div>
      <div *ngFor="let productWrapper of products">
        <div *ngIf="!productWrapper.edit">
          {{productWrapper.product.name}} ({{productWrapper.product.categoryPath}})
          <a (click)="onProductEdit(productWrapper)">(edytuj)</a>
        </div>
        <div *ngIf="productWrapper.edit">
          <input [(ngModel)]="productWrapper.changes.name"/>
          <select-component
            [data]="categories"
            [displayField]="'name'"
            [placeholder]="'Kategoria'"
            [allowNewValues]="false"
            [(ngModel)]="productWrapper.changes.category">
            <ng-template let-item let-markSearchText="markSearchText" #listItem>
              <span [innerHTML]="markSearchText(item.name)"></span>
            </ng-template>
          </select-component>
          <a (click)="onProductSave(productWrapper)">(zapisz)</a>
        </div>
      </div>
    </div>
  `,
  styles: [
      `
      .add-new-category {
        display: flex;
      }
    `
  ]
})
export class ProductListComponent implements OnInit {

  products: ProductListWrapper[];
  categories: Category[];
  newProduct: {name?: string, category?: Category} = {};

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<Category[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
    this.httpClient
      .get<Product[]>('/api/v1/product')
      .subscribe(data => this.products = data.map(product => new ProductListWrapper(product)));
  }

  addProduct() {
    if (!!this.newProduct.name && this.newProduct.category) {
      // TODO: send changes to server
      console.log(this.newProduct);
      this.newProduct = {};
    } else {
      alert('Błąd walidacji');
    }
  }

  onProductEdit(productWrapper: ProductListWrapper) {
    productWrapper.startEdit()
  }

  onProductSave(productWrapper: ProductListWrapper) {
    const changes = productWrapper.endEdit();
    // TODO: send changes to server
    console.log(changes);
    if (changes.name) {
      productWrapper.product.name = changes.name;
    }
    if (changes.categoryUuid) {
      productWrapper.product.categoryUuid = changes.categoryUuid;
      productWrapper.product.categoryName = changes.categoryName;
      productWrapper.product.categoryPath = changes.categoryPath;
    }
  }

}
