import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITeamDetailPoint } from 'app/shared/model/team-detail-point.model';
import { TeamDetailPointService } from './team-detail-point.service';

@Component({
  selector: 'jhi-team-detail-point-delete-dialog',
  templateUrl: './team-detail-point-delete-dialog.component.html'
})
export class TeamDetailPointDeleteDialogComponent {
  teamDetailPoint: ITeamDetailPoint;

  constructor(
    protected teamDetailPointService: TeamDetailPointService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.teamDetailPointService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'teamDetailPointListModification',
        content: 'Deleted an teamDetailPoint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-team-detail-point-delete-popup',
  template: ''
})
export class TeamDetailPointDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ teamDetailPoint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TeamDetailPointDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.teamDetailPoint = teamDetailPoint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/team-detail-point', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/team-detail-point', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          }
        );
      }, 0);
    });
  }

  ngOnDestroy() {
    this.ngbModalRef = null;
  }
}
