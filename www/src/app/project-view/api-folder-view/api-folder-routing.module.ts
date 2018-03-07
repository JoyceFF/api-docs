import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ApiFolderComponent} from "./api-folder.component";


const routes: Routes = [
  {
    path: ':projectId' ,
    component: ApiFolderComponent
  },{
    path: 'api-view',
    loadChildren:'./api-view/api.module#ApiModule'
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ApiFolderRoutingModule {}
