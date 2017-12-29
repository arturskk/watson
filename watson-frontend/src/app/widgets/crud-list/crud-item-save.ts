import {CrudItemState} from './crut-item-state';

export interface CrudItemSaveCommitConfig<T> {
  value?: Partial<T>;
  state?: CrudItemState;
}

export interface CrudItemSaveRollbackConfig<T> {
  message?: string;
  state?: CrudItemState;
}

export interface CrudItemSave<T> {
  item: Partial<T>;
  changed: Partial<T>;
  rollback(config: CrudItemSaveRollbackConfig<T>);
  commit(config: CrudItemSaveCommitConfig<T>);
}
