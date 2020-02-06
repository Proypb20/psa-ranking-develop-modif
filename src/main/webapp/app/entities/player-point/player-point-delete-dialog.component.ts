import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerPoint } from 'app/shared/model/player-point.model';
import { PlayerPointService } from './player-point.service';

@Component({
  selector: 'jhi-player-point-delete-dialog',
  templateUrl: './player-point-delete-dialog.component.html'
})
export class PlayerPointDeleteDialogComponent {
  playerPoint: IPlayerPoint;

  constructor(
    protected playerPointService: PlayerPointService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.playerPointService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'playerPointListModification',
        content: 'Deleted an playerPoint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-player-point-delete-popup',
  template: ''
})
export class PlayerPointDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerPoint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlayerPointDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.playerPoint = playerPoint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/player-point', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/player-point', { outlets: { popup: null } }]);
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
