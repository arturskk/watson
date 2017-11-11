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
