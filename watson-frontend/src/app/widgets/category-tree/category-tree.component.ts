import {HttpClient} from '@angular/common/http';
import {Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges} from '@angular/core';
import {CategoryTreeDto, CategoryTreeDtoData} from '../../category/category-tree-dto';

@Component({
  selector: 'ws-category-tree',
  template: `
    <ng-container *ngIf="categoryTree; else spinner">
      <div class="search-box">
        <ws-select
          (onChange)="expandCategory($event)"
          [data]="categoryList"
          [skipValueWriting]="true"
          [displayField]="'name'"
          [allowNewValues]="false"
          [placeholder]="'Szukaj'">
          <ng-template let-item let-markSearchText="markSearchText" let-newItem="newItem" #listItem>
            <span [innerHTML]="markSearchText.call(undefined, item.name) | safeHtml"></span>
            <div *ngIf="item.path" class="select-item-description">
              {{item.path | joinArray: ' > '}}
            </div>
          </ng-template>
        </ws-select>
      </div>
      <ws-category-tree-item [item]="categoryTree"
                          [expandedItems]="expandedItems"
                          (selected)="expandCategory($event)">
      </ws-category-tree-item>
    </ng-container>
    <ng-template #spinner>
      <ws-spinner></ws-spinner>
    </ng-template>
  `,
  styleUrls: [
    'category-tree.component.scss'
  ]
})
export class CategoryTreeComponent implements OnInit, OnChanges {

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
    if (selectedCategory && selectedCategory.uuid === this.categoryUuid) {
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
