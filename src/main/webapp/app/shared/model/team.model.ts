export interface ITeam {
  id?: number;
  name?: string;
  active?: boolean;
  ownerLogin?: string;
  ownerId?: number;
}

export class Team implements ITeam {
  constructor(public id?: number, public name?: string, public active?: boolean, public ownerLogin?: string, public ownerId?: number) {
    this.active = this.active || false;
  }
}
