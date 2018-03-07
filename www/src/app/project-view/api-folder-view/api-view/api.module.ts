import { NgModule } from '@angular/core';
import {ApiViewComponent} from "./api-view.component";
import { AddApiComponent } from './add-api/add-api.component';
import {ApiRoutingModule} from "./api-routing.module";
import {ApiService} from "./api.service";
import {SharedModule} from "../../../shared/shared.module";
import {AlertModule, ModalModule, TooltipModule, TypeaheadModule} from "ngx-bootstrap";
import { AddParamComponent } from './add-param/add-param.component';
import { SaveParamDetailsComponent } from './save-param-details/save-param-details.component';
import { ParamJsonImportComponent } from './param-json-import/param-json-import.component';
import {JsonModule} from "../../../json-view/json.module";
import { AddHeaderComponent } from './add-header/add-header.component';
import { CurlImportComponent } from './curl-import/curl-import.component';
import {PreModule} from "../../../pre-view/pre.module";
import {SelectModelComponent} from "./select_model/select-model.component";
import {ModelService} from "../../../model-view/model.service";
import { PreviewMarkdownComponent } from './preview-markdown/preview-markdown.component';
import {MarkdownToHtmlModule} from "ng2-markdown-to-html";
import {SettingsService} from "../../../settings-view/settings.service";

@NgModule({
  imports: [
    SharedModule,
    ModalModule.forRoot(),
    TypeaheadModule.forRoot(),
    TooltipModule.forRoot(),
    AlertModule.forRoot(),
    MarkdownToHtmlModule.forRoot(),
    JsonModule,
    PreModule,
    ApiRoutingModule,
  ],
  declarations: [
    ApiViewComponent,
    AddApiComponent,
    AddParamComponent,
    SaveParamDetailsComponent,
    ParamJsonImportComponent,
    AddHeaderComponent,
    CurlImportComponent,
    SelectModelComponent,
    PreviewMarkdownComponent
  ],
  providers:[
    ApiService,
    ModelService,
    SettingsService
  ],
  entryComponents: [
    AddApiComponent,
    AddParamComponent,
    AddHeaderComponent,
    CurlImportComponent,
    SaveParamDetailsComponent,
    ParamJsonImportComponent,
    SelectModelComponent,
    PreviewMarkdownComponent
  ]
})
export class ApiModule { }
