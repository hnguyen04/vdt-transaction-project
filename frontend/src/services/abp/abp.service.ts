import { IBaseHttpResponse } from '@/base/base.model';
import { httpService } from '@/base/http-service';

import { IAbpConfiguration } from './abp.model';
import { IUserInfo } from '../auth/auth.model';

class AbpService {
  async getConfigurations() {
    const response = await httpService.request<
      IBaseHttpResponse<IAbpConfiguration>
    >({
      url: '/auth/users/GetUserConfiguration',
      method: 'GET',
    });

    return response.result;
  }

  async getCurLoginInfo() {
    const response = await httpService.request<
      IBaseHttpResponse<IUserInfo>
    >({
      url: '/auth/users/GetMyInfo',
      method: 'GET',
    });

    return response.result;
  }
}

export const abpService = new AbpService();
