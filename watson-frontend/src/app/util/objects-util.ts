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

}
