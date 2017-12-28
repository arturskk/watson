import {Component, HostBinding} from '@angular/core';

@Component({
  selector: 'ws-button-flat',
  template: `
    <ng-content></ng-content>
  `
})
export class ButtonFlatComponent {

  @HostBinding('class')
  get buttonClasses() {
    return 'waves-effect waves-classic';
  }

}
