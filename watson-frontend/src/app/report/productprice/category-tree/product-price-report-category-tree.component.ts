import {HttpClient} from '@angular/common/http';
import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {CategoryTreeDto, CategoryTreeDtoData} from '../../../category/list/category-tree-dto';

@Component({
  selector: 'ws-product-price-report-category-tree',
  template: `
    <ng-container *ngIf="categoryTree; else spinner">
      <ws-product-price-category-tree-item [item]="categoryTree"
                                           [expandedItems]="expandedItems"
                                           (selected)="expandCategory($event)">
      </ws-product-price-category-tree-item>
    </ng-container>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'product-price-report-category-tree.component.scss'
  ]
})
export class ProductPriceReportCategoryTreeComponent implements OnInit, OnChanges {

  @Input() categoryUuid: string;
  @Output() categorySelected = new EventEmitter<CategoryTreeDto>();
  expandedItems: CategoryTreeDto[] = []
  categoryTree: CategoryTreeDto;
  categoryList: CategoryTreeDto[] = [];

  constructor(private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.httpClient
      .get<CategoryTreeDtoData>(`/api/v1/category/tree/_category_receipt_item`)
      .map(tree => new CategoryTreeDto(tree))
      .subscribe(tree => {
        this.categoryTree = tree;
        this.categoryTree.accept(
          category => this.categoryList.push(category)
        );
        if (this.categoryUuid) {
          this.refreshExpandedTree();
        }
      });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.categoryUuid && this.categoryTree) {
      this.refreshExpandedTree();
    }
  }

  expandCategory(selectedCategory: CategoryTreeDto) {
    if (selectedCategory.uuid === this.categoryUuid) {
      this.categorySelected.next(selectedCategory.parent);
    } else {
      this.categorySelected.next(selectedCategory);
    }
  }

  private refreshExpandedTree() {
    let categoryPath = [];
    let parent = this.categoryList.find(i => i.uuid === this.categoryUuid);
    while (parent !== undefined) {
      categoryPath = [parent, ...categoryPath];
      parent = parent.parent;
    }
    this.expandedItems = categoryPath;
  }

}
