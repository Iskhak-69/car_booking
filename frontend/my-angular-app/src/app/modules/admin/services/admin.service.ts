import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { StorageService } from 'src/app/auth/services/storage/storage.service';

const BASIC_URL =["http://localhost:8081"];

@Injectable({
  providedIn: 'root'
})
export class AdminService {

  constructor(private http: HttpClient) { }

  postCar(carDto: any) {
    return this.http.post(BASIC_URL + "/api/admin/car", carDto, {
      headers: this.createAuthorizationHeader()
    });
  }

  getAllCars(): Observable<any> {
    return this.http.get(BASIC_URL + "/api/admin/cars", {
      headers: this.createAuthorizationHeader()
    })
  }

  deleteCar(carId: number): Observable<any> {
    return this.http.delete(BASIC_URL + "/api/admin/car/" + carId, {
      headers: this.createAuthorizationHeader()
    })
  }

  updateCar(carId: number, carDto: any) {
    return this.http.put(BASIC_URL + "/api/admin/car/" + carId, carDto, {
      headers: this.createAuthorizationHeader()
    });
  }

  getCarById(carId: number): Observable<any> {
    return this.http.get(BASIC_URL + "/api/admin/car/" + carId, {
      headers: this.createAuthorizationHeader(),
    });
  }


  createAuthorizationHeader(): HttpHeaders {
    let auhtHeaders: HttpHeaders = new HttpHeaders();
    return auhtHeaders.set('Authorization',
      'Bearer ' + StorageService.getToken()
    );
  }
  // createAuthorizationHeader(): HttpHeaders {
  //   const token = StorageService.getToken();
  //   if (!token) {
  //     console.error('Authorization token is missing!');
  //   }
  //   return new HttpHeaders().set('Authorization', 'Bearer ' + token);
  // }
  
}
