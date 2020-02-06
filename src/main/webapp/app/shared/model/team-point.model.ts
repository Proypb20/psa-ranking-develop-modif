export interface ITeamPoint {
  id?: number;
  points?: number;
  teamName?: string;
  teamId?: number;
  tournamentName?: string;
  tournamentId?: number;
}

export class TeamPoint implements ITeamPoint {
  constructor(
    public id?: number,
    public points?: number,
    public teamName?: string,
    public teamId?: number,
    public tournamentName?: string,
    public tournamentId?: number
  ) {}
}
