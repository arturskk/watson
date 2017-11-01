import {Injectable} from '@angular/core';

@Injectable()
export class ReceiptService {

  addReceipt() {
    const receipt = {
      date: '',
      uuid: '',
      budgetUuid: '',
      accountUuid: '',
      shopUuid: '',
      categoryUuid: '',
      tags: [],
      items: [
        {
          description: '',
          categoryUuid: '',
          productUuid: '',
          tags: [],
          cost: {amount: ''}
        }
      ]
    };
    const event = {
      payload: receipt
    };
  }

}
