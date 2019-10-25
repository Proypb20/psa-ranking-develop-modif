import { Moment } from 'moment';
import { IEvent } from 'app/shared/model/event.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface ITournament {
  id?: number;
  name?: string;
  closeInscrDays?: number;
  status?: Status;
  createDate?: Moment;
  updatedDate?: Moment;
  addressId?: number;
  events?: IEvent[];
  ownerId?: number;
}

export class Tournament implements ITournament {
  constructor(
    public id?: number,
    public name?: string,
    public closeInscrDays?: number,
    public status?: Status,
    public createDate?: Moment,
    public updatedDate?: Moment,
    public addressId?: number,
    public events?: IEvent[],
    public ownerId?: number
  ) {}
}
