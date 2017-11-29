import {Component, Input} from '@angular/core';
import {CategoryReportItem} from './CategoryReportItem';

@Component({
  selector: 'report-category-item',
  template: `
    <div>
      <div (click)="expanded = !expanded" class="description">
        <span *ngIf="expanded">(-)</span>
        <span *ngIf="!expanded">(+)</span>
        <span>{{category.name}} - (Kategoria: {{category.categoryCost.description}}zł, Suma: {{category.totalCost.description}}zł)</span>
      </div>
      <div class="subcategories" *ngIf="expanded">
        <report-category-item [category]="subCategory" *ngFor="let subCategory of category.subCategories">
        </report-category-item>
      </div>
    </div>
  `,
  styles: [
      `
      :host {
        display: block;
      }

      .subcategories {
        padding-left: 25px;
      }

      .description {
        cursor: pointer;
      }
    `
  ]
})
export class ReportCategoryItemComponent {

  @Input() category: CategoryReportItem;
  expanded = false;

}
