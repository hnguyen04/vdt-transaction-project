import { BaseCrudService, TBaseResponse } from '@/base/base-crud-service';
import { httpService } from '@/base/http-service';

class TransactionService extends BaseCrudService {
    constructor() {
        super('/transaction/transactions/');
    }

    public async approve(
        data: any,
        path = '/Approve',
    ): Promise<TBaseResponse<any>> {
        const response = await httpService.request<TBaseResponse<any>>({
            url: `${this.basePath}${path}`,
            method: 'POST',
            data,
        });
        return response;
    }

    public async reject(
        data: any,
        path = '/Approve',
    ): Promise<TBaseResponse<any>> {
        const response = await httpService.request<TBaseResponse<any>>({
            url: `${this.basePath}${path}`,
            method: 'POST',
            data,
        });
        return response;
    }

}

const transactionService = new TransactionService();

export default transactionService;
