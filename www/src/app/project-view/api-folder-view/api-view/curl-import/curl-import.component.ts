import {Component, ElementRef, OnInit} from '@angular/core';
import {DocsApi} from "../model/docs-api";
import {BsModalRef} from "ngx-bootstrap";
import {NgForm} from "@angular/forms";

import * as ace from 'brace';
import 'brace/mode/sh';
import 'brace/theme/terminal';
import 'brace/keybinding/vim';
import {ApiService} from "../api.service";

@Component({
  selector: 'app-curl-import',
  templateUrl: './curl-import.component.html',
  styleUrls: ['./curl-import.component.css']
})
export class CurlImportComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef, private el: ElementRef,private apiService:ApiService) { }

  ngOnInit() {
    this.editor = ace.edit('javascript-editor');//dom 元素 根据Id选择
    this.editor.getSession().setMode('ace/mode/sh');//语言
    this.editor.setTheme('ace/theme/terminal');//主题
    this.editor.setKeyboardHandler('ace/keyboard/vim');//
    this.editor.setOption("wrap", "free");//自动换行
    this.editor.clearSelection();
  }

  editor:any;
  api:DocsApi;
  curlHtml:string;

  saveCurl(form: NgForm){
    let curl:string = this.editor.getValue();
    this.apiService.checkCurl(curl).then(()=>{
      this.api.curl = curl;
      this.bsModalRef.hide();
    });
  }
}
