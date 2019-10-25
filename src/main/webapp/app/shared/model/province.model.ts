import { ILocation } from 'app/shared/model/location.model';

export interface IProvince {
  id?: number;
  name?: string;
  countryId?: number;
  locations?: ILocation[];
}

export class Province implements IProvince {
  constructor(public id?: number, public name?: string, public countryId?: number, public locations?: ILocation[]) {}
}
