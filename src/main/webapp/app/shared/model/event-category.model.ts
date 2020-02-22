import { IGame } from 'app/shared/model/game.model';

export interface IEventCategory {
  id?: number;
  splitDeck?: boolean;
  eventName?: string;
  eventId?: number;
  categoryName?: string;
  categoryId?: number;
  formatName?: string;
  formatId?: number;
  games?: IGame[];
}

export class EventCategory implements IEventCategory {
  constructor(
    public id?: number,
    public splitDeck?: boolean,
    public eventName?: string,
    public eventId?: number,
    public categoryName?: string,
    public categoryId?: number,
    public formatName?: string,
    public formatId?: number,
    public games?: IGame[]
  ) {
    this.splitDeck = this.splitDeck || false;
  }
}
