import {MethodDetail} from "../../model/method-detail";
import {Component, OnInit} from "@angular/core";
import {BsModalRef} from "ngx-bootstrap";
import {DocsService} from "../../../docs.service";

@Component({
  //selector: 'modal-content',
  templateUrl : './markdown-generate.component.html'
})
export class MarkdownGenerateComponent implements OnInit{
  fileName:string;

  ngOnInit(): void {

  }

  constructor(private bsModalRef: BsModalRef,private docsService: DocsService){}
}
