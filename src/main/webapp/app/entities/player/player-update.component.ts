import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { IPlayer, Player } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IRoster } from 'app/shared/model/roster.model';
import { RosterService } from 'app/entities/roster/roster.service';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html'
})
export class PlayerUpdateComponent implements OnInit {
  isSaving: boolean;

  users: IUser[];

  rosters: IRoster[];
  
  rId: number;
  private sub: any;

  editForm = this.fb.group({
    id: [],
    profile: [],
    userId: [],
    rosterId: [null, Validators.required]
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerService: PlayerService,
    protected userService: UserService,
    protected rosterService: RosterService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
  this.sub = this.activatedRoute
        .queryParams
        .subscribe(params => {
           this.rId = +params['rId'] || 0;
      });
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);
    });
    this.userService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUser[]>) => response.body)
      )
      .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    this.rosterService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<IRoster[]>) => mayBeOk.ok),
        map((response: HttpResponse<IRoster[]>) => response.body)
      )
      .subscribe((res: IRoster[]) => (this.rosters = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(player: IPlayer) {
    this.editForm.patchValue({
      id: player.id,
      profile: player.profile,
      userId: player.userId,
      rosterId: this.rId || player.rosterId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const player = this.createFromForm();
    if (player.id !== undefined) {
      this.subscribeToSaveResponse(this.playerService.update(player));
    } else {
      this.subscribeToSaveResponse(this.playerService.create(player));
    }
  }

  private createFromForm(): IPlayer {
    return {
      ...new Player(),
      id: this.editForm.get(['id']).value,
      profile: this.editForm.get(['profile']).value,
      userId: this.editForm.get(['userId']).value,
      rosterId: this.rId || this.editForm.get(['rosterId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayer>>) {
    result.subscribe(() => this.onSaveSuccess(), () => this.onSaveError());
  }

  protected onSaveSuccess() {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError() {
    this.isSaving = false;
  }
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }

  trackUserById(index: number, item: IUser) {
    return item.id;
  }

  trackRosterById(index: number, item: IRoster) {
    return item.id;
  }
}
