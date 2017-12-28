import {Component} from '@angular/core';

@Component({
  selector: 'watson-app',
  template: `
    <watson-navbar></watson-navbar>
    <main>
      <router-outlet></router-outlet>
    </main>
    <footer>
      Watson! v.0.2.0 (<a>#abc3few3</a>)
    </footer>
  `
})
export class AppComponent {
}
