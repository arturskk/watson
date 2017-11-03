import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {WatsonApp} from './WatsonApp';
import {AppRoutingModule} from './AppRoutingModule';
import {ReceiptService} from './receipt/ReceiptService';
import {HttpClientModule} from '@angular/common/http';


@NgModule({
  declarations: [
    WatsonApp
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule
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
