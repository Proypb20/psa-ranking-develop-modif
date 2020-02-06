import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ITeamPoint } from 'app/shared/model/team-point.model';
import { TeamPointService } from './team-point.service';

@Component({
  selector: 'jhi-team-point-delete-dialog',
  templateUrl: './team-point-delete-dialog.component.html'
})
export class TeamPointDeleteDialogComponent {
  teamPoint: ITeamPoint;

  constructor(protected teamPointService: TeamPointService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.teamPointService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'teamPointListModification',
        content: 'Deleted an teamPoint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-team-point-delete-popup',
  template: ''
})
export class TeamPointDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ teamPoint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(TeamPointDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.teamPoint = teamPoint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/team-point', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/team-point', { outlets: { popup: null } }]);
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
