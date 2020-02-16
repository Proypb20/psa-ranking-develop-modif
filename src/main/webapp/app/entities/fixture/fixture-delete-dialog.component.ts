import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFixture } from 'app/shared/model/fixture.model';
import { FixtureService } from './fixture.service';

@Component({
  selector: 'jhi-fixture-delete-dialog',
  templateUrl: './fixture-delete-dialog.component.html'
})
export class FixtureDeleteDialogComponent {
  fixture: IFixture;

  constructor(protected fixtureService: FixtureService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.fixtureService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'fixtureListModification',
        content: 'Deleted an fixture'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-fixture-delete-popup',
  template: ''
})
export class FixtureDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ fixture }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FixtureDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.fixture = fixture;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/fixture', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/fixture', { outlets: { popup: null } }]);
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
