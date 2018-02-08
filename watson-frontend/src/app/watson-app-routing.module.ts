import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CategoryListComponent} from './category/list/category-list.component';
import {ProductListComponent} from './product/list/product-list.component';
import {ReceiptAddComponent} from './receipt/add/receipt-add.component';
import {ReportCategoryComponent} from './report/category/report-category.component';
import {ProductPriceReportComponent} from './report/productprice/product-price-report.component';

const routes: Routes = [
  {path: 'receipt/add', component: ReceiptAddComponent},
  {path: 'category/:type/list', component: CategoryListComponent},
  {path: 'product/list', component: ProductListComponent},
  {path: 'report/category', component: ReportCategoryComponent},
  {path: 'report/product-price', component: ProductPriceReportComponent},
  {path: '', redirectTo: '/receipt/add', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class WatsonAppRoutingModule {
}
