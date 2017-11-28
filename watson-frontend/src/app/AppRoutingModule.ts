import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {CategoryListComponent} from './category/list/CategoryListComponent';
import {ProductListComponent} from './product/list/ProductListComponent';
import {AddReceiptComponent} from './receipt/add/AddReceiptComponent';
import {ReportCategoryComponent} from './report/category/ReportCategoryComponent';

const routes: Routes = [
  {path: 'receipt/add', component: AddReceiptComponent},
  {path: 'category/:type/list', component: CategoryListComponent},
  {path: 'product/list', component: ProductListComponent},
  {path: 'report/category', component: ReportCategoryComponent},
  {path: '', redirectTo: '/receipt/add', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
