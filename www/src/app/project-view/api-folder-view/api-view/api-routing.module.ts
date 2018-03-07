import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ApiViewComponent} from "./api-view.component";


const routes: Routes = [
  {
    path: ':id' ,
    component: ApiViewComponent
  },
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ApiRoutingModule {}
