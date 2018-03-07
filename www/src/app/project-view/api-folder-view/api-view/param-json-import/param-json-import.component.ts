import {Component, OnInit, ViewChild} from '@angular/core';
import {DocsApi} from "../model/docs-api";
import {BsModalRef} from "ngx-bootstrap";
import {JsonEditorOptions, JsonViewComponent} from "../../../../json-view/json-view.component";
import {DocsParam} from "../model/docs-param";
import {docsTypeof} from "../../../../shared/app.common";
import {stringify} from "querystring";

@Component({
  selector: 'app-param-json-import',
  templateUrl: './param-json-import.component.html',
  styleUrls: ['./param-json-import.component.css']
})
export class ParamJsonImportComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef) {
    this.editorOptions = new JsonEditorOptions();
    this.editorOptions.mode = 'code';
  //  this.editorOptions.theme = "ace/theme/terminal";
  }

  ngOnInit() {
  }

  api: DocsApi;
  type: string;

  public editorOptions: JsonEditorOptions;
  @ViewChild(JsonViewComponent) editor: JsonViewComponent;

  importParam(){
    console.log(this.editor.getText());
    let json = JSON.parse(this.editor.getText());
    let params:DocsParam[] = [];

    this.forParam(params,json);
    if (this.type == 'param'){
      if (this.api.params == null){
        this.api.params = [];
      }
      for (let obj of params) {
        if (!this.isIncludes(this.api.params,obj)){
          this.api.params.push(obj);
        }
      }
    }else if (this.type == 'body'){
      if (this.api.body == null){
        this.api.body = [];
      }
      for (let obj of params) {
        if (!this.isIncludes(this.api.body,obj)){
          this.api.body.push(obj);
        }
      }
    }else if (this.type == 'result'){
      if (this.api.result == null){
        this.api.result = [];
      }
      for (let obj of params) {
        if (!this.isIncludes(this.api.result,obj)){
          this.api.result.push(obj);
        }
      }
    }
    this.bsModalRef.hide();
  }

  isIncludes(params:DocsParam[],obj:DocsParam){
    // for (let obj1 of params) {
    //   if (obj1.level == 1 && obj.level ==1 && obj1.name == obj.name){
    //     return true;
    //   }
    // }
    return false;
  }

  forParam(params:DocsParam[],json:any,parentId?:any,level?:any){
    if (level == null){
      level = 1;
    }else{
      level = level + 1;
    }
    let type = docsTypeof(json);

    if (type == 'Array<object>'){
      let keys:string[] = [];
      for (let obj of json[0]) {
        for (let key in obj){
          keys.push(key);
          let val = obj[key];

          let param:DocsParam = new DocsParam();
          param.name = key;
          param.required = '是';
          param.level = level;
          param.parentId = parentId;
          param.describe = '';

          let type2 = docsTypeof(val);

          if (type2 == 'Array<object>'){
            param.type = 'Array';
            params.push(param);
            this.forParam(params,val,param.id,level);
          }else if (type2 == 'Object'){
            param.type = 'Object';
            params.push(param);
            this.forParam(params,val,param.id,level);
          }else{
            param.defaultValue = String(val);
            param.type = type2;
            params.push(param);
          }
        }
      }

      for (let obj of json){
        for (let key in obj){
           if (!keys.includes(key)){
             keys.push(key);
             let val = obj[key];

             let param:DocsParam = new DocsParam();
             param.name = key;
             param.required = '是';
             param.level = level;
             param.parentId = parentId;
             param.describe = '';

             let type2 = docsTypeof(val);

             if (type2 == 'Array<object>'){
               param.type = 'Array';
               params.push(param);
               this.forParam(params,val,param.id,level);
             }else if (type2 == 'Object'){
               param.type = 'Object';
               params.push(param);
               this.forParam(params,val,param.id,level);
             }else{
               param.defaultValue = String(val);
               param.type = type2;
               params.push(param);
             }
           }
        }
      }

    }else if (type == 'Object'){
      for (let key in json){
        let val = json[key];

        let param:DocsParam = new DocsParam();
        param.name = key;
        param.required = '是';
        param.level = level;
        param.parentId = parentId;
        param.describe = '';

        let type2 = docsTypeof(val);

        if (type2 == 'Array<object>'){
          param.type = 'Array';
          params.push(param);
          this.forParam(params,val,param.id,level);
        }else if (type2 == 'Object'){
          param.type = 'Object';
          params.push(param);
          this.forParam(params,val,param.id,level);
        }else{
          param.defaultValue = String(val);
          param.type = type2;
          params.push(param);
        }
      }
   }

  }


}
