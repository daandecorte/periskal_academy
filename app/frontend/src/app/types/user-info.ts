export interface IUser {
  ID: string;
  Firstname: string;
  Lastname: string;
  Shipname: string;
  Role: string;
  Products: string[];
  LoginResult: string;
  Skippers: ISkipper[];
}

export interface ISkipper {
  ID: string;
  Username: string;
  Firstname: string;
  Lastname: string;
  Shipname: string;
  Role: string;
  Products: string[];
}
