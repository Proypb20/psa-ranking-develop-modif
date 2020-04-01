import { Component, OnInit, AfterViewInit, Renderer, ElementRef } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { LoginModalService } from 'app/core/login/login-modal.service';
import { Register } from './register.service';
import { JhiAlertService, JhiDataUtils } from 'ng-jhipster';

@Component({
  selector: 'jhi-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent implements OnInit, AfterViewInit {
  doNotMatch: string;
  error: string;
  errorEmailExists: string;
  errorUserExists: string;
  success: boolean;
  modalRef: NgbModalRef;

  registerForm = this.fb.group({
    login: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50), Validators.pattern('^[_.@A-Za-z0-9-]*$')]],
    lastName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    firstName: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(50)]],
    email: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(254), Validators.email]],
    password: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(50)]],
    phone: ['', [Validators.required, Validators.minLength(10), Validators.maxLength(15)]],
    numDoc: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
    bornDate: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(10)]],
    picture: [],
    pictureContentType: []
  });

  constructor(
    protected dataUtils: JhiDataUtils,
    private languageService: JhiLanguageService,
    private loginModalService: LoginModalService,
    protected jhiAlertService: JhiAlertService,
    private registerService: Register,
    private elementRef: ElementRef,
    private renderer: Renderer,
    private fb: FormBuilder
  ) {}

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#login'), 'focus', []);
  }

  register() {
    let registerAccount = {};
    const login = this.registerForm.get(['login']).value;
    const lastName = this.registerForm.get(['lastName']).value;
    const firstName = this.registerForm.get(['firstName']).value;
    const email = this.registerForm.get(['email']).value;
    const password = this.registerForm.get(['password']).value;
    const phone = this.registerForm.get(['phone']).value;
    const numDoc = this.registerForm.get(['numDoc']).value; 
    const bornDate = this.registerForm.get(['bornDate']).value;
    const picture = this.registerForm.get(['picture']).value;
    const pictureContentType = this.registerForm.get(['pictureContentType']).value;
    
    if (password !== this.registerForm.get(['confirmPassword']).value) {
      this.doNotMatch = 'ERROR';
    } else {
      registerAccount = { ...registerAccount, login, lastName, firstName, email, password, phone, numDoc, bornDate, picture, pictureContentType};
      this.doNotMatch = null;
      this.error = null;
      this.errorUserExists = null;
      this.errorEmailExists = null;
      this.languageService.getCurrent().then(langKey => {
        registerAccount = { ...registerAccount, langKey };
        this.registerService.save(registerAccount).subscribe(
          () => {
            this.success = true;
          },
          response => this.processError(response)
        );
      });
    }
  }

  openLogin() {
    this.modalRef = this.loginModalService.open();
  }

  private processError(response: HttpErrorResponse) {
    console.error(
      `Backend returned code ${response.status}, ` +
      `body was: ${response.error}`);
    this.success = null;
    if (response.status === 400 && response.error === 'error.userexists') {
      this.errorUserExists = 'ERROR';
    } else if (response.status === 400 && response.error === 'error.emailexists') {
      this.errorEmailExists = 'ERROR';
    } else {
      this.error = 'ERROR';
    }
  }
    
  protected onError(errorMessage: string) {
    this.jhiAlertService.error(errorMessage, null, null);
  }
 
   getSelected(selectedVals: any[], option: any) {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
  
  clearInputImage(field: string, fieldContentType: string, idInput: string) {
    this.registerForm.patchValue({
      [field]: null,
      [fieldContentType]: null
    });
    if (this.elementRef && idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }
  
  byteSize(field) {
    return this.dataUtils.byteSize(field);
  }

  openFile(contentType, field) {
    return this.dataUtils.openFile(contentType, field);
  }

  setFileData(event, field: string, isImage) {
    return new Promise((resolve, reject) => {
      if (event && event.target && event.target.files && event.target.files[0]) {
        const file: File = event.target.files[0];
        if (isImage && !file.type.startsWith('image/')) {
          reject(`File was expected to be an image but was found to be ${file.type}`);
        } else {
          const filedContentType: string = field + 'ContentType';
          this.dataUtils.toBase64(file, base64Data => {
            this.registerForm.patchValue({
              [field]: base64Data,
              [filedContentType]: file.type
            });
          });
        }
      } else {
        reject(`Base64 data was not set as file could not be extracted from passed parameter: ${event}`);
      }
    }).then(
      // eslint-disable-next-line no-console
      () => console.log('blob added'), // success
      this.onError
    );
  }
}