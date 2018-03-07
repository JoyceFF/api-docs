import { Component } from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {ApiFolderService} from "../api-folder.service";
import {DocsApiFolder} from "../model/docs-api-folder";

@Component({
  selector: 'app-add-api-folder',
  templateUrl: './add-api-folder.component.html',
  styleUrls: ['./add-api-folder.component.css']
})
export class AddApiFolderComponent{

  constructor(private bsModalRef: BsModalRef,protected apiFolderService:ApiFolderService) { }

  apiFolder:DocsApiFolder = new DocsApiFolder();
  projectId:string;

  saveApiFolder(){
    this.apiFolder["projectId"] = this.projectId;
    this.apiFolderService.addApiFolder(this.apiFolder).then();
    this.bsModalRef.hide();
  }


}
