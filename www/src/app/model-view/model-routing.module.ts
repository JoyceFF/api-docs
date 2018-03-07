import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ModelViewComponent} from "./model-view.component";


const routes: Routes = [
  {
    path: '' ,
    component: ModelViewComponent,
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ModelRoutingModule {}
