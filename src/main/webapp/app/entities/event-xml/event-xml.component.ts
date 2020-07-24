import { Component, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';
import { JhiEventManager } from 'ng-jhipster';
import { EventXmlService } from 'app/entities/event-xml/event-xml.service';
import { HttpClient } from '@angular/common/http';
import { SERVER_API_URL } from 'app/app.constants';
import { JhiAlertService } from 'ng-jhipster';

@Component({
  selector: 'jhi-event-xml-modal',
  templateUrl: './event-xml.component.html'
})
export class EventXmlModalComponent implements AfterViewInit {
  public resourceUrl = SERVER_API_URL + 'api/events';
  eventXmlForm = this.fb.group({
    file: [null]
  });

  constructor(
    private eventManager: JhiEventManager,
    private eventXmlService: EventXmlService,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private router: Router,
    public activeModal: NgbActiveModal,
    private fb: FormBuilder,
    protected jhiAlertService: JhiAlertService,
    private httpClient: HttpClient
  ) {}

  ngAfterViewInit() {
    setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#username'), 'focus', []), 0);
  }
  cancel() {
    this.eventXmlForm.patchValue({
      file: '',
    });
    this.activeModal.dismiss('cancel');
  }

  onFileSelect(event) {
    if (event.target.files.length > 0) {
      const file = event.target.files[0];
      this.eventXmlForm.get('file').setValue(file);
    }
  }

  onSubmit() {
    const formData = new FormData();
    formData.set('file', this.eventXmlForm.get('file').value);
    this.httpClient.post<any>(this.resourceUrl + '/importXML', formData).subscribe(
      (res) => this.onError(res.message));
  }

  onError(errorMessage: string) {
	    this.jhiAlertService.error(errorMessage, null, null);
  }

}
