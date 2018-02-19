import {Component, EventEmitter, Input, Output} from '@angular/core';

@Component({
  selector: 'ws-add-receipt-item-amount',
  template: `
    <div class="item-amount">
      <input type="number" [value]="amount.count || ''" (change)="countChanged($event.target.value)" />
    </div>
    <div>
      <select [value]="amount.unit || 'op'" (change)="unitChanged($event.target.value)">
        <option>op</option>
        <option>szt</option>
        <option>kg</option>
        <option>l</option>
      </select>
    </div>
  `,
  styleUrls: [
    'add-receipt-item-amount.component.scss'
  ]
})
export class AddReceiptItemAmountComponent {

  @Input() amount;
  @Output() amountChange = new EventEmitter();

  countChanged(count) {
    this.amount.count = count;
    this.amountChange.next(this.amount);
  }

  unitChanged(unit) {
    this.amount.unit = unit;
    this.amountChange.next(this.amount);
  }

}
