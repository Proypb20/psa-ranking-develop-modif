export interface IFormula {
  id?: number;
  formula?: string;
  var1?: string;
  var2?: string;
  var3?: string;
  description?: string;
  example?: string;
  tournamentName?: string;
  tournamentId?: number;
}

export class Formula implements IFormula {
  constructor(
    public id?: number,
    public formula?: string,
    public var1?: string,
    public var2?: string,
    public var3?: string,
    public description?: string,
    public example?: string,
    public tournamentName?: string,
    public tournamentId?: number
  ) {}
}
