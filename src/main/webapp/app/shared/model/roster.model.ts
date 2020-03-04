export interface IRoster {
  id?: number;
  active?: boolean;
  teamName?: string;
  teamId?: number;
  eventCategoryId?: number;
}

export class Roster implements IRoster {
  constructor(
    public id?: number,
    public active?: boolean,
    public teamName?: string,
    public teamId?: number,
    public eventCategoryId?: number
  ) {
    this.active = this.active || false;
  }
}
