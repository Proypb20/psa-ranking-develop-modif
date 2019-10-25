import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRoster } from 'app/shared/model/roster.model';
import { RosterService } from './roster.service';

@Component({
  selector: 'jhi-roster-delete-dialog',
  templateUrl: './roster-delete-dialog.component.html'
})
export class RosterDeleteDialogComponent {
  roster: IRoster;

  constructor(protected rosterService: RosterService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.rosterService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'rosterListModification',
        content: 'Deleted an roster'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-roster-delete-popup',
  template: ''
})
export class RosterDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ roster }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(RosterDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.roster = roster;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/roster', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/roster', { outlets: { popup: null } }]);
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
