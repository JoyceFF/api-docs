import { Component, OnInit } from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {ApiService} from "../api.service";
import {DocsApi} from "../model/docs-api";

@Component({
  selector: 'app-add-api',
  templateUrl: './add-api.component.html',
  styleUrls: ['./add-api.component.css']
})
export class AddApiComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef,private apiService:ApiService) { }

  ngOnInit() {
  }

  api:DocsApi = new DocsApi();
  allApi:DocsApi[] = [];
  folderId:string;

  saveApi(){
    this.api["folderId"] = this.folderId;
    this.allApi.push(this.api);
    this.bsModalRef.hide();
  }

}
