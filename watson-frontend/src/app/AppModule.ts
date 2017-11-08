import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {WatsonApp} from './WatsonApp';
import {AppRoutingModule} from './AppRoutingModule';
import {ReceiptService} from './receipt/ReceiptService';
import {HttpClientModule} from '@angular/common/http';
import {SelectComponent} from './select/SelectComponent';
import {FormsModule} from '@angular/forms';

@NgModule({
  declarations: [
    SelectComponent,
    WatsonApp
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
    WatsonApp
  ]
})
export class AppModule {
}
