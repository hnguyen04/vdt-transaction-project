import axios from 'axios';
import Cookies from 'js-cookie';

import { IBaseHttpResponse } from '@/base/base.model';
import { httpService } from '@/base/http-service';
import { API_ENDPOINT } from '@/configs/constant.config';

import {
  ILoginInput,
  ILoginResult,
  IRefreshTokenResult,
  IRegisterInput,
  IRegisterResult,
  IUserInfo,
} from './auth.model';

class AuthService {
  async login(input: ILoginInput) {
    const response = await axios.post<IBaseHttpResponse<ILoginResult>>(
      `${API_ENDPOINT}/auth/auth/login`,
      input,
    );

    const data = response.data.result;

    Cookies.set('accessToken', data.accessToken);
    Cookies.set('refreshToken', data.refreshToken);

    return this.getUserInfo();
  }

  async getUserInfo() {
    const accessToken = Cookies.get('accessToken');
    if (!accessToken) {
      throw new Error('Access token is required');
    }

    const response = await httpService.request<IBaseHttpResponse<IUserInfo>>({
      url: '/auth/users/GetMyInfo',
      method: 'GET',
    });

    return response.result;
  }

  async refreshToken() {
    try {
      const refreshToken = Cookies.get('refreshToken');
      const response = await axios.post<IBaseHttpResponse<IRefreshTokenResult>>(
        `${API_ENDPOINT}/auth/auth/refresh`,
        { refreshToken : refreshToken }
      );

      const data = response.data.result;

      Cookies.set('accessToken', data.accessToken);
      Cookies.set('refreshToken', data.refreshToken);

      return true;
    } catch (error) {
      Cookies.remove('accessToken');
      Cookies.remove('refreshToken');
      window.location.href = '/auth/login';
      return false;
    }
  }

  async logout() {
    try {
      const token = Cookies.get('accessToken');
      console.log('token', token);
      const response = await axios.post<IBaseHttpResponse<null>>(
        `${API_ENDPOINT}/auth/auth/logout`,
        {
          token: token,
        }
      );

      return response.data.result;
    } catch (error) {
      return Promise.reject(error);
    } finally {
      Cookies.remove('accessToken');
      Cookies.remove('refreshToken');
      window.location.href = '/auth/login';
    }
  }

  async register(input: IRegisterInput) {
    const response = await axios.post<IBaseHttpResponse<IRegisterResult>>(
      `${API_ENDPOINT}/auth/auth/register`,
      input,
    );
    const data = response.data.result;

    return data;
  }
}

const authService = new AuthService();

export default authService;
