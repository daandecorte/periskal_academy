import { Injectable } from '@angular/core';

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
  selectedCertificatIds: number[] = []
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
