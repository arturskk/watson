import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'watson-navbar',
  template: `
    <nav>
      <div>Watson!</div>
      <div><a routerLink="/receipt/add">Dodaj rachunek</a></div>
      <div><a routerLink="/report/category">Raport wydatków - kategorie</a></div>
      <div><a routerLink="/product/list">Produkty</a></div>
      <div><a routerLink="/category/receipt_item/list">Kategorie produktów</a></div>
      <div><a routerLink="/category/receipt/list">Kategorie rachunków</a></div>
    </nav>
  `
})
export class NavbarComponent implements OnInit {

  constructor() { }

  ngOnInit() {
  }

}
