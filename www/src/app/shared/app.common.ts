import {RequestOptionsArgs} from "@angular/http";
import {DocsParam} from "../project-view/api-folder-view/api-view/model/docs-param";

export const headers = new Headers({'Content-Type': 'application/json', 'Accept': 'application/json'});
export const requestOptions: RequestOptionsArgs = ({headers: this.headers});

export const host: string = "http://0.0.0.0:5555";
export const baseUrl: string = host + "/maxwon_docs";

export const url = (url: string) => {
  return baseUrl + url;
}

export const handleError = (error: any) => {
  let msg: any;
  if (error._body != null) {
    msg = error._body;
  } else {
    msg = error.statusText;
  }
  console.error(error)
  alertError(msg);
  return Promise.reject(error.message || error);
}

export const mapToObject = (map: Map<string, object>, object: any) => {
  for (let obj in map) {
    object[obj] = map[obj];
  }
  return object;
}

export const ParamType = [
  'String',
  'Number',
  'Boolean',
  'Array',
  'Object'
]

export const ParamTypeMap = {
  'String': 'String',
  'Number': 'Number',
  'Boolean': 'Boolean',
  'Array': 'Array',
  'Object': 'Object'
}

export const ParamTypeDefaultValueMap = {
  'String': "",
  'Number': "0",
  'Boolean': "false",
  'Array': "[]",
  'Object': "{}"
}

export const sortParams = (params: DocsParam[]) => {
  if (params != null) {
    let sortParams: DocsParam[] = [];
    sort(params, params, sortParams);
    return sortParams;
  }
}

export const sort = (params: DocsParam[], childNodes: DocsParam[], result: DocsParam[]) => {
  for (let obj of childNodes) {
    if (!result.includes(obj)) {
      result.push(obj);
      let childNodes = findChild(params, obj);
      sort(params, childNodes, result);
    }
  }
}

export const findChild = (params: DocsParam[], obj: DocsParam) => {
  let childNodes: DocsParam[] = [];
  for (let obj1 of params) {
    if (obj1.parentId == obj.id) {
      childNodes.push(obj1);
    }
  }
  return childNodes;
}

export const docsTypeof = (obj: any) => {
  if (obj instanceof Array) {
    for (let val of obj) {
      let type: string = typeof val;
      if (type == 'object') {
        return 'Array<object>';
      }
    }
    return 'Array';
  } else {
    let type = typeof obj;
    return type.replace(/( |^)[a-z]/g, (L) => L.toUpperCase());
  }
}

export const alerts = [];

export const addAlerts = (type: string, timeout: number, msg: string) => {
  alerts.push({
    type: type,
    msg: msg,
    timeout: timeout
  });
}

export const alertError = (msg: string) => {
  alerts.push({
    type: 'error',
    msg: msg,
    timeout: 2000,
    dismissible: 'dismissible'
  });
}

export const alertSuccess = (msg: string) => {
  alerts.push({
    type: 'success',
    msg: msg,
    timeout: 2000,
    dismissible: 'dismissible'
  });
}


export const uuid = () => {
  let s = [];
  let hexDigits = "0123456789abcdef";
  for (var i = 0; i < 36; i++) {
    s[i] = hexDigits.substr(Math.floor(Math.random() * 0x10), 1);
  }
  s[14] = "4";  // bits 12-15 of the time_hi_and_version field to 0010
  s[19] = hexDigits.substr((s[19] & 0x3) | 0x8, 1);  // bits 6-7 of the clock_seq_hi_and_reserved to 01
  s[8] = s[13] = s[18] = s[23] = "-";

  let uuid = s.join("");
  return uuid;
}


export const type = (type: string) => {
  type = type.toLowerCase();

  let number = [
    'int',
    'number',
    'double',
    'short',
    'long',
    'float'
  ]

  let string = [
    'string',
    'char',
    'text',
  ]

  let boolean = [
    'boolean',
    'bool'
  ]

  let array = [
    'array',
    'list'
  ]

  let object = [
    'object'
  ]

  for (let obj of number) {
     if (type.indexOf(obj) == 0){
       return 'Number'
     }
  }


  for (let obj of string) {
    if (type.indexOf(obj) == 0){
      return 'String'
    }
  }

  for (let obj of boolean) {
    if (type.indexOf(obj) == 0){
      return 'Boolean'
    }
  }

  for (let obj of array) {
    if (type.indexOf(obj) == 0){
      return 'Array'
    }
  }

  return 'Object'
}
