import { Moment } from 'moment';

export interface IUserExtra {
  id?: number;
  numDoc?: string;
  phone?: string;
  bornDate?: Moment;
  pictureContentType?: string;
  picture?: any;
  docTypeId?: number;
  userLogin?: string;
  userId?: number;
}

export class UserExtra implements IUserExtra {
  constructor(
    public id?: number,
    public numDoc?: string,
    public phone?: string,
    public bornDate?: Moment,
    public pictureContentType?: string,
    public picture?: any,
    public docTypeId?: number,
    public userLogin?: string,
    public userId?: number
  ) {}
}
