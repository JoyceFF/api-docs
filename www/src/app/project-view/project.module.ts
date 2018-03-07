import {NgModule, OnInit} from "@angular/core";
import {ProjectViewComponent} from "./project-view.component";
import {ProjectService} from "./project.service";
import {AddProjectComponent} from "./add-project-view/add-project.component";
import {SharedModule} from "../shared/shared.module";
import {ProjectRoutingModule} from "./project-routing.module";
import {UpProjectComponent} from "./up-project-view/up-project.component";
import {ModalModule} from "ngx-bootstrap";

@NgModule({
  imports: [
    SharedModule,
    ModalModule.forRoot(),
    ProjectRoutingModule,
  ],
  declarations: [
    ProjectViewComponent,
    AddProjectComponent,
    UpProjectComponent
  ],
  exports: [],
  providers: [
    ProjectService
  ],
  entryComponents: [
    AddProjectComponent,
    UpProjectComponent
  ]
})

export class ProjectModule{

}
