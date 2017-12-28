import {ExpanseCost} from './expanse-cost';

export interface CategoryReportItem {

  categoryCost: ExpanseCost;
  name: string;
  subCategories: CategoryReportItem[];
  totalCost: ExpanseCost;
  type: string;
  uuid: string;

}
