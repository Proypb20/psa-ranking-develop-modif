export interface IEventCategory {
  id?: number;
  splitDeck?: boolean;
  eventName?: string;
  eventId?: number;
  categoryName?: string;
  categoryId?: number;
  formatName?: string;
  formatId?: number;
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
    public formatId?: number
  ) {
    this.splitDeck = this.splitDeck || false;
  }
}
