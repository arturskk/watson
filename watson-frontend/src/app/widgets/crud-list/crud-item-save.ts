export interface CrudItemSave<T> {
  item: T;
  changed: T;
  rollback(message: string);
  commit();
}
