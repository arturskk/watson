import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'joinArray'
})
export class JoinArrayPipe implements PipeTransform {

  transform(value: any[], args?: any): any {
    if (!value) {
      return value;
    }
    return value.join(args || ', ');
  }

}
