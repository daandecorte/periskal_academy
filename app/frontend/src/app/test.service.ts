import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TestService {

  constructor(private httpClient: HttpClient) {}
   GetAll() {
    return this.httpClient.get("http://academy-142-132-228-124.traefik.me/api/test");
  }
}
