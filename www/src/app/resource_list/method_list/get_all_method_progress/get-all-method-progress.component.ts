import {Component, OnInit} from "@angular/core";
import {BsModalRef} from "ngx-bootstrap";
import {DocsService} from "../../../docs.service";

@Component({
  //selector: 'modal-content',
  templateUrl : './get-all-method-progress.component.html'
})
export class GetAllMethodProgressComponent implements OnInit{
  progress:number = 0;
  uuid:string;
  interval:any;


  ngOnInit(): void {
    this.interval = setInterval(()=>{
      this.loadProgress();
    },100)
  }

  constructor(private bsModalRef: BsModalRef,private docsService: DocsService){
  }

  loadProgress(){
   this.docsService.getAllMethodDetailProgress(this.uuid)
     .then(progress => this.progress = progress);
   if (this.progress == 100){
     clearInterval(this.interval);
     this.bsModalRef.hide();
   }
  }



  //tables = require('raw-loader!/Users/jiaweiwei/maxwondocs/doc_api/b2b2c/mall/mall.md');
}
