import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {WatsonApp} from './WatsonApp';
import {AppRoutingModule} from './AppRoutingModule';
import {ReceiptService} from './receipt/ReceiptService';


@NgModule({
  declarations: [
    WatsonApp
  ],
  imports: [
    BrowserModule,
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
