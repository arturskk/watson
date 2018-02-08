import {Component} from '@angular/core';

@Component({
  selector: 'ws-panel',
  template: `
    <ng-content></ng-content>
  `,
  styleUrls: [
    'panel.component.scss'
  ]
})
export class PanelComponent {

  constructor() {
  }

}
