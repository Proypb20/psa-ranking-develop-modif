import { Status } from 'app/shared/model/enumerations/status.model';

export interface IGame {
  id?: number;
  pointsA?: number;
  pointsB?: number;
  splitDeckNum?: number;
  timeLeft?: number;
  status?: Status;
  overtimeA?: number;
  overtimeB?: number;
  uvuA?: number;
  uvuB?: number;
  teamAName?: string;
  teamAId?: number;
  teamBName?: string;
  teamBId?: number;
  eventCategoryId?: number;
}

export class Game implements IGame {
  constructor(
    public id?: number,
    public pointsA?: number,
    public pointsB?: number,
    public splitDeckNum?: number,
    public timeLeft?: number,
    public status?: Status,
    public overtimeA?: number,
    public overtimeB?: number,
    public uvuA?: number,
    public uvuB?: number,
    public teamAName?: string,
    public teamAId?: number,
    public teamBName?: string,
    public teamBId?: number,
    public eventCategoryId?: number
  ) {}
}
