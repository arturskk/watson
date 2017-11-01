import {Component} from '@angular/core';

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
export class WatsonApp {
}
