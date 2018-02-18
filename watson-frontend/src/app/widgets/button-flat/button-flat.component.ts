import {Component, EventEmitter, HostBinding, Output} from '@angular/core';

@Component({
  selector: 'ws-button-flat',
  template: `
    <div (click)="clicked.next()">
      <ng-content></ng-content>
    </div>
  `,
  styleUrls: [
    'button-flat.component.scss'
  ]
})
export class ButtonFlatComponent {

  @Output() clicked = new EventEmitter();

}
