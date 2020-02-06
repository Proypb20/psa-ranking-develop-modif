import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayerDetailPoint } from 'app/shared/model/player-detail-point.model';
import { PlayerDetailPointService } from './player-detail-point.service';

@Component({
  selector: 'jhi-player-detail-point-delete-dialog',
  templateUrl: './player-detail-point-delete-dialog.component.html'
})
export class PlayerDetailPointDeleteDialogComponent {
  playerDetailPoint: IPlayerDetailPoint;

  constructor(
    protected playerDetailPointService: PlayerDetailPointService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.playerDetailPointService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'playerDetailPointListModification',
        content: 'Deleted an playerDetailPoint'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-player-detail-point-delete-popup',
  template: ''
})
export class PlayerDetailPointDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ playerDetailPoint }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlayerDetailPointDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.playerDetailPoint = playerDetailPoint;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/player-detail-point', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/player-detail-point', { outlets: { popup: null } }]);
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
