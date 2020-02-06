import { IPlayer } from 'app/shared/model/player.model';

export interface IRoster {
  id?: number;
  active?: boolean;
  categoryId?: number;
  players?: IPlayer[];
  teamName?: string;
  teamId?: number;
  tournamentName?: string;
  tournamentId?: number;
  eventName?: string;
  eventId?: number;
}

export class Roster implements IRoster {
  constructor(
    public id?: number,
    public active?: boolean,
    public categoryId?: number,
    public players?: IPlayer[],
    public teamName?: string,
    public teamId?: number,
    public tournamentName?: string,
    public tournamentId?: number,
    public eventName?: string,
    public eventId?: number
  ) {
    this.active = this.active || false;
  }
}
