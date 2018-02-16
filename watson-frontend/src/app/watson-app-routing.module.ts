import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AccountListComponent} from './account/list/account-list.component';
import {CategoryListComponent} from './category/list/category-list.component';
import {ProductListComponent} from './product/list/product-list.component';
import {ReceiptAddComponent} from './receipt/add/receipt-add.component';
import {ReportCategoryComponent} from './report/category/report-category.component';
import {ProductPriceReportComponent} from './report/productprice/product-price-report.component';
import {ShopListComponent} from './shop/list/shop-list.component';

const routes: Routes = [
  {path: 'receipt/add', component: ReceiptAddComponent},
  {path: 'category/:type/list', component: CategoryListComponent},
  {path: 'product/list', component: ProductListComponent},
  {path: 'shop/list', component: ShopListComponent},
  {path: 'account/list', component: AccountListComponent},
  {path: 'report/category', component: ReportCategoryComponent},
  {path: 'report/product-price/:category', component: ProductPriceReportComponent},
  {path: '', redirectTo: '/receipt/add', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class WatsonAppRoutingModule {
}
