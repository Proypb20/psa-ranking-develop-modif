import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { PsaRankingSharedModule } from 'app/shared/shared.module';
import { TournamentComponent } from './tournament.component';
import { TournamentDetailComponent } from './tournament-detail.component';
import { TournamentUpdateComponent } from './tournament-update.component';
import { TournamentDeletePopupComponent, TournamentDeleteDialogComponent } from './tournament-delete-dialog.component';
import { tournamentRoute, tournamentPopupRoute } from './tournament.route';

const ENTITY_STATES = [...tournamentRoute, ...tournamentPopupRoute];

@NgModule({
  imports: [PsaRankingSharedModule, RouterModule.forChild(ENTITY_STATES)],
  declarations: [
    TournamentComponent,
    TournamentDetailComponent,
    TournamentUpdateComponent,
    TournamentDeleteDialogComponent,
    TournamentDeletePopupComponent
  ],
  entryComponents: [TournamentDeleteDialogComponent]
})
export class PsaRankingTournamentModule {}
