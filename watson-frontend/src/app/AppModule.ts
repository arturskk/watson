import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './AppRoutingModule';
import {CategoryListComponent} from './category/list/CategoryListComponent';
import {CategoryListItemComponent} from './category/list/CategoryListItemComponent';
import {EditCategoryComponent} from './category/list/EditCategoryComponent';
import {EditProductComponent} from './product/list/EditProductComponent';
import {ProductListComponent} from './product/list/ProductListComponent';
import {ProductListItemComponent} from './product/list/ProductListItemComponent';
import {AddReceiptComponent} from './receipt/add/AddReceiptComponent';
import {ReceiptService} from './receipt/ReceiptService';
import {ReportCategoryComponent} from './report/category/ReportCategoryComponent';
import {ReportCategoryItemComponent} from './report/category/ReportCategoryItemComponent';
import {SelectComponent} from './select/SelectComponent';
import {WatsonAppComponent} from './WatsonAppComponent';

@NgModule({
  declarations: [
    SelectComponent,
    AddReceiptComponent,
    CategoryListItemComponent,
    EditCategoryComponent,
    CategoryListComponent,
    ProductListComponent,
    EditProductComponent,
    ProductListItemComponent,
    ReportCategoryComponent,
    ReportCategoryItemComponent,
    WatsonAppComponent
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
    WatsonAppComponent
  ]
})
export class AppModule {
}
