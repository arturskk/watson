import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {NavbarComponent} from './navbar/navbar.component';
import {AppComponent} from './app.component';
import {PanelComponent} from './widgets/panel/panel.component';
import {SelectComponent} from './widgets/select/select.component';
import {CategoryListItemComponent} from './category/list/category-list-item.component';
import {CategoryEditComponent} from './category/list/category-edit.component';
import {CategoryListComponent} from './category/list/category-list.component';
import {AppRoutingModule} from './app-routing.module';
import {ProductListComponent} from './product/list/product-list.component';
import {ProductEditComponent} from './product/list/product-edit.component';
import {ProductListItemComponent} from './product/list/product-list-item.component';
import {ReceiptService} from './receipt/receipt.service';
import {ReceiptAddComponent} from './receipt/add/receipt-add.component';
import {ReportCategoryComponent} from './report/category/report-category.component';
import {ReportCategoryItemComponent} from './report/category/report-category-item.component';

@NgModule({
  declarations: [
    SelectComponent,
    ReceiptAddComponent,
    CategoryListItemComponent,
    CategoryEditComponent,
    CategoryListComponent,
    ProductListComponent,
    ProductEditComponent,
    ProductListItemComponent,
    ReportCategoryComponent,
    ReportCategoryItemComponent,
    NavbarComponent,
    AppComponent,
    PanelComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    FormsModule
  ],
  providers: [
    ReceiptService
  ],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule {
}
