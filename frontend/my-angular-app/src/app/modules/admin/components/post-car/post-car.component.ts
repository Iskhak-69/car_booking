import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AdminService } from '../../services/admin.service';
import { Router } from '@angular/router';
import { NzNotificationService } from 'ng-zorro-antd/notification';

@Component({
  selector: 'app-post-car',
  templateUrl: './post-car.component.html',
  styleUrls: ['./post-car.component.scss']
})
export class PostCarComponent implements OnInit {

  postCarForm!: FormGroup;
  isSpinning = false;
  selectedFile!: File | null;
  imagePreview: string | ArrayBuffer | null = null;
  listOfOption: Array<{ label: string; value: string}> = [];
  listOfBrands = ["BMW", "AUDI", "FERRARI", "TESLA", "VOLVO", "TOYOTA", "HONDA", "FORD", "NISSAN", "HYUNDAI", "LEXUS", "KIA"];
  listOfType = ["Petrol", "Hybrid", "Diesel", "Electric", "CNG"];
  listOfColor = ["Red", "White", "Blue", "Black", "Orange", "Grey", "Silver"];
  listOfTransmission =["Manual", "Automatic"];

  constructor(private fb: FormBuilder,
              private adminService: AdminService,
              private router: Router,
              private notification: NzNotificationService ) {}

  ngOnInit() {
    this.postCarForm = this.fb.group({
      name: [null, Validators.required],
      brand: [null, Validators.required],
      type: [null, Validators.required],
      transmission: [null, Validators.required],
      color: [null, Validators.required],
      price: [null, Validators.required],
      description: [null, Validators.required],
      modelYear: [new Date(), Validators.required],
    });
  }

  onYearChange(selectedYear: Date) {
    if (selectedYear) {
      const year = selectedYear.getFullYear();
      this.postCarForm.patchValue({ modelYear: year });
    }
  }

  postCar() {
    console.log(this.postCarForm.value);
    const formData: FormData = new FormData();
    formData.append('brand', this.postCarForm.get('brand')?.value);
    formData.append('name', this.postCarForm.get('name')?.value);
    formData.append('type', this.postCarForm.get('type')?.value);
    formData.append('color', this.postCarForm.get('color')?.value);
    formData.append('modelYear', this.postCarForm.get('modelYear')?.value);
    formData.append('transmission', this.postCarForm.get('transmission')?.value);
    formData.append('description', this.postCarForm.get('description')?.value);
    formData.append('price', this.postCarForm.get('price')?.value);

    if (this.selectedFile) {
      formData.append('image', this.selectedFile, this.selectedFile.name);
    }

    this.adminService.postCar(formData).subscribe((res) => {
      console.log(res);
      this.notification.success("Success", "Car posted successfully", { nzDuration: 5000 });
      this.router.navigateByUrl("/admin/dashboard");
    }, error => {
      this.notification.error("ERROR", "Error while posting car", { nzDuration: 5000 });
    });
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.previewImage();
  }

  previewImage() {
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = () => {
        this.imagePreview = reader.result;
      }
      reader.readAsDataURL(this.selectedFile);
    }
  }
}
