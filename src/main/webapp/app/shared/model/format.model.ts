export interface IFormat {
  id?: number;
  name?: string;
  description?: string;
  coeficient?: number;
  playersQty?: number;
}

export class Format implements IFormat {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public coeficient?: number,
    public playersQty?: number
  ) {}
}
