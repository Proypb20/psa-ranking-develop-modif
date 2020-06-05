import { NgModule } from '@angular/core';

import { PbPointsSharedModule } from 'app/shared/shared.module';
import { EventXmlModalComponent } from './event-xml.component';

@NgModule({
  declarations: [EventXmlModalComponent],
  entryComponents: [EventXmlModalComponent],
  exports: [EventXmlModalComponent]
})
export class PbPointsEventXmlModule {}
