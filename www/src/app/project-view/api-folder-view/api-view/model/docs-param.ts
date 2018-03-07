import {uuid} from "../../../../shared/app.common";

export class DocsParam{
  id:string;
  name:string;
  required:string;
  type:string;
  describe:string;
  defaultValue:string;
  parentId:string;
  level:number;
  constructor(){
    this.id = uuid();
  }
}
