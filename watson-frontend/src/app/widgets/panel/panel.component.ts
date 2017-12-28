import {Component} from '@angular/core';

@Component({
  selector: 'ws-panel',
  template: `
    <div>
      <ng-content></ng-content>
    </div>
  `
})
export class PanelComponent {

  constructor() {
  }

}
