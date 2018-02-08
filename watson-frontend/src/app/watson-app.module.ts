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
import {ProductPriceReportCategoryTreeComponent} from './report/productprice/category-tree/product-price-report-category-tree.component';
import {ProductPriceReportComponent} from './report/productprice/product-price-report.component';
import {ProductPriceReportPanelComponent} from './report/productprice/report-panel/product-price-report-panel.component';
import {SafeHtmlPipe} from './util/safe-html.pipe';
import {AppVersionComponent} from './version/app-version.component';
import {ButtonFlatComponent} from './widgets/button-flat/button-flat.component';
import {ButtonComponent} from './widgets/button/button.component';
import {CrudItemComponent} from './widgets/crud-list/crud-item.component';
import {CrudListComponent} from './widgets/crud-list/crud-list.component';
import {JoinArrayPipe} from './widgets/join-array.pipe';
import {PanelComponent} from './widgets/panel/panel.component';
import {SelectComponent} from './widgets/select/select.component';
import {SpinnerComponent} from './widgets/spinner/spinner.component';
import { ProductPriceCategoryTreeItemComponent } from './report/productprice/category-tree/product-price-category-tree-item/product-price-category-tree-item.component';
import { ProductPriceReportPanelDataTableComponent } from './report/productprice/report-panel/product-price-report-panel-data-table/product-price-report-panel-data-table.component';
import { ProductPriceReportPanelFilterComponent } from './report/productprice/report-panel/product-price-report-panel-filter/product-price-report-panel-filter.component';
import { ProductPriceReportPanelOptionsComponent } from './report/productprice/report-panel/product-price-report-panel-options/product-price-report-panel-options.component';
import { ProductPriceReportPanelSummaryComponent } from './report/productprice/report-panel/product-price-report-panel-summary/product-price-report-panel-summary.component';

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
    AddReceiptItemProductComponent,
    AppVersionComponent,
    SafeHtmlPipe,
    ProductPriceReportComponent,
    ProductPriceReportCategoryTreeComponent,
    ProductPriceReportPanelComponent,
    ProductPriceCategoryTreeItemComponent,
    ProductPriceReportPanelDataTableComponent,
    ProductPriceReportPanelFilterComponent,
    ProductPriceReportPanelOptionsComponent,
    ProductPriceReportPanelSummaryComponent
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
