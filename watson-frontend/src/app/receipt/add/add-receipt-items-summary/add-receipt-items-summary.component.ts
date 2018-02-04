import {Component, Input} from '@angular/core';

@Component({
  selector: 'ws-add-receipt-items-summary',
  template: `
    <div class="receipt-summary-cell">Pozycje: {{nonEmptyReceiptItemCount()}}</div>
    <div class="receipt-summary-cell">Kwota: {{receiptSummaryCost()}}zł</div>
  `,
  styleUrls: [
    'add-receipt-items-summary.component.scss'
  ]
})
export class AddReceiptItemsSummaryComponent {

  @Input() items;

  nonEmptyReceiptItemCount(): number {
    return this.items.length;
  }

  receiptSummaryCost(): number {
    return this.items
      .map(item => item.cost)
      .reduce((prev, current) => ((parseFloat(current) || 0) + (parseFloat(prev) || 0)), 0)
      .toFixed(2);
  }

}
