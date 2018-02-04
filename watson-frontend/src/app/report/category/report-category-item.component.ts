import {Component, Input} from '@angular/core';
import {CategoryReportItem} from './category-report-item';

@Component({
  selector: 'ws-report-category-item',
  template: `
    <div>
      <div (click)="category.subCategories.length > 0 && expanded = !expanded" class="description">
        <span>{{category.name}} ({{category.totalCost.description}}zł)</span>
        <ng-container *ngIf="category.subCategories.length > 0">
          <a *ngIf="expanded">(-)</a>
          <a *ngIf="!expanded">(+)</a>
        </ng-container>
      </div>
      <div class="subcategories" *ngIf="expanded">
        <span class="category-summary">({{category.name}} {{category.categoryCost.description}}zł)</span>
        <ng-container *ngFor="let subCategory of category.subCategories">
          <ws-report-category-item *ngIf="showEmpty || subCategory.totalCost.amount > 0" 
                                   [category]="subCategory">
          </ws-report-category-item>
        </ng-container>
      </div>
    </div>
  `,
  styleUrls: [
    'report-category-item.component.scss'
  ]
})
export class ReportCategoryItemComponent {

  @Input() category: CategoryReportItem;
  @Input() showEmpty = false;
  expanded = false;

}
