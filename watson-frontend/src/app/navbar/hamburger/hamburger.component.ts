import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'ws-hamburger',
  template: `
    <div class="hamburger-container" [ngClass]="{'active': active}" (click)="hamburgerClicked($event)">
      <div class="bar top"></div>
      <div class="bar middle"></div>
      <div class="bar bottom"></div>
    </div>
  `,
  styleUrls: [
    'hamburger.component.scss'
  ]
})
export class HamburgerComponent {

  @Input() active : boolean;

  @Output() clicked = new EventEmitter();

  hamburgerClicked($event): void {
    this.clicked.next($event);
  }

}
