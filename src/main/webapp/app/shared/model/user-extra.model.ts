import { Moment } from 'moment';
import { IAddress } from 'app/shared/model/address.model';

export interface IUserExtra {
  id?: number;
  numDoc?: string;
  phone?: string;
  bornDate?: Moment;
  userId?: number;
  addresses?: IAddress[];
  docTypeId?: number;
}

export class UserExtra implements IUserExtra {
  constructor(
    public id?: number,
    public numDoc?: string,
    public phone?: string,
    public bornDate?: Moment,
    public userId?: number,
    public addresses?: IAddress[],
    public docTypeId?: number
  ) {}
}
