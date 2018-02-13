export interface CategoryTreeDtoData {
  depth: number;
  type: string;
  uuid: string;
  name: string;
  path: string[];
  children: CategoryTreeDtoData[];
}

export class CategoryTreeDto {

  depth: number;
  type: string;
  uuid: string;
  name: string;
  path: string[];
  pathString: string;
  children: CategoryTreeDto[];
  parent: CategoryTreeDto;

  constructor(data: CategoryTreeDtoData, parent?: CategoryTreeDto) {
    this.depth = data.depth;
    this.type = data.type;
    this.uuid = data.uuid;
    this.name = data.name;
    this.path = data.path;
    this.pathString = this.path.join(' > ');
    this.parent = parent;
    this.children = data.children.map(child => new CategoryTreeDto(child, this));
  }

  accept(consumer: (CategoryTreeDto) => void) {
    consumer(this);
    this.children.forEach(child => child.accept(consumer));
  }

}
