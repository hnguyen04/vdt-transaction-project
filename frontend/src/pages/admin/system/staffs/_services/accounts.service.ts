import { BaseCrudService, TBaseResponse } from '@/base/base-crud-service';
import { httpService } from '@/base/http-service';



class AccountsService extends BaseCrudService {
  constructor() {
    super('/auth/users');
  }
  public async resetPassword<T>(data: any, path = '/ResetPassword') {
    const res = await httpService.request<TBaseResponse<T>>({
      method: 'PUT',
      url: `${this.basePath}${path}`,
      data,
    });

    return res.result;
  }

}

const accountsService = new AccountsService();

export default accountsService;
