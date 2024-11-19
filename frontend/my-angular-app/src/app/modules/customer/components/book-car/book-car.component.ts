import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { NzMessageService } from 'ng-zorro-antd/message';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { StorageService } from 'src/app/auth/services/storage/storage.service';
import { error } from 'console';

@Component({
  selector: 'app-book-car',
  templateUrl: './book-car.component.html',
  styleUrls: ['./book-car.component.scss']
})
export class BookCarComponent {

  car: any;
  carId: number = this.activated.snapshot.params["id"];
  bookACarForm!: FormGroup;
  isSpinning = false;
  dateFormat = 'yyyy-mm-dd';

  constructor(private customerService: CustomerService,
    private message: NzMessageService,
    private activated: ActivatedRoute,
    private fb: FormBuilder
  ) {
    this.bookACarForm = this.fb.group({
      fromDate: [null, Validators.required],
      toDate: [null, Validators.required]
    })
    this.getCarById();
  }

  getCarById() {
    this.customerService.getCarById(this.carId).subscribe((res) => {
      console.log(res);
      res.processedImg = "data:image/jpeg;base64,"  + res.returnedImage;
      this.car = res;
      })
  }

  bookCar(FormData: any){
    this.isSpinning = true;
    let obj = {
      fromDate: FormData.fromDate,
      toDate: FormData.toDate,
      userId: StorageService.getUserId()
    }
    this.customerService.bookACar(this.carId, obj).subscribe((res) => {
      this.isSpinning = false;
      console.log(res);
      this.message.success("Car booked successfully", { nzDuration: 5000 });
    }, error => {
      this.message.error ("Something went wrong", { nzDuration: 5000 });
    })
  }
}

