export interface ProductSummary {

  uuid: string;
  name: string;
  category: {
    uuid: string;
    name: string;
    path: string;
  };
  defaultUnit: string;

}
