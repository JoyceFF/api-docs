import { Component, OnInit } from '@angular/core';
import {DocsApiFolder} from "../model/docs-api-folder";
import {BsModalRef} from "ngx-bootstrap";
import {ApiFolderService} from "../api-folder.service";
import {NgForm} from "@angular/forms";

@Component({
  selector: 'app-up-api-folder',
  templateUrl: './up-api-folder.component.html',
  styleUrls: ['./up-api-folder.component.css']
})
export class UpApiFolderComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef,protected apiFolderService:ApiFolderService) { }

  ngOnInit() {
  }

  apiFolder:DocsApiFolder = new DocsApiFolder();

  upApiFolder(form:NgForm){
     this.apiFolderService.updateApiFolder(this.apiFolder.id,form.value)
       .then(() => this.bsModalRef.hide());
  }
}
