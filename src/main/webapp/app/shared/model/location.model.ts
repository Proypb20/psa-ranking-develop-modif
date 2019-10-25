import { ICity } from 'app/shared/model/city.model';

export interface ILocation {
  id?: number;
  name?: string;
  provinceId?: number;
  cities?: ICity[];
}

export class Location implements ILocation {
  constructor(public id?: number, public name?: string, public provinceId?: number, public cities?: ICity[]) {}
}
