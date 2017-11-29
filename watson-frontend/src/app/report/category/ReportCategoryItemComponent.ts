import {Component, Input} from '@angular/core';

@Component({
  selector: 'report-category-item',
  template: `
    <div>
      <div (click)="expanded = !expanded" class="description">
        <ng-container *ngIf="category.subCategories && category.subCategories.length > 0">
          <span *ngIf="expanded">(-)</span>
          <span *ngIf="!expanded">(+)</span>
        </ng-container>
        <ng-container *ngIf="!category.subCategories || category.subCategories.length == 0">
          <span *ngIf="!expanded">(&nbsp;)</span>
        </ng-container>
        <span>{{category.name}} - {{category.totalCost.description}}zł</span>
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

  @Input() category: any;
  expanded = false;

}
