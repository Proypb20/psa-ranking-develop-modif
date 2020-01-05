import { Moment } from 'moment';
import { ICategory } from 'app/shared/model/category.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface IEvent {
  id?: number;
  name?: string;
  fromDate?: Moment;
  endDate?: Moment;
  endInscriptionDate?: Moment;
  status?: Status;
  createDate?: Moment;
  updatedDate?: Moment;
  tournamentId?: number;
  cityId?: number;
  categories?: ICategory[];
}

export class Event implements IEvent {
  constructor(
    public id?: number,
    public name?: string,
    public fromDate?: Moment,
    public endDate?: Moment,
    public endInscriptionDate?: Moment,
    public status?: Status,
    public createDate?: Moment,
    public updatedDate?: Moment,
    public tournamentId?: number,
    public cityId?: number,
    public categories?: ICategory[]
  ) {}
}
