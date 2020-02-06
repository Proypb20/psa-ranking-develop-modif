export interface IPlayerDetailPoint {
  id?: number;
  points?: number;
  eventName?: string;
  eventId?: number;
  playerPointId?: number;
}

export class PlayerDetailPoint implements IPlayerDetailPoint {
  constructor(
    public id?: number,
    public points?: number,
    public eventName?: string,
    public eventId?: number,
    public playerPointId?: number
  ) {}
}
