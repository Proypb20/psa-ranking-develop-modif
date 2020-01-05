import { ICity } from 'app/shared/model/city.model';

export interface IProvince {
  id?: number;
  name?: string;
  countryId?: number;
  cities?: ICity[];
}

export class Province implements IProvince {
  constructor(public id?: number, public name?: string, public countryId?: number, public cities?: ICity[]) {}
}
