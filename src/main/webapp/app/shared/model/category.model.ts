import { IEvent } from 'app/shared/model/event.model';

export interface ICategory {
  id?: number;
  name?: string;
  description?: string;
  events?: IEvent[];
}

export class Category implements ICategory {
  constructor(public id?: number, public name?: string, public description?: string, public events?: IEvent[]) {}
}
