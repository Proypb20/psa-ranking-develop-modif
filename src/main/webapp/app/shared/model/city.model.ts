export interface ICity {
  id?: number;
  name?: string;
  latitude?: string;
  longitude?: string;
  provinceId?: number;
}

export class City implements ICity {
  constructor(public id?: number, public name?: string, public latitude?: string, public longitude?: string, public provinceId?: number) {}
}
