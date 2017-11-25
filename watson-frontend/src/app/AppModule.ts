import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {AppRoutingModule} from './AppRoutingModule';
import {CategoryListComponent} from './category/list/CategoryListComponent';
import {ProductListComponent} from './product/list/ProductListComponent';
import {ReceiptService} from './receipt/ReceiptService';
import {HttpClientModule} from '@angular/common/http';
import {SelectComponent} from './select/SelectComponent';
import {FormsModule} from '@angular/forms';
import {AddReceiptComponent} from './receipt/add/AddReceiptComponent';
import {WatsonAppComponent} from './WatsonAppComponent';
import {EditProductComponent} from './product/list/EditProductComponent';
import {ProductListItemComponent} from './product/list/ProductListItemComponent';

@NgModule({
  declarations: [
    SelectComponent,
    AddReceiptComponent,
    CategoryListComponent,
    ProductListComponent,
    EditProductComponent,
    ProductListItemComponent,
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
