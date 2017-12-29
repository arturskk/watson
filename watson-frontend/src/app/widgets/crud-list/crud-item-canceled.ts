export interface CrudItemCanceled<T> {
  item: Partial<T>;
  changed: Partial<T>;
}
