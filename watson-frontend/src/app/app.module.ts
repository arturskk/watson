import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {NavbarComponent} from './navbar/navbar.component';
import {AppComponent} from './app.component';
import {PanelComponent} from './widgets/panel/panel.component';
import {SelectComponent} from './widgets/select/select.component';
import {CategoryEditComponent} from './category/list/category-edit.component';
import {CategoryListComponent} from './category/list/category-list.component';
import {AppRoutingModule} from './app-routing.module';
import {ProductListComponent} from './product/list/product-list.component';
import {ProductEditComponent} from './product/edit/product-edit.component';
import {ReceiptService} from './receipt/receipt.service';
import {ReceiptAddComponent} from './receipt/add/receipt-add.component';
import {ReportCategoryComponent} from './report/category/report-category.component';
import {ReportCategoryItemComponent} from './report/category/report-category-item.component';
import { ButtonComponent } from './widgets/button/button.component';
import { ButtonFlatComponent } from './widgets/button-flat/button-flat.component';
import {CrudListComponent} from './widgets/crud-list/crud-list.component';
import {CrudItemComponent} from './widgets/crud-list/crud-item.component';

@NgModule({
  declarations: [
    SelectComponent,
    ReceiptAddComponent,
    CategoryEditComponent,
    CategoryListComponent,
    ProductListComponent,
    ProductEditComponent,
    ReportCategoryComponent,
    ReportCategoryItemComponent,
    NavbarComponent,
    AppComponent,
    PanelComponent,
    ButtonComponent,
    ButtonFlatComponent,
    CrudListComponent,
    CrudItemComponent
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
