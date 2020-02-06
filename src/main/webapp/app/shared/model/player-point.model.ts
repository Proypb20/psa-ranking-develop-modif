export interface IPlayerPoint {
  id?: number;
  points?: number;
  tournamentName?: string;
  tournamentId?: number;
  userLogin?: string;
  userId?: number;
}

export class PlayerPoint implements IPlayerPoint {
  constructor(
    public id?: number,
    public points?: number,
    public tournamentName?: string,
    public tournamentId?: number,
    public userLogin?: string,
    public userId?: number
  ) {}
}
