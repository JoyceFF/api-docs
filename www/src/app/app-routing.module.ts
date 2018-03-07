import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {NotFoundComponent} from "./shared/not-found.component";
import {SelectivePreloadingStrategy} from "./selective-preloading-strategy";


const routes: Routes = [
  {
    path: 'project-view',
    loadChildren: './project-view/project.module#ProjectModule'
  },{
    path: 'model-view',
    loadChildren: './model-view/model.module#ModelModule'
  },{
    path: 'settings-view',
    loadChildren: './settings-view/settings.module#SettingsModule'
  },
  {
    path: '',
    redirectTo: '/project-view',
    pathMatch: 'full'
  },
  {
    path: '**',
    component: NotFoundComponent
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {preloadingStrategy: SelectivePreloadingStrategy,useHash:true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
