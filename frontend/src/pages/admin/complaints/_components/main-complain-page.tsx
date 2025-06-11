import { ApprovalOutlined, CheckCircleOutline, NoteAddOutlined } from '@mui/icons-material';
import { GridColDef } from '@mui/x-data-grid';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { enqueueSnackbar } from 'notistack';
import { useMemo } from 'react';

import { TCrudFormField } from '@/base/crud-form-field.type';
import appService from '@/services/app/app.service';

import complainService from '../_services/complain.service';
import { ComplainStatusChip } from './complain-status-chip';
import { complainStatusOptions, EComplainStatus } from '../_services/complain.model';
import BaseCrudPage from '@/base/base-crud-page';
import useAuth from '@/hooks/use-auth';
import NiceModal from '@ebay/nice-modal-react';
import ComplainNoteModal from './complain-note-modal';


interface MainComplainPageProps {
    transactionId?: string;
    userId?: string;
    isUnresolve?: boolean;
}

const ComplainMainPage = ({ transactionId, userId, isUnresolve }: MainComplainPageProps) => {
    const queryClient = useQueryClient();
    const authQuery = useAuth();
    const { id } = authQuery.data || {}; 



    const { mutate: resolveMutation } = useMutation({
        mutationFn: (complainId: string) =>
            complainService.resolve({
                id: complainId,
                resolverId: id,
            }),
        onSuccess: () => {
            appService.hideLoadingModal();
            enqueueSnackbar('Giải quyết khiếu nại thành công', {
                variant: 'success',
            });
            queryClient.refetchQueries([
                'complain/getAll',
                {
                    page: 0,
                    pageSize: 10,
                    resolverId: userId,
                    transactionId: transactionId,
                },
            ]);
            queryClient.refetchQueries([
                'complain/getAllUnresolved',
                {
                    page: 0,
                    pageSize: 10,
                    resolverId: userId,
                    transactionId: transactionId,
                },
            ]);
        },
        onError: (err: any) => {
            appService.hideLoadingModal();
            enqueueSnackbar(err.response?.data?.message || 'Đã có lỗi xảy ra', {
                variant: 'error',
            });
        },
    });

    const { mutate: claimMutation } = useMutation({
        mutationFn: (complainId: string) =>
            complainService.claim({
                id: complainId,
                resolverId: id,
            }),
        onSuccess: () => {
            appService.hideLoadingModal();
            enqueueSnackbar('Giải quyết khiếu nại thành công', {
                variant: 'success',
            });
            queryClient.refetchQueries([
                'complain/getAll',
                {
                    page: 0,
                    pageSize: 10,
                    resolverId: userId,
                    transactionId: transactionId,
                },
            ]);
            queryClient.refetchQueries([
                'complain/getAllUnresolved',
                {
                    page: 0,
                    pageSize: 10,
                    resolverId: userId,
                    transactionId: transactionId,
                },
            ]);
        },
        onError: (err: any) => {
            appService.hideLoadingModal();
            enqueueSnackbar(err.response?.data?.message || 'Đã có lỗi xảy ra', {
                variant: 'error',
            });
        },
    });
    const tableColumns = useMemo<GridColDef[]>(
        () => [
            {
                field: 'id',
                headerName: 'ID',
                flex: 0.5,
                hide: true,
            },
            {
                field: 'transactionCode',
                headerName: 'Mã giao dịch',
                flex: 1,
                hide: true,
            },
            {
                field: 'userFullName',
                headerName: 'Tên người dùng',
                flex: 1,
            },
            {
                field: 'content',
                headerName: 'Nội dung khiếu nại',
                flex: 1.5,
            },
            {
                field: 'resolverFullName',
                headerName: 'Người giải quyết',
                flex: 1,
                hide: isUnresolve,
            },
            {
                field: 'timeSubmit ',
                headerName: 'Thời gian gửi',
                flex: 1,
                renderCell: (params) => {
                    return new Date(params.row.createdAt).toLocaleString('vi-VN', {
                        year: 'numeric',
                        month: '2-digit',
                        day: '2-digit',
                        hour: '2-digit',
                        minute: '2-digit',
                    });
                }
            },
            
            {
                field: 'status',
                headerName: 'Trạng thái',
                flex: 1,
                renderCell: (params) => {
                    return (
                        <ComplainStatusChip
                            status={params.row.status}
                        />
                    );
                },
            },
            {
                field: 'resolvingNote',
                headerName: 'Ghi chú giải quyết',
                flex: 1,

            },
            {
                field: 'createdAt',
                headerName: 'Ngày tạo',
                flex: 1,
                hide: true,
            },
            {
                field: 'updatedAt',
                headerName: 'Ngày cập nhật',
                flex: 1,
                hide: true,
            }
        ],
        [] // Add status to dependencies if needed,
    );

    const viewFields: TCrudFormField[] = useMemo(
        () => [
            {
                name: 'transactionCode',
                label: 'Mã giao dịch',
                type: 'text',
                colSpan: 6,
                readonly: true,
            },
            {
                name: "userName",
                label: 'Tên đăng nhập',
                type: 'text',
                colSpan: 6,
                readonly: true,
            },
            {
                name: 'userFullName',
                label: 'Tên người dùng',
                type: 'text',
                colSpan: 6,
                readonly: true,
            },
            {
                name: 'status',
                label: 'Trạng thái',
                type: 'select',
                colSpan: 6,
                options: complainStatusOptions,
                readonly: true,
            },
            {
                name: 'content',
                label: 'Nội dung khiếu nại',
                type: 'textarea',
                colSpan: 12,
                readonly: true,
            },
            {
                name: 'resolverName',
                label: 'Tài khoản giải quyết',
                colSpan: 6,
                type: 'text',

            },
            {
                name: 'resolverFullName',
                label: 'Người giải quyết',
                type: 'text',
                colSpan: 6,
                readonly: true,
            },
            {
                name: 'resolverPhoneNumber',
                label: 'SĐT',
                type: 'text',
                colSpan: 6,
                readonly: true,
            },
            {
                name: 'timeSubmit',
                label: 'Thời gian gửi',
                type: 'text',
                colSpan: 6,
                readonly: true,
            },
            {
                name: 'resolvingNote',
                label: 'Ghi chú từ người giải quyết',
                type: 'textarea',
                colSpan: 12,
                readonly: true,
            }
        ],
        []
    );

    const editFields: TCrudFormField[] = useMemo(
        () => [
            {
                name: 'content',
                label: 'Nội dung khiếu nại',
                type: 'textarea',
                colSpan: 12,
            },
        ],
        []
    )

    const filterFields: TCrudFormField[] = useMemo(
        () => [
            {
                name: 'status',
                label: 'Trạng thái',
                type: 'select',
                colSpan: 6,
                options: complainStatusOptions,
                defaultValue: isUnresolve ? complainStatusOptions.find(option => option.value === EComplainStatus.PENDING)?.value : undefined,
                readonly: isUnresolve,
            },
            {
                name: 'userFullName',
                label: 'Tên người dùng',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'resolverFullName',
                label: 'Người giải quyết',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'transactionCode',
                label: 'Mã giao dịch',
                type: 'text',
                colSpan: 6,
            },
            
        ],
        [EComplainStatus, complainStatusOptions, isUnresolve]
    );

    const extendActions = [
        {
            title: 'Lưu ghi chú',
            icon: <NoteAddOutlined/>,
            onClick: (_row: any) => {
                NiceModal.show(ComplainNoteModal, {
                    transactionId: transactionId,
                    resolverId: userId,
                    complainId: _row.id,
                })
            },
        },
        {
            title: 'Đánh dấu đã giải quyết',
            icon: <CheckCircleOutline color='success' />,
            onClick: (_row: any) => {
                resolveMutation(_row.id);
            },
        }
    ];

    if (isUnresolve) {
        extendActions.push({
            title: 'Nhận khiếu nại',
            icon: <ApprovalOutlined color='info'/>,
            onClick: (_row: any) => {
                claimMutation(_row.id);
            },
        });

    }


    return (
        <BaseCrudPage
            title={`Danh sách khiếu nại ${isUnresolve ? 'chưa giải quyết' : ''}`}
            unitName={'khiếu nại'}
            name="complain"
            service={complainService}
            columns={tableColumns}
            updateFields={editFields}
            viewFields={viewFields}
            filterFields={filterFields}
            hideExportExcelBtn
            hideImportExcelBtn
            hideDeleteManyBtn
            hideSelectRowCheckbox
            hideAddBtn
            getAllPath={ isUnresolve ? '/GetAllUnResolved' : '/GetAll'}
            updatePath='/Update'
            deletePath='/Delete'
            beautyView
            defaultGetAllParams={{
                transactionId: transactionId,
                resolverId: userId,
            }}

            extendActions={ extendActions }
        />
    );
};

export default ComplainMainPage;
