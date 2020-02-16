import { Status } from 'app/shared/model/enumerations/status.model';

export interface IFixture {
  id?: number;
  status?: Status;
  eventName?: string;
  eventId?: number;
  categoryName?: string;
  categoryId?: number;
}

export class Fixture implements IFixture {
  constructor(
    public id?: number,
    public status?: Status,
    public eventName?: string,
    public eventId?: number,
    public categoryName?: string,
    public categoryId?: number
  ) {}
}
