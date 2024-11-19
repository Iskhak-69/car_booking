import { NgModule } from '@angular/core';

// NG ZORRO IMPORTS
import { NzLayoutModule } from 'ng-zorro-antd/layout';
import { NzFormModule } from 'ng-zorro-antd/form';
import { NzInputModule } from 'ng-zorro-antd/input';
import { NzSpinModule } from 'ng-zorro-antd/spin';
import { NzMessageModule } from 'ng-zorro-antd/message';
import { NzButtonModule } from 'ng-zorro-antd/button';
import { NzSelectModule } from 'ng-zorro-antd/select';
import { NzDatePickerModule } from 'ng-zorro-antd/date-picker';
import { NzNotificationModule } from 'ng-zorro-antd/notification';



@NgModule({
  exports: [
    // NG ZORRO IMPORTS
    NzLayoutModule,
    NzFormModule,
    NzInputModule,
    NzSpinModule,
    NzMessageModule,
    NzButtonModule,
    NzSelectModule,
    NzDatePickerModule,
    NzNotificationModule 
  ]
})
export class NgZorroModule { }
