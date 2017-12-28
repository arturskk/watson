import {ObjectsUtil} from './objects-util';

export class ArraysUtil {

  static fillWithCopies<T>(times: number, prototype: T): T[] {
    const result = [];
    for (let idx = 0; idx < times; ++idx) {
      result.push(ObjectsUtil.deepCopy(prototype));
    }
    return result;
  }

}
