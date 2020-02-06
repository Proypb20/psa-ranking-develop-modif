export interface ITeamDetailPoint {
  id?: number;
  points?: number;
  teamPointId?: number;
  eventName?: string;
  eventId?: number;
}

export class TeamDetailPoint implements ITeamDetailPoint {
  constructor(
    public id?: number,
    public points?: number,
    public teamPointId?: number,
    public eventName?: string,
    public eventId?: number
  ) {}
}
