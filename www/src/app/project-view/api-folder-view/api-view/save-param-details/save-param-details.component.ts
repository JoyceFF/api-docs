import {Component, OnInit} from '@angular/core';
import {BsModalRef} from "ngx-bootstrap";
import {ApiService} from "../api.service";

@Component({
  selector: 'app-save-param-details',
  templateUrl: './save-param-details.component.html',
  styleUrls: ['./save-param-details.component.css']
})
export class SaveParamDetailsComponent implements OnInit {

  constructor(private bsModalRef: BsModalRef, private apiService: ApiService) {
  }

  folderId: string;
  msg: Map<string, string>[] = [];
  interval: any;

  ngOnInit() {
    this.interval = setInterval(() => {
      this.loaDetails();
    }, 100)
  }

  alerts: any = [];

  loaDetails(): void {
    this.apiService.getSaveApiDetails(this.folderId).then(msg => {
      if (msg.indexOf("修改") > -1) {
        let map: Map<string, string> = new Map();
        map['type'] = 'info';
        map['msg'] = msg;
        this.msg.push(map);
      } else if (msg.indexOf("添加") > -1) {
        let map: Map<string, string> = new Map();
        map['type'] = 'success';
        map['msg'] = msg;
        this.msg.push(map);
      } else if (msg.indexOf("删除") > -1) {
        let map: Map<string, string> = new Map();
        map['type'] = 'delete';
        map['msg'] = msg;
        this.msg.push(map);
      } else {
        clearInterval(this.interval);
      }
    })


  }

}
