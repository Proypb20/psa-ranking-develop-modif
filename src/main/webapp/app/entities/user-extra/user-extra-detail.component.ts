import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JhiDataUtils } from 'ng-jhipster';

import { IUserExtra } from 'app/shared/model/user-extra.model';

@Component({
  selector: 'jhi-user-extra-detail',
  templateUrl: './user-extra-detail.component.html'
})
export class UserExtraDetailComponent implements OnInit {
  userExtra: IUserExtra;

  constructor(protected dataUtils: JhiDataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit() {
    this.activatedRoute.data.subscribe(({ userExtra }) => {
      this.userExtra = userExtra;
    });
  }

  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }
  previousState() {
    window.history.back();
  }
}
