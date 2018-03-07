import { Component, OnInit } from '@angular/core';
import {MaxwonResource} from "./model/resource";
import {DocsService} from "../docs.service";

@Component({
   selector: 'resource-list',
   templateUrl : './resource.list.component.html',
   styleUrls:[ './resource.list.component.css' ]
})

export class ResourceListComponent implements OnInit{
    resource: MaxwonResource[] = [];

    constructor(private docsService: DocsService){}

    ngOnInit(): void{
       this.docsService.getResource().then(resource =>{
         this.resource = resource;
       });
    }
}
