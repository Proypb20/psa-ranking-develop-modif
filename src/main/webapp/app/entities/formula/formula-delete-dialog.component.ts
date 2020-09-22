import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFormula } from 'app/shared/model/formula.model';
import { FormulaService } from './formula.service';

@Component({
  selector: 'jhi-formula-delete-dialog',
  templateUrl: './formula-delete-dialog.component.html'
})
export class FormulaDeleteDialogComponent {
  formula: IFormula;

  constructor(protected formulaService: FormulaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

  clear() {
    this.activeModal.dismiss('cancel');
  }

  confirmDelete(id: number) {
    this.formulaService.delete(id).subscribe(response => {
      this.eventManager.broadcast({
        name: 'formulaListModification',
        content: 'Deleted an formula'
      });
      this.activeModal.dismiss(true);
    });
  }
}

@Component({
  selector: 'jhi-formula-delete-popup',
  template: ''
})
export class FormulaDeletePopupComponent implements OnInit, OnDestroy {
  protected ngbModalRef: NgbModalRef;

  constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ formula }) => {
      setTimeout(() => {
        this.ngbModalRef = this.modalService.open(FormulaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
        this.ngbModalRef.componentInstance.formula = formula;
        this.ngbModalRef.result.then(
          result => {
            this.router.navigate(['/formula', { outlets: { popup: null } }]);
            this.ngbModalRef = null;
          },
          reason => {
            this.router.navigate(['/formula', { outlets: { popup: null } }]);
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
