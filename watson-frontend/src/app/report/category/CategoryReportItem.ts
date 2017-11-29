import {ExpanseCost} from './ExpanseCost';

export interface CategoryReportItem {

  categoryCost: ExpanseCost;
  name: string;
  subCategories: CategoryReportItem[];
  totalCost: ExpanseCost;
  type: string;
  uuid: string;

}
