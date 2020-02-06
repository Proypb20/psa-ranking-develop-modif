import { ProfileUser } from 'app/shared/model/enumerations/profile-user.model';

export interface IPlayer {
  id?: number;
  profile?: ProfileUser;
  userLogin?: string;
  userId?: number;
  rosterId?: number;
}

export class Player implements IPlayer {
  constructor(
    public id?: number,
    public profile?: ProfileUser,
    public userLogin?: string,
    public userId?: number,
    public rosterId?: number
  ) {}
}
