import {Component, EventEmitter, Output} from '@angular/core';

@Component({
  selector: 'ws-button',
  template: `
    <button (click)="clicked.next($event)">
      <ng-content></ng-content>
    </button>
  `,
  styleUrls: [
    'button.component.scss'
  ]
})
export class ButtonComponent {

  @Output() clicked = new EventEmitter();

}
