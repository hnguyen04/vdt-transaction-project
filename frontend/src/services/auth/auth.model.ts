export enum ERole {
  USER = 'USER',
  ADMIN = 'ADMIN',
  STAFF = 'STAFF',
  SUPER_ADMIN = 'SUPER_ADMIN'
}

export interface ILoginInput {
  userName: string;
  password: string;
}

export interface ILoginResult {
  accessToken: string;
  refreshToken: string;
  expiresIn: any;
}

export interface IRegisterInput {
  userName: string;
  password: string;
  fullName: string;
  phoneNumber: string;
  cmnd: string;
  role: ERole;
}

export interface IRegisterResult {
  userId: string;
  message: string;
}

export interface GetUserInfoApiParams {
  accessToken: string;
}

export interface IUserInfo {
  id: string;
  userName: string;
  phoneNumber: string;
  cmnd: string;
  fullName: string;
  createdAt: string;
  updatedAt: string;
  role: ERole;
}

export interface IRefreshTokenRequest {
  accessToken: string;
  refreshToken: string;
}

export interface IRefreshTokenResult {
  accessToken: string;
  refreshToken: string;
  expiresIn: number;
}

export interface IAuthState {

  currentUser?: IUserInfo;
  isAuth: boolean;
}

export type TAuthActionType = 'logout' | 'setIsAuth' | 'setCurrentUser';
