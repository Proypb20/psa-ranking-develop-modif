import { IRoster } from 'app/shared/model/roster.model';
import { ProfileUser } from 'app/shared/model/enumerations/profile-user.model';

export interface IPlayer {
  id?: number;
  profile?: ProfileUser;
  rosters?: IRoster[];
  userLogin?: string;
  userId?: number;
}

export class Player implements IPlayer {
  constructor(
    public id?: number,
    public profile?: ProfileUser,
    public rosters?: IRoster[],
    public userLogin?: string,
    public userId?: number
  ) {}
}
