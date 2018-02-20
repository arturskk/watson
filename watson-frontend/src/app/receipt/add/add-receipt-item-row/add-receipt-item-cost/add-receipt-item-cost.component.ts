import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'ws-add-receipt-item-cost',
  template: `
    <div class="label">Koszt</div>
    <div class="item-cost-input-wrapper">
        <input type="number" [value]="cost || ''" (change)="costChange.next($event.target.value)" />
    </div>
  `,
  styleUrls: [
    'add-receipt-item-cost.component.scss'
  ]
})
export class AddReceiptItemCostComponent {

  @Input() cost;
  @Output() costChange = new EventEmitter();

}
