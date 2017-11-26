import {ObjectUtil} from './ObjectUtil';

export class ArrayUtil {

  static fillWithCopies<T>(times: number, prototype: T): T[] {
    const result = [];
    for (let idx = 0; idx < times; ++idx) {
      result.push(ObjectUtil.deepCopy(prototype));
    }
    return result;
  }

}
