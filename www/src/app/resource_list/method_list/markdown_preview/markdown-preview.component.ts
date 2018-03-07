import {MethodDetail} from "../../model/method-detail";
import {Component, OnInit} from "@angular/core";
import {BsModalRef} from "ngx-bootstrap";
import {DocsService} from "../../../docs.service";

@Component({
  //selector: 'modal-content',
  templateUrl : './markdown-preview.component.html'
})
export class MarkdownPreviewComponent implements OnInit{
  markdown:string;

  ngOnInit(): void {

  }

  constructor(private bsModalRef: BsModalRef,private docsService: DocsService){}

  //tables = require('raw-loader!/Users/jiaweiwei/maxwondocs/doc_api/b2b2c/mall/mall.md');
}
