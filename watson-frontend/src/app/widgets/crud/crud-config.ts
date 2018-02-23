import {CrudItemSave} from '../crud-list/crud-item-save';

export interface CrudConfig<T> {
  keys: {
    itemAddKey: string;
    itemListKey: string;
  };
  api: {
    endpoint: string;
  };
  model: {
    mapper: (crudItemSave: CrudItemSave<T>) => any;
  }
}
