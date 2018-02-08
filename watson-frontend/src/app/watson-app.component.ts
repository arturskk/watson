import {Component} from '@angular/core';

@Component({
  selector: 'watson-app',
  template: `
    <watson-navbar></watson-navbar>
    <main>
      <router-outlet></router-outlet>
    </main>
    <footer>
      <ws-app-version></ws-app-version>
    </footer>
  `,
  styleUrls: [
    'watson-app.component.scss'
  ]
})
export class WatsonAppComponent {
}
