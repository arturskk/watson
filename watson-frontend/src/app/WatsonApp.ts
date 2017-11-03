import {Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs/Observable';

@Component({
  selector: 'watson-app',
  template: `
    <nav>
      <div>Dashboard</div>
      <div>Budżet</div>
      <div>Zakupy</div>
      <div>Dieta</div>
    </nav>
    <aside>
      sidebar
    </aside>
    <main>
      <h1>Dodaj wydatek</h1>
      <div>
        <label>Data <input/></label>
      </div>
      <div>
        <label>Kategoria <input/></label>
      </div>
      <div>
        <label>Konto <input/></label>
      </div>
      <div>
        <button>dodaj</button>
      </div>
    </main>
    <footer>
      Watson footer
    </footer>
  `,
  styles: [
      `
      nav {
        display: flex;
      }

      aside {
        display: none;
      }

      main {
        border: 1px solid gray;
      }

      footer {
        font-size: 0.8em;
        font-style: italic;
      }
    `
  ]
})
export class WatsonApp implements OnInit {

  constructor(private httpClient: HttpClient) {}

  ngOnInit(): void {
    const accounts = this.httpClient.get('/api/v1/account');
    const categoriesReceipt = this.httpClient.get('/api/v1/category/_category_receipt');
    const categoriesReceiptItems = this.httpClient.get('/api/v1/category/_category_receipt_item');
    const products = this.httpClient.get('/api/v1/product');
    const shops = this.httpClient.get('/api/v1/shop');

    Observable.forkJoin(
      accounts,
      categoriesReceipt,
      categoriesReceiptItems,
      products,
      shops
    ).subscribe(result => {
      console.log(result);
    });
  }

}
