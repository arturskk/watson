export class ObjectsUtil {

  /**
   * Returns deep copy of passed object.
   * <p>
   *     Can't handle circular references!
   * </p>
   * @param {T} obj
   * @returns {T}
   */
  static deepCopy<T>(obj: T): T {
    return JSON.parse(JSON.stringify(obj));
  }

  /**
   * Returns objects deep equality.
   * <p>
   *     Can't handle circular references!
   * </p>
   * @param a
   * @param b
   * @returns {boolean}
   */
  static deepEquals(a: any, b: any) {
    return JSON.stringify(a) === JSON.stringify(b);
  }

  /**
   * Calls callback method only when value is provided.
   * <p>
   *     Used to create conditional consumers.
   * </p>
   * @param {T} value
   * @param {(T) => void} callback
   */
  static ifProvided<T>(value: T, consumer: (T) => void, elseCallback = () => {}) {
    if (value !== undefined) {
      consumer(value);
    } else {
      elseCallback();
    }
  }

  static getByProperty(obj: any, property: string, defaultValue?: any): any {
    return property.split('.').reduce((val, key) => val && val[key], obj);
  }

}
