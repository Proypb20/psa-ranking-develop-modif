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
import { IUserExtra } from 'app/shared/model/user-extra.model';
import { UserExtraService } from 'app/entities/user-extra/user-extra.service';
import { IRoster } from 'app/shared/model/roster.model';
import { RosterService } from 'app/entities/roster/roster.service';

@Component({
  selector: 'jhi-player-update',
  templateUrl: './player-update.component.html'
})
export class PlayerUpdateComponent implements OnInit {
  isSaving: boolean;

  userextras: IUserExtra[];

  rosters: IRoster[];

  editForm = this.fb.group({
    id: [],
    profile: [],
    captainFlag: [],
    userExtraId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected playerService: PlayerService,
    protected userExtraService: UserExtraService,
    protected rosterService: RosterService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.activatedRoute.data.subscribe(({ player }) => {
      this.updateForm(player);
    });
    this.userExtraService
      .query({ filter: 'player-is-null' })
      .pipe(
        filter((mayBeOk: HttpResponse<IUserExtra[]>) => mayBeOk.ok),
        map((response: HttpResponse<IUserExtra[]>) => response.body)
      )
      .subscribe(
        (res: IUserExtra[]) => {
          if (!this.editForm.get('userExtraId').value) {
            this.userextras = res;
          } else {
            this.userExtraService
              .find(this.editForm.get('userExtraId').value)
              .pipe(
                filter((subResMayBeOk: HttpResponse<IUserExtra>) => subResMayBeOk.ok),
                map((subResponse: HttpResponse<IUserExtra>) => subResponse.body)
              )
              .subscribe(
                (subRes: IUserExtra) => (this.userextras = [subRes].concat(res)),
                (subRes: HttpErrorResponse) => this.onError(subRes.message)
              );
          }
        },
        (res: HttpErrorResponse) => this.onError(res.message)
      );
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
      captainFlag: player.captainFlag,
      userExtraId: player.userExtraId
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
      captainFlag: this.editForm.get(['captainFlag']).value,
      userExtraId: this.editForm.get(['userExtraId']).value
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

  trackUserExtraById(index: number, item: IUserExtra) {
    return item.id;
  }

  trackRosterById(index: number, item: IRoster) {
    return item.id;
  }

  getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
