import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDocType } from 'app/shared/model/doc-type.model';

@Component({
  selector: 'jhi-doc-type-detail',
  templateUrl: './doc-type-detail.component.html'
})
export class DocTypeDetailComponent implements OnInit {
  docType: IDocType;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ docType }) => {
      this.docType = docType;
    });
  }

  previousState() {
    window.history.back();
  }
}
