import { Component, OnInit } from '@angular/core';
import {DocsApi} from "../model/docs-api";
import {BsModalRef} from "ngx-bootstrap";
import {NgForm} from "@angular/forms";
import {mapToObject} from "../../../../shared/app.common";
import {DocsHeader} from "../model/docs-header";

@Component({
  selector: 'app-add-header',
  templateUrl: './add-header.component.html',
  styleUrls: ['./add-header.component.css']
})
export class AddHeaderComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef) { }

  ngOnInit() {
  }

  api:DocsApi;

  saveHeader(form: NgForm) {
    let header:DocsHeader = mapToObject(form.value,new DocsHeader());
    if (this.api.headers == null){
      this.api.headers = [];
    }
    this.api.headers.push(header);
    this.bsModalRef.hide();
  }
}
