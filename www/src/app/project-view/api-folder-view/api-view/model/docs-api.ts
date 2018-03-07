import {DocsParam} from "./docs-param";
import {DocsHeader} from "./docs-header";
import {DocsPathParam} from "./docs-path-param";

export class DocsApi {
  id: string;
  folderId: string;
  name: string;
  describe: string;
  http: string;
  url: string;
  permission:string;
  pathParams: DocsPathParam[] = [];
  params: DocsParam[] = [];
  headers: DocsHeader[] = [];
  body: DocsParam[] = [];
  result: DocsParam[] = [];
  curl: string;
}
