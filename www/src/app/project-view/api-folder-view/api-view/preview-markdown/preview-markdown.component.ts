import { Component, OnInit } from '@angular/core';
import {BsModalRef, tr} from "ngx-bootstrap";
import {ApiService} from "../api.service";
import {DocsApi} from "../model/docs-api";
import {host, url} from "../../../../shared/app.common";

@Component({
  selector: 'app-preview-markdown',
  templateUrl: './preview-markdown.component.html',
  styleUrls: ['./preview-markdown.component.css']
})
export class PreviewMarkdownComponent implements OnInit {

  markdown:string;

  ngOnInit(): void {

  }

  constructor(private bsModalRef: BsModalRef,private apiService:ApiService){}

  apis:DocsApi[];
  fileName:string;
  generateStatus:boolean = false;

  download(){
    this.generateStatus = true;
    this.apiService.generateMarkdown(this.markdown).then(filename => {
      this.fileName = filename;
      this.generateStatus = false;
    });
  }
}
