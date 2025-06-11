import { BaseCrudService, TBaseResponse } from '@/base/base-crud-service';
import { httpService } from '@/base/http-service';

class ComplainService extends BaseCrudService {
    constructor() {
        super('/complain/complains');
    }

    public async assign(
        data: any,
        path = '/Assign',
    ): Promise<TBaseResponse<any>> {
        const response = await httpService.request<TBaseResponse<any>>({
            url: `${this.basePath}${path}`,
            method: 'POST',
            data,
        });
        return response;
    }


    public async claim(
        data: any,
        path = '/Claim',
    ): Promise<TBaseResponse<any>> {
        const response = await httpService.request<TBaseResponse<any>>({
            url: `${this.basePath}${path}`,
            method: 'POST',
            data,
        });
        return response;
    }

    public async note(
        data: any,
        path = '/Note',
    ): Promise<TBaseResponse<any>> {
        const response = await httpService.request<TBaseResponse<any>>({
            url: `${this.basePath}${path}`,
            method: 'POST',
            data,
        });
        return response;
    }

    public async resolve(
        data: any,
        path = '/Resolve',
    ): Promise<TBaseResponse<any>> {
        const response = await httpService.request<TBaseResponse<any>>({
            url: `${this.basePath}${path}`,
            method: 'POST',
            data,
        });
        return response;
    }

}

const complainService = new ComplainService();

export default complainService;
