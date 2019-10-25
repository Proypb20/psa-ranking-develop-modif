import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IPlayer } from 'app/shared/model/player.model';
import { PlayerService } from './player.service';

@Component({
  selector: 'jhi-player-delete-dialog',
  templateUrl: './player-delete-dialog.component.html'
})
export class PlayerDeleteDialogComponent {
  player: IPlayer;

  constructor(protected playerService: PlayerService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.playerService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'playerListModification',
        content: 'Deleted an player'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-player-delete-popup',
  template: ''
})
export class PlayerDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ player }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(PlayerDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.player = player;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/player', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/player', { outlets: { popup: null } }]);
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
