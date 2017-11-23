import {HttpClient} from "@angular/common/http";
import {Component, OnInit} from "@angular/core";
import {Category} from "../../category/Category";
import {Product} from "../Product";

@Component({
  selector: 'product-list-component',
  template: `
    <h1>Produkty</h1>
    <h2>Dodaj produkt</h2>
    <div *ngIf="categories" class="add-new-category">
      <input placeholder="Nazwa" />
      <select-component 
        [data]="categories"
        [displayField]="'name'"
        [placeholder]="'Kategoria'">
        <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
          <span *ngIf="newItem">Dodaj: </span>
          <span [innerHTML]="markSearchText(item.name)"></span>
        </ng-template>
      </select-component>
      <a>(dodaj)</a>
    </div>
    <h2>Lista produktów</h2>
    <div>
      <div *ngFor="let product of products">
        {{product.name}} ({{product.categoryPath}})
        <a>(edytuj)</a>
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

  products: Product[];
  categories: Category[];

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<Category[]>('/api/v1/category/_category_receipt_item')
      .subscribe(data => this.categories = data);
    this.httpClient
      .get<Product[]>('/api/v1/product')
      .subscribe(data => this.products = data);
  }

}
