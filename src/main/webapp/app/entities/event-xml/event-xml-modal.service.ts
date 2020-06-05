import { Injectable } from '@angular/core';
import { NgbModal, NgbModalRef, NgbModalOptions } from '@ng-bootstrap/ng-bootstrap';

import { EventXmlModalComponent } from 'app/entities/event-xml/event-xml.component';

@Injectable({ providedIn: 'root' })
export class EventXmlModalService {
  private isOpen = false;
  constructor(private modalService: NgbModal) {}

  open(): NgbModalRef {
    if (this.isOpen) {
      return;
    }
    this.isOpen = true;

    const ngbModalOptions: NgbModalOptions = {
          backdrop : 'static',
          keyboard : false
    };

    const modalRef = this.modalService.open(EventXmlModalComponent, ngbModalOptions);
    modalRef.result.finally(() => (this.isOpen = false));
    return modalRef;
  }
}
