export class DiffsUtil {

  static skipIfWithoutChange(obj: any) {
    for (const key of Object.keys(obj)) {
      if (obj[key] !== undefined) {
        return obj;
      }
    }
    return undefined;
  }

  static diffBuilder(newObject: any, oldObject: any): (attribute: string) => any {
    return (attribute: string) => this.returnIfChanged(newObject, oldObject, attribute);
  }

  static returnIfChanged(newObject: any, oldObject: any, property: string): any {
    const newValue = this.getByProperty(newObject, property);
    const oldValue = this.getByProperty(oldObject, property);
    if (newValue !== oldValue) {
      return newValue;
    } else {
      return undefined;
    }
  }

  static getByProperty(obj: any, property: string): any {
    return property.split('.').reduce((val, key) => val && val[key], obj);
  }

  static diff(a: any, b: any, desc: {[key: string]: any}): any {
    const result = {};
    const diffBuilder = DiffsUtil.diffBuilder(a, b);
    for (const key of Object.keys(desc)) {
      const mapping = desc[key];
      if (typeof mapping === 'object' && mapping) {
        result[key] = DiffsUtil.diff(a[key], b[key], desc[key]);
      } else if (typeof mapping === 'string') {
        result[key] = diffBuilder(mapping);
      } else {
        result[key] = diffBuilder(key);
      }
    }
    return DiffsUtil.skipIfWithoutChange(result);
  }

}
