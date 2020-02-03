import { IPlayer } from 'app/shared/model/player.model';

export interface IRoster {
  id?: number;
  active?: boolean;
  categoryId?: number;
  players?: IPlayer[];
  teamName?: string;
  teamId?: number;
}

export class Roster implements IRoster {
  constructor(
    public id?: number,
    public active?: boolean,
    public categoryId?: number,
    public players?: IPlayer[],
    public teamName?: string,
    public teamId?: number
  ) {
    this.active = this.active || false;
  }
}
