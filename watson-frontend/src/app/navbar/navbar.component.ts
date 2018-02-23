import {Component} from '@angular/core';
import {Router} from '@angular/router';

@Component({
  selector: 'watson-navbar',
  template: `
    <nav>
      <div class="brand-name">Watson!</div>
      <div class="navbar-menu" [ngClass]="{'open': mobileMenuOpen}">
        <div class="menu-container">
          <div><a routerLink="/receipt/add" routerLinkActive="active-link">Dodaj rachunek</a></div>
          <div><a routerLink="/report/category" routerLinkActive="active-link">Raport wydatków</a></div>
          <div><a routerLink="/report/product-price/root" routerLinkActive="active-link">Raport cen produktów</a></div>
          <div><a routerLink="/product/list" routerLinkActive="active-link">Produkty</a></div>
          <div><a routerLink="/shop/list" routerLinkActive="active-link">Sklepy</a></div>
          <div><a routerLink="/retail-chain/list" routerLinkActive="active-link">Sieci handlowe</a></div>
          <div><a routerLink="/producer/list" routerLinkActive="active-link">Producenci</a></div>
          <div><a routerLink="/account/list" routerLinkActive="active-link">Konta</a></div>
          <div><a routerLink="/category/receipt_item/list" routerLinkActive="active-link">Kategorie produktów</a></div>
          <div><a routerLink="/category/receipt/list" routerLinkActive="active-link">Kategorie rachunków</a></div>
        </div>
      </div>
      <ws-hamburger [active]="mobileMenuOpen" (clicked)="toggleMenu()"></ws-hamburger>
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
