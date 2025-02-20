import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private httpClient: HttpClient) {}
   GetAll() {
    return this.httpClient.get("http://localhost:8080/api/test");
  }
}
