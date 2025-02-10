import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { HeaderComponent } from './header/header.component';
import { TableComponent } from './table/table.component';
import { SecondpComponent } from './secondp/secondp.component';


const routes:
Routes = [

  { path: 'header', component: HeaderComponent },
  { path: '', component: HeaderComponent },
  { path: 'table', component: TableComponent },
  //{ path: '**', component: HeaderComponent },

  { path: 'second', component: SecondpComponent },



];



@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}





