import { Moment } from 'moment';
import { ICity } from 'app/shared/model/city.model';

export interface IUserExtra {
  id?: number;
  numDoc?: string;
  phone?: string;
  bornDate?: Moment;
  userId?: number;
  cities?: ICity[];
  docTypeId?: number;
}

export class UserExtra implements IUserExtra {
  constructor(
    public id?: number,
    public numDoc?: string,
    public phone?: string,
    public bornDate?: Moment,
    public userId?: number,
    public cities?: ICity[],
    public docTypeId?: number
  ) {}
}
