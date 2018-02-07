import {Cost} from '../../cost/cost';

export interface CategoryReportItem {

  categoryCost: Cost;
  name: string;
  subCategories: CategoryReportItem[];
  totalCost: Cost;
  type: string;
  uuid: string;

}
