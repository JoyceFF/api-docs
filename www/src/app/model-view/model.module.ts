import {NgModule} from '@angular/core';
import {ModelViewComponent} from "./model-view.component";
import {ModelService} from "./model.service";
import {ModelRoutingModule} from "./model-routing.module";
import {SharedModule} from "../shared/shared.module";
import {ModalModule} from "ngx-bootstrap";
import {AddModelComponent} from "./add-model/add-model.component";
import {UpModelComponent} from "./up-model/up-model.component";
import {ModelDetailsComponent} from "./model-details/model-details.component";
import { MarkdownImportDetailsComponent } from './markdown-import-details/markdown-import-details.component';

@NgModule({
  imports: [
    SharedModule,
    ModalModule.forRoot(),
    ModelRoutingModule
  ],
  declarations: [
    ModelViewComponent,
    AddModelComponent,
    UpModelComponent,
    ModelDetailsComponent,
    MarkdownImportDetailsComponent
  ],
  providers: [
    ModelService
  ],
  entryComponents: [
    AddModelComponent,
    UpModelComponent,
    ModelDetailsComponent,
    MarkdownImportDetailsComponent
  ]
})
export class ModelModule {
}
