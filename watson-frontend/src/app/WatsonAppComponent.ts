import {Component} from '@angular/core';

@Component({
  selector: 'watson-app',
  template: `
    <nav>
      <div><a routerLink="/receipt/add">Dodaj rachunek</a></div>
      <div>&nbsp;|&nbsp;</div>
      <div><a routerLink="/product/list">Produkty</a></div>
      <!--<div>&nbsp;|&nbsp;</div>-->
      <!--<div><a routerLink="/category/list">Kategorie rachunków</a></div>-->
      <div>&nbsp;|&nbsp;</div>
      <div><a routerLink="/category/list">Kategorie produktów</a></div>
    </nav>
    <main>
      <router-outlet></router-outlet>
    </main>
    <footer>
      Watson footer
    </footer>
  `
})
export class WatsonAppComponent {
}
