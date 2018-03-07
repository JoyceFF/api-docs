import {Component, OnInit} from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {NgForm} from "@angular/forms";
import {DocsApi} from "../model/docs-api";
import {mapToObject, ParamType, sortParams} from "../../../../shared/app.common";
import {DocsParam} from "../model/docs-param";
import {ApiService} from "../api.service";

@Component({
  selector: 'app-add-param',
  templateUrl: './add-param.component.html',
  styleUrls: ['./add-param.component.css']
})
export class AddParamComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef) {
  }

  ngOnInit() {
    this.param.type = 'String';
    this.param.required = '是';
  }

  api: DocsApi;
  type: string;
  parentId: string;
  paramType = ParamType;
  param:DocsParam = new DocsParam();

  saveParam(form: NgForm) {
    let param = mapToObject(form.value, new DocsParam());
    if (this.type == 'param') {
      if (this.api.params == null) {
        this.api.params = [];
      }
      let level: number = 1;
      if (this.parentId != null) {
        for (let p of this.api.params) {
          if (p.id == this.parentId) {
            if (p.level != null) {
              level = p.level;
            }
          }
        }
        level = level + 1;
        param.parentId = this.parentId;
      }
      param.level = level;
      this.api.params.push(param);
      this.api.params = sortParams(this.api.params);
    } else if (this.type == 'body') {
      if (this.api.body == null) {
        this.api.body = [];
      }
      let level: number = 1;
      if (this.parentId != null) {
        for (let b of this.api.body) {
          if (b.id == this.parentId) {
            if (b.level != null) {
              level = b.level;
            }
          }
        }
        level = level + 1;
        param.parentId = this.parentId;
      }
      param.level = level;
      this.api.body.push(param);
      this.api.body = sortParams(this.api.body);
    }else if (this.type == 'result') {
      if (this.api.result == null) {
        this.api.result = [];
      }
      let level: number = 1;
      if (this.parentId != null) {
        for (let b of this.api.result) {
          if (b.id == this.parentId) {
            if (b.level != null) {
              level = b.level;
            }
          }
        }
        level = level + 1;
        param.parentId = this.parentId;
      }
      param.level = level;
      this.api.result.push(param);
      this.api.result = sortParams(this.api.result);
    }
    this.bsModalRef.hide();
  }
}
