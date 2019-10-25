import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IDocType } from 'app/shared/model/doc-type.model';
import { DocTypeService } from './doc-type.service';

@Component({
  selector: 'jhi-doc-type-delete-dialog',
  templateUrl: './doc-type-delete-dialog.component.html'
})
export class DocTypeDeleteDialogComponent {
  docType: IDocType;

  constructor(protected docTypeService: DocTypeService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.docTypeService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'docTypeListModification',
        content: 'Deleted an docType'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-doc-type-delete-popup',
  template: ''
})
export class DocTypeDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ docType }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(DocTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.docType = docType;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/doc-type', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/doc-type', { outlets: { popup: null } }]);
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
