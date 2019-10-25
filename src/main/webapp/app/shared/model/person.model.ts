import { Moment } from 'moment';

export interface IPerson {
  id?: number;
  names?: string;
  surnames?: string;
  email?: string;
  phone?: string;
  bornDate?: Moment;
  psaId?: string;
  eraseDate?: Moment;
  active?: boolean;
  createDate?: Moment;
  updatedDate?: Moment;
  addressId?: number;
  docTypeId?: number;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public names?: string,
    public surnames?: string,
    public email?: string,
    public phone?: string,
    public bornDate?: Moment,
    public psaId?: string,
    public eraseDate?: Moment,
    public active?: boolean,
    public createDate?: Moment,
    public updatedDate?: Moment,
    public addressId?: number,
    public docTypeId?: number
  ) {
    this.active = this.active || false;
  }
}
