import { NgModule }             from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {ProjectViewComponent} from "./project-view.component";


const routes: Routes = [
  {
    path: '' ,
    component: ProjectViewComponent,
  },{
    path:'api-folder',
    loadChildren:'./api-folder-view/api-folder.module#ApiFolderModule'
  }
];

@NgModule({
  imports: [ RouterModule.forChild(routes) ],
  exports: [ RouterModule ]
})
export class ProjectRoutingModule {}
