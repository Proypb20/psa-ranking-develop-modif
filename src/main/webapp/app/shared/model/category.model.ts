import { TimeType } from 'app/shared/model/enumerations/time-type.model';

export interface ICategory {
  id?: number;
  name?: string;
  description?: string;
  gameTimeType?: TimeType;
  gameTime?: number;
  stopTimeType?: TimeType;
  stopTime?: number;
  totalPoints?: number;
  difPoints?: number;
  order?: number;
  tournamentName?: string;
  tournamentId?: number;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public name?: string,
    public description?: string,
    public gameTimeType?: TimeType,
    public gameTime?: number,
    public stopTimeType?: TimeType,
    public stopTime?: number,
    public totalPoints?: number,
    public difPoints?: number,
    public order?: number,
    public tournamentName?: string,
    public tournamentId?: number
  ) {}
}
