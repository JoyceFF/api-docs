import {NgModule} from '@angular/core';
import {SharedModule} from "../shared/shared.module";
import {SettingsViewComponent} from "./settings-view.component";
import {SettingsRoutingModule} from "./settings-routing.module";
import {SortableModule, TabsModule} from "ngx-bootstrap";
import {SettingsService} from "./settings.service";


@NgModule({
  imports: [
    SharedModule,
    TabsModule.forRoot(),
    SortableModule.forRoot(),
    SettingsRoutingModule
  ],
  declarations: [
    SettingsViewComponent
  ],
  providers: [
    SettingsService
  ],
  entryComponents: [
  ]
})
export class SettingsModule {
}
