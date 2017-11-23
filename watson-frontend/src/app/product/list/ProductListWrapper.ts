import {Product} from "../Product";

export class ProductListWrapper {

  public edit: boolean = false
  public changes: {name: string, category: { name: string, uuid: string, path: string }};

  constructor(public product: Product) {
  }

  startEdit() {
    this.edit = true;
    this.changes = {
      name: this.product.name,
      category: {
        uuid: this.product.categoryUuid,
        name: this.product.categoryName,
        path: this.product.categoryPath
      }
    };
  }

  endEdit(): Partial<Product> {
    this.edit = false;
    const result: Partial<Product> = {};
    if (this.product.name !== this.changes.name) {
      result.name = this.changes.name;
    }
    if (this.product.categoryUuid !== this.changes.category.uuid) {
      result.categoryUuid = this.changes.category.uuid;
      result.categoryName = this.changes.category.name;
      result.categoryPath = this.changes.category.path;
    }
    return result;
  }

}
