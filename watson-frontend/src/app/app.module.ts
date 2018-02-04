import {HttpClientModule} from '@angular/common/http';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {BrowserModule} from '@angular/platform-browser';
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {CategoryEditComponent} from './category/edit/category-edit.component';
import {CategoryListComponent} from './category/list/category-list.component';
import {NavbarComponent} from './navbar/navbar.component';
import {ProductEditComponent} from './product/edit/product-edit.component';
import {ProductListComponent} from './product/list/product-list.component';
import {AddReceiptDetailsComponent} from './receipt/add/add-receipt-details/add-receipt-details.component';
import {AddReceiptItemAmountComponent} from './receipt/add/add-receipt-item-row/add-receipt-item-amount/add-receipt-item-amount.component';
import {AddReceiptItemCostComponent} from './receipt/add/add-receipt-item-row/add-receipt-item-cost/add-receipt-item-cost.component';
import {AddReceiptItemProductComponent} from './receipt/add/add-receipt-item-row/add-receipt-item-product/add-receipt-item-product.component';
import {AddReceiptItemsSummaryComponent} from './receipt/add/add-receipt-items-summary/add-receipt-items-summary.component';
import {ReceiptAddComponent} from './receipt/add/receipt-add.component';
import {ReceiptService} from './receipt/receipt.service';
import {ReportCategoryItemComponent} from './report/category/report-category-item.component';
import {ReportCategoryComponent} from './report/category/report-category.component';
import {ButtonFlatComponent} from './widgets/button-flat/button-flat.component';
import {ButtonComponent} from './widgets/button/button.component';
import {CrudItemComponent} from './widgets/crud-list/crud-item.component';
import {CrudListComponent} from './widgets/crud-list/crud-list.component';
import {JoinArrayPipe} from './widgets/join-array.pipe';
import {PanelComponent} from './widgets/panel/panel.component';
import {SelectComponent} from './widgets/select/select.component';
import {SpinnerComponent} from './widgets/spinner/spinner.component';

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
    CrudItemComponent,
    JoinArrayPipe,
    SpinnerComponent,
    AddReceiptDetailsComponent,
    AddReceiptItemsSummaryComponent,
    AddReceiptItemAmountComponent,
    AddReceiptItemCostComponent,
    AddReceiptItemProductComponent
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
