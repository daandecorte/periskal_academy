import { Injectable } from '@angular/core';
import { Certificate } from '../certificates/select-module/select-module.component';

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
  selectedUserIds: number[] = []
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
