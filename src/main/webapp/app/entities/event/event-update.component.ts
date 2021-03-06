import { Component, OnInit } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';
import { IEvent, Event } from 'app/shared/model/event.model';
import { EventService } from './event.service';
import { ITournament } from 'app/shared/model/tournament.model';
import { TournamentService } from 'app/entities/tournament/tournament.service';
import { ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city/city.service';
import { AccountService } from 'app/core/auth/account.service';

@Component({
  selector: 'jhi-event-update',
  templateUrl: './event-update.component.html'
})
export class EventUpdateComponent implements OnInit {
  isSaving: boolean;
  currentAccount: any;
  tournaments: ITournament[];
  tId: number;
  cities: ICity[];
  fromDateDp: any;
  endDateDp: any;
  endInscriptionDateDp: any;

  editForm = this.fb.group({
    id: [],
    name: [],
    fromDate: [],
    endDate: [],
    endInscriptionDate: [],
    status: [],
    createDate: [],
    updatedDate: [],
    tournamentId: [],
    cityId: []
  });

  constructor(
    protected jhiAlertService: JhiAlertService,
    protected eventService: EventService,
    protected tournamentService: TournamentService,
    protected accountService: AccountService,
    protected cityService: CityService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.isSaving = false;
    this.accountService.identity().subscribe(account => {
      this.currentAccount = account;
    });
    if (localStorage.getItem("TOURNAMENTID"))
    {
       this.tId = +localStorage.getItem("TOURNAMENTID")
    };
    this.activatedRoute.data.subscribe(({ event }) => {
      this.updateForm(event);
    });
    if (this.currentAccount.authorities.includes('ROLE_OWNER_TOURNAMENT') && !this.currentAccount.authorities.includes('ROLE_ADMIN'))
    {
      this.tournamentService
      .query({'ownerId.equals': this.currentAccount.id})
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    }
    else
    {
      this.tournamentService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ITournament[]>) => mayBeOk.ok),
        map((response: HttpResponse<ITournament[]>) => response.body)
      )
      .subscribe((res: ITournament[]) => (this.tournaments = res), (res: HttpErrorResponse) => this.onError(res.message));
    }
    this.cityService
      .query()
      .pipe(
        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
        map((response: HttpResponse<ICity[]>) => response.body)
      )
      .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
  }

  updateForm(event: IEvent) {
    this.editForm.patchValue({
      id: event.id,
      name: event.name,
      fromDate: event.fromDate,
      endDate: event.endDate,
      endInscriptionDate: event.endInscriptionDate,
      status: event.status,
      createDate: event.createDate != null ? event.createDate.format(DATE_TIME_FORMAT) : null,
      updatedDate: event.updatedDate != null ? event.updatedDate.format(DATE_TIME_FORMAT) : null,
      tournamentId: this.tId || event.tournamentId,
      cityId: event.cityId
    });
  }

  previousState() {
    window.history.back();
  }

  save() {
    this.isSaving = true;
    const event = this.createFromForm();
    if (event.id !== undefined) {
      this.subscribeToSaveResponse(this.eventService.update(event));
    } else {
      this.subscribeToSaveResponse(this.eventService.create(event));
    }
  }

  private createFromForm(): IEvent {
    return {
      ...new Event(),
      id: this.editForm.get(['id']).value,
      name: this.editForm.get(['name']).value,
      fromDate: this.editForm.get(['fromDate']).value,
      endDate: this.editForm.get(['endDate']).value,
      endInscriptionDate: this.editForm.get(['endInscriptionDate']).value,
      status: this.editForm.get(['status']).value,
      createDate:
        this.editForm.get(['createDate']).value != null ? moment(this.editForm.get(['createDate']).value, DATE_TIME_FORMAT) : undefined,
      updatedDate:
        this.editForm.get(['updatedDate']).value != null ? moment(this.editForm.get(['updatedDate']).value, DATE_TIME_FORMAT) : undefined,
      tournamentId: this.editForm.get(['tournamentId']).value,
      cityId: this.editForm.get(['cityId']).value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEvent>>) {
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

  trackTournamentById(index: number, item: ITournament) {
    return item.id;
  }

  trackCityById(index: number, item: ICity) {
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
