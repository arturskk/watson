import {Component} from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'watson-navbar',
  template: `
    <nav>
      <div class="brand-name">Watson!</div>
      <div class="navbar-menu" [ngClass]="{'open': mobileMenuOpen}">
        <div class="menu-container">
          <div><a routerLink="/receipt/add">Dodaj rachunek</a></div>
          <div><a routerLink="/report/category">Raport wydatków</a></div>
          <div><a routerLink="/report/product-price/root">Raport cen produktów</a></div>
          <div><a routerLink="/product/list">Produkty</a></div>
          <div><a routerLink="/shop/list">Sklepy</a></div>
          <div><a routerLink="/retail-chain/list">Sieci handlowe</a></div>
          <div><a routerLink="/account/list">Konta</a></div>
          <div><a routerLink="/category/receipt_item/list">Kategorie produktów</a></div>
          <div><a routerLink="/category/receipt/list">Kategorie rachunków</a></div>
        </div>
      </div>
      <watson-hamburger [active]="mobileMenuOpen" (clicked)="toggleMenu()"></watson-hamburger>
      <div *ngIf="mobileMenuOpen" class="mask" (click)="onMaskClick()"></div>
    </nav>
  `,
  styleUrls: [
    'navbar.component.scss'
  ]
})
export class NavbarComponent {

  mobileMenuOpen = false;

  constructor(private router: Router) {
    //TODO jak to inaczej zrobic???
    this.router.events
      .subscribe(e => this.mobileMenuOpen = false);

  }

  toggleMenu() {
    this.mobileMenuOpen = !this.mobileMenuOpen;
  }

  onMaskClick() {
    this.mobileMenuOpen = false;
  }

}
