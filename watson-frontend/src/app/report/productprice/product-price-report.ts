export interface ProductPriceReportItemShop {
  uuid: string;
  name: string;
}

export interface ProductPriceReportItemProduct {
  uuid: string;
  name: string;
}

export interface ProductPriceReportItemCategory {
  uuid: string;
  name: string;
}

export interface ProductPriceReportItemReceipt {
  uuid: string;
  itemUuid: string;
}

export interface ProductPriceReportItem {
  id: number;
  categoryPath: ProductPriceReportItemCategory[];
  product: ProductPriceReportItemProduct;
  shop: ProductPriceReportItemShop;
  receipt: ProductPriceReportItemReceipt;
  date: string;
  pricePerUnit: string;
  unit: string;
}

export interface ProductPriceReport {
  categoryUuid: string;
  includeSubcategories: boolean;
  items: ProductPriceReportItem[];
}
