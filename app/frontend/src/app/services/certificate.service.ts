import { Injectable } from '@angular/core';
import { Certificate } from '../certificates/select-training/select-training.component';
import { IUsers } from '../skippers/skippers.component';

@Injectable({
  providedIn: 'root'
})
export class CertificateService {
  public billingInfo: BillingInfo = {
    company: "",
    vat: "",
    street: "",
    city: "",
    postal: "",
    country: "",
    state: "",
    name: "",
    phone: "",
    email: "",
  };
  selectedCertificates: Certificate[] = []
  selectedUsers: IUsers[] = []
  constructor() { }
}
export interface BillingInfo {
  company: string;
  vat: string;
  street: string;
  city: string;
  postal: string;
  country: string;
  state: string;
  name: string;
  phone: string;
  email: string;
}
