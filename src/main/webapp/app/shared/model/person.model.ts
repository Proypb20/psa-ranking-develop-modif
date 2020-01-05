import { Moment } from 'moment';

export interface IPerson {
  id?: number;
  psaId?: string;
  eraseDate?: Moment;
  active?: boolean;
  createDate?: Moment;
  updatedDate?: Moment;
  address?: string;
  zipCode?: string;
  cityId?: number;
  docTypeId?: number;
}

export class Person implements IPerson {
  constructor(
    public id?: number,
    public psaId?: string,
    public eraseDate?: Moment,
    public active?: boolean,
    public createDate?: Moment,
    public updatedDate?: Moment,
    public address?: string,
    public zipCode?: string,
    public cityId?: number,
    public docTypeId?: number
  ) {
    this.active = this.active || false;
  }
}
