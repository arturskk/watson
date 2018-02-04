import {Component} from '@angular/core';

@Component({
  selector: 'ws-panel',
  template: `
    <div>
      <ng-content></ng-content>
    </div>
  `,
  styleUrls: [
    'panel.component.scss'
  ]
})
export class PanelComponent {

  constructor() {
  }

}
