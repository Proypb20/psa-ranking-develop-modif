export interface IDocType {
  id?: number;
  name?: string;
  description?: string;
}

export class DocType implements IDocType {
  constructor(public id?: number, public name?: string, public description?: string) {}
}
