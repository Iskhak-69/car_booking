import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AdminRoutingModule } from './admin-routing.module';
import { AdminDashboardComponent } from './components/admin-dashboard/admin-dashboard.component';
import { PostCarComponent } from './components/post-car/post-car.component';
import { NgZorroModule } from 'src/app/NgZorroModule';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateCarComponent } from './components/update-car/update-car.component';


@NgModule({
  declarations: [
    AdminDashboardComponent,
    PostCarComponent,
    UpdateCarComponent
  ],
  imports: [
    CommonModule,
    AdminRoutingModule,
    NgZorroModule,
    ReactiveFormsModule,
    FormsModule
  ]
})
export class AdminModule { }
