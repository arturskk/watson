import {Component} from '@angular/core';

@Component({
  selector: 'watson-navbar',
  template: `
    <nav>
      <div class="brand-name">Watson!</div>
      <div><a routerLink="/receipt/add">Dodaj rachunek</a></div>
      <div><a routerLink="/report/category">Raport wydatków</a></div>
      <div><a routerLink="/report/product-price/root">Raport cen produktów</a></div>
      <div><a routerLink="/product/list">Produkty</a></div>
      <div><a routerLink="/category/receipt_item/list">Kategorie produktów</a></div>
      <div><a routerLink="/category/receipt/list">Kategorie rachunków</a></div>
    </nav>
  `,
  styleUrls: [
    'navbar.component.scss'
  ]
})
export class NavbarComponent {

}
