import { Moment } from 'moment';
import { IPlayer } from 'app/shared/model/player.model';
import { ProfileUser } from 'app/shared/model/enumerations/profile-user.model';

export interface IRoster {
  id?: number;
  active?: boolean;
  profile?: ProfileUser;
  createDate?: Moment;
  updatedDate?: Moment;
  categoryId?: number;
  players?: IPlayer[];
}

export class Roster implements IRoster {
  constructor(
    public id?: number,
    public active?: boolean,
    public profile?: ProfileUser,
    public createDate?: Moment,
    public updatedDate?: Moment,
    public categoryId?: number,
    public players?: IPlayer[]
  ) {
    this.active = this.active || false;
  }
}
