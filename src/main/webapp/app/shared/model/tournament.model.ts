import { IEvent } from 'app/shared/model/event.model';
import { Status } from 'app/shared/model/enumerations/status.model';

export interface ITournament {
  id?: number;
  name?: string;
  closeInscrDays?: number;
  status?: Status;
  categorize?: boolean;
  logoContentType?: string;
  logo?: any;
  cantPlayersNextCategory?: number;
  qtyTeamGroups?: number;
  events?: IEvent[];
  ownerLogin?: string;
  ownerId?: number;
}

export class Tournament implements ITournament {
  constructor(
    public id?: number,
    public name?: string,
    public closeInscrDays?: number,
    public status?: Status,
    public categorize?: boolean,
    public logoContentType?: string,
    public logo?: any,
    public cantPlayersNextCategory?: number,
    public qtyTeamGroups?: number,
    public events?: IEvent[],
    public ownerLogin?: string,
    public ownerId?: number
  ) {
    this.categorize = this.categorize || false;
  }
}
