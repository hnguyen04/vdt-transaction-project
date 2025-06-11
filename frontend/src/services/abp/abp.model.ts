import { ERole, IUserInfo } from "../auth/auth.model";

export interface IAbpConfiguration {
  role: ERole;
  auth: {
    allPermissions: any;
    grantedPermissions: any;
  };
}

export interface IAbpState {
  permissions: string[];
  curLoginInfo?: IUserInfo;
}


export type TAbpActionType = 'setPermissions' | 'setCurLoginInfo';
