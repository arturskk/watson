import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AddReceiptComponent} from './receipt/add/AddReceiptComponent';

const routes: Routes = [
  {path: 'add/receipt', component: AddReceiptComponent},
  {path: '', redirectTo: '/add/receipt', pathMatch: 'full'}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
