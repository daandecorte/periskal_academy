import { Injectable } from '@angular/core';
import { IUser } from './types/user-info';

@Injectable({
  providedIn: 'root',
})
export class UserInfoService {
  private _user: IUser | null = null;

  constructor() {}

  public get user(): IUser | null {
    return this._user;
  }

  public set user(value: IUser) {
    this._user = value;
  }
}
