import {DiffUtil} from './DiffUtil';
import {ObjectUtil} from './ObjectUtil';

describe('DiffUtil', () => {

  it('should generate diff using description', () => {
    // given
    const objA = {
      prop1: 'Property1',
      prop2: 'Property2',
      prop3: {
        prop4: 'Property4',
        prop5: 'Property5',
      }
    };
    const objB = {
      prop1: objA.prop1 + '_changed',
      prop2: objA.prop2,
      prop3: {
        prop4: objA.prop3.prop4 + '_changed',
        prop5: objA.prop3.prop5
      }
    };

    // when
    const diffBuilder = DiffUtil.diffBuilder(objA, objB);
    const standardDiff = DiffUtil.skipIfWithoutChange({
      prop1: diffBuilder('prop1'),
      prop2: diffBuilder('prop2'),
      prop3: DiffUtil.skipIfWithoutChange({
        prop4: diffBuilder('prop3.prop4'),
        prop5: diffBuilder('prop3.prop5')
      }),
      complex: diffBuilder('prop3.prop4')
    });
    const descDiff = DiffUtil.diff(objA, objB, {
      prop1: null,
      prop2: null,
      prop3: {
        prop4: null,
        prop5: null
      },
      complex: 'prop3.prop4'
    });

    // then
    // noinspection JSIgnoredPromiseFromCall
    expect(ObjectUtil.deepEquals(standardDiff, descDiff)).toBeTruthy();
  });

});
