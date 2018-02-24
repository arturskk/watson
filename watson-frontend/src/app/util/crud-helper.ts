import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {CrudItemState} from '../widgets/crud-list/crut-item-state';
import {CrudItemSave} from '../widgets/crud-list/crud-item-save';
import {Observable} from 'rxjs/Observable';
import {Subject} from 'rxjs/Subject';

export interface CrudResource<T> {
  added: (crudItemSave: CrudItemSave<T>) => void;
  edited: (crudItemSave: CrudItemSave<T>) => void;
}

@Injectable()
export class CrudHelper<T> {

  static readonly defaultUuidExtractor = (crudItemSave: CrudItemSave<any>) => crudItemSave.item['uuid'];

  constructor(private httpClient: HttpClient) {
  }

  asResource(config: {
    api: string,
    mapper: (crudItemSave: CrudItemSave<T>) => any,
    uuidExtractor?: (crudItemSave: CrudItemSave<T>) => string, onSuccess?: any
  }): CrudResource<T> {
    return {
      added: ev => this.itemAdded(ev, {
        api: config.api,
        diff: config.mapper(ev)
      }).subscribe(() => config.onSuccess ? config.onSuccess() : undefined),
      edited: ev => this.itemEdited(ev, {
        api: `${config.api}/${(config.uuidExtractor || CrudHelper.defaultUuidExtractor)(ev)}`,
        diff: config.mapper(ev)
      }).subscribe(() => config.onSuccess ? config.onSuccess() : undefined)
    };
  }

  itemAdded(crudItemSave: CrudItemSave<T>, config: { api: string; diff: any }): Observable<void> {
    const subject = new Subject<void>();
    if (config.diff) {
    this.httpClient
      .post(config.api, config.diff)
      .subscribe(
        () => {
          crudItemSave.commit({
            value: {},
            state: CrudItemState.EDIT
          });
          subject.next();
        },
        response => crudItemSave.rollback({
          message: response.error.errors ? response.error.errors.map(error => `${error.field} ${error.defaultMessage}`) : response.error.message
        })
      );
    } else {
      crudItemSave.commit({
        value: {},
        state: CrudItemState.EDIT
      });
      subject.next();
    }
    return subject.asObservable();
  }

  itemEdited(crudItemSave: CrudItemSave<T>, config: { api: string; diff: any }) {
    const subject = new Subject<void>();
    if (config.diff) {
      this.httpClient
        .put(config.api, config.diff)
        .subscribe(
          () => {
            crudItemSave.commit();
            subject.next();
          },
          response => crudItemSave.rollback({
            message: response.error.errors ? response.error.errors.map(error => `${error.field} ${error.defaultMessage}`) : response.error.message
          })
        );
    } else {
      crudItemSave.commit();
      subject.next();
    }
    return subject.asObservable();
  }

}
