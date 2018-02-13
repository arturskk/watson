import {Component, EventEmitter, HostBinding, Input, OnChanges, Output, SimpleChanges} from '@angular/core';
import {CategoryTreeDto} from '../../../category/category-tree-dto';

@Component({
  selector: 'ws-category-tree-item',
  template: `
    <div (click)="selected.next(item)" class="item-summary">
      <div class="name" [class.expanded]="expanded" [class.active]="isFirstPlanDepth || isCurrentCategory">
        {{item.name}}
      </div>
      <div class="expand-icon" *ngIf="expandable && item.parent" [class.active]="isFirstPlanDepth">
        <i class="far fa-minus-square" *ngIf="expanded"></i>
        <i class="far fa-plus-square" *ngIf="!expanded"></i>
      </div>
    </div>
    <div *ngIf="expandable && expandedItems.indexOf(item) >= 0">
      <ng-container *ngFor="let child of sortedChildren">
        <ws-category-tree-item [item]="child"
                            [expandedItems]="expandedItems"
                            (selected)="selected.next($event)">
        </ws-category-tree-item>
      </ng-container>
    </div>
  `,
  styleUrls: ['./category-tree-item.component.scss']
})
export class CategoryTreeItemComponent implements OnChanges {

  @Input() item: CategoryTreeDto;
  @Input() sortedChildren: CategoryTreeDto[];
  @Input() expandedItems: CategoryTreeDto[];
  @Output() selected = new EventEmitter();
  @HostBinding('attr.depth') get attrDepth() {
    return this.item.depth;
  }
  expanded: boolean;
  expandable: boolean;
  isFirstPlanDepth: boolean;
  isCurrentCategory: boolean;

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.item || changes.expandedItems) {
      this.sortedChildren = [...this.item.children];
      const expandedChild = this.sortedChildren.findIndex(i => this.expandedItems.indexOf(i) >= 0);
      if (expandedChild >= 0) {
        this.sortedChildren.splice(0, 0, this.sortedChildren.splice(expandedChild, 1)[0]);
      }
      this.expandable = this.item.children && this.item.children.length > 0;
      this.expanded = this.expandedItems.indexOf(this.item) >= 0;
      this.isFirstPlanDepth = this.expandedItems.length === 0 || this.expandedItems[this.expandedItems.length - 1].depth + 1 === this.item.depth;
      this.isCurrentCategory = this.expandedItems.length === 0 || this.expandedItems[this.expandedItems.length - 1] === this.item;
    }
  }

}
