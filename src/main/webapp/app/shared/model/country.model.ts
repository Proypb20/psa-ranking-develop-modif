import { IProvince } from 'app/shared/model/province.model';

export interface ICountry {
  id?: number;
  name?: string;
  provinces?: IProvince[];
}

export class Country implements ICountry {
  constructor(public id?: number, public name?: string, public provinces?: IProvince[]) {}
}
