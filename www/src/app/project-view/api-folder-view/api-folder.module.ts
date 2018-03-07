import {NgModule, OnInit} from "@angular/core";
import {ModalModule} from "ngx-bootstrap";
import {SharedModule} from "../../shared/shared.module";
import {ApiFolderService} from "./api-folder.service";
import {ApiFolderComponent} from "./api-folder.component";
import {ApiFolderRoutingModule} from "./api-folder-routing.module";
import { AddApiFolderComponent } from './add-api-folder/add-api-folder.component';
import { UpApiFolderComponent } from './up-api-folder/up-api-folder.component';

@NgModule({
  imports: [
    SharedModule,
    ModalModule.forRoot(),
    ApiFolderRoutingModule,
  ],
  declarations: [
    ApiFolderComponent,
    AddApiFolderComponent,
    UpApiFolderComponent
  ],
  exports: [
  ],
  providers: [
    ApiFolderService
  ],
  entryComponents: [
    AddApiFolderComponent,
    UpApiFolderComponent
  ]
})

export class ApiFolderModule{
}
