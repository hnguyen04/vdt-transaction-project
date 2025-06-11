import { CancelOutlined, ApprovalOutlined, DetailsOutlined } from '@mui/icons-material';
import { GridColDef } from '@mui/x-data-grid';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { enqueueSnackbar } from 'notistack';
import { useMemo } from 'react';

import BaseCrudPage from '@/base/base-crud-page';
import { TCrudFormField } from '@/base/crud-form-field.type';
import appService from '@/services/app/app.service';
import transactionService from '../_services/transaction.service';
import { TransactionTypeChip } from './transaction-type-chip';

import { ETransactionStatus, ETransactionType, transactionStatusOptions, transactionTypeOptions } from '../_services/transaction.model';
import { TransactionStatusChip } from './transaction-status-chip';
import { useNavigate } from 'react-router-dom';

interface MainTransactionPageProps {
    status: ETransactionStatus | undefined;
}

const TransactionMainPage = ({ status }: MainTransactionPageProps) => {
    const queryClient = useQueryClient();
    const navigate = useNavigate();

    const { mutate: approveMutation } = useMutation({
        mutationFn: (id: string) =>
            transactionService.approve({
                id: id,
            }),
        onSuccess: () => {
            appService.hideLoadingModal();
            enqueueSnackbar('Duyệt thành công', {
                variant: 'success',
            });
            queryClient.refetchQueries([
                'transaction/getAll',
                {
                    status: status,
                    page: 0,
                    pageSize: 10,
                },
            ]);
            queryClient.refetchQueries([
                'transaction/getAll',
                {
                    status: ETransactionStatus.COMPLETED,
                    page: 0,
                    pageSize: 10,
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

    const { mutate: rejectMutation } = useMutation({
        mutationFn: (id: string) =>
            transactionService.reject({
                id: id,
            }),
        onSuccess: () => {
            appService.hideLoadingModal();
            enqueueSnackbar('Đã từ chối giao dịch', {
                variant: 'success',
            });
            queryClient.refetchQueries([
                'transaction/getAll',
                {
                    status: status,
                    page: 0,
                    pageSize: 10,
                },
            ]);
            queryClient.refetchQueries([
                'transaction/getAll',
                {
                    status: ETransactionStatus.FAILED,
                    page: 0,
                    pageSize: 10,
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
                field: 'userId',
                headerName: 'ID người dùng',
                flex: 1,
                hide: true,
            },
            {
                field: 'code',
                headerName: 'Mã giao dịch',
                flex: 1,
            },
            {
                field: 'fullName',
                headerName: 'Tên người dùng',
                flex: 1,
            },
            {
                field: 'commitTime',
                headerName: 'Thời gian giao dịch',
                flex: 1,
                renderCell: (params) => {
                    return (
                        <span>
                            {new Date(params.row.createdAt).toLocaleString('vi-VN', {
                                dateStyle: 'short',
                                timeStyle: 'short',
                            })}
                        </span>
                    );
                },
            },
            {
                field: 'amount',
                headerName: 'Số tiền',
                flex: 1,
                renderCell: (params) => {
                    return (
                        <span>
                            {params.row.amount.toLocaleString('vi-VN', {
                                style: 'currency',
                                currency: 'VND',
                            })}
                        </span>
                    );
                },
            },
            {
                field: 'type',
                headerName: 'Loại giao dịch',
                flex: 1,
                renderCell: (params) => {
                    return (
                        <TransactionTypeChip
                            type={params.row.type}
                        />
                    );
                },
            },
            {
                field: 'status',
                headerName: 'Trạng thái',
                flex: 1,
                renderCell: (params) => {
                    return (
                        <TransactionStatusChip
                            status={params.row.status}
                        />
                    );
                },
            },
            {
                field: 'userName',
                headerName: 'Tài khoản người dùng',
                flex: 1,
                hide: true,
            },
            {
                field: 'phoneNumber',
                headerName: 'Số điện thoại',
                flex: 1,
                hide: true,
            },
            {
                field: 'cmnd',
                headerName: 'CMND/CCCD',
                flex: 1,
                hide: true,
            },
            {
                field: 'userCode',
                headerName: 'Mã người dùng',
                flex: 1,
                hide: true,
            },
            {
                field: 'sourceAccount',
                headerName: 'Tài khoản nguồn',
                flex: 1,
                hide: true,
            },
            {
                field: 'destinationAccount',
                headerName: 'Tài khoản đích',
                flex: 1,
                hide: true,
            },
            {
                field: 'bankCode',
                headerName: 'Mã ngân hàng',
                flex: 1,
                hide: true,
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
        [status, ETransactionStatus, ETransactionType] // Add status to dependencies if needed,
    );

    const editFields: TCrudFormField[] = useMemo(
        () => [
            {
                name: 'amount',
                label: 'Số tiền',
                type: 'number',
                colSpan: 12,
            },
            {
                name: 'status',
                label: 'Trạng thái',
                type: 'select',
                colSpan: 12,
                options: transactionStatusOptions
            }
        ],
        [
            transactionStatusOptions
        ]
    )

    const filterFields: TCrudFormField[] = useMemo(
        () => [
            {
                name: 'status',
                label: 'Trạng thái',
                type: 'select',
                colSpan: 6,
                options: transactionStatusOptions,
                defaultValue: status,
                readOnly: status !== undefined
            },
            {
                name: 'type',
                label: 'Loại giao dịch',
                type: 'select',
                colSpan: 6,
                options: transactionTypeOptions
            },
            {
                name: 'code',
                label: 'Mã giao dịch',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'userName',
                label: 'Tài khoản người dùng',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'cmnd',
                label: 'CMND/CCCD',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'userCode',
                label: 'Mã người dùng',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'phoneNumber',
                label: 'Số điện thoại',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'sourceAccount',
                label: 'Tài khoản nguồn',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'destinationAccount',
                label: 'Tài khoản đích',
                type: 'text',
                colSpan: 6,
            },
            {
                name: 'bankCode',
                label: 'Mã ngân hàng',
                type: 'text',
                colSpan: 6,
            }
        ],
        [
            transactionStatusOptions,
            transactionTypeOptions
        ]
    )

    const extendActions = [
        {
            title: 'Xem chi tiết',
            icon: <DetailsOutlined />,
            onClick: (_row: any) => {
                navigate(`/transactions/${_row.id}`);
            },
        }
    ];

    if (status === ETransactionStatus.PENDING) {
        extendActions.push({
            title: 'Phê duyệt',
            icon: <ApprovalOutlined color='success'/>,
            onClick: (_row: any) => {
                approveMutation(_row.id);
            },
        });

        extendActions.push({
            title: 'Từ chối',
            icon: <CancelOutlined color='error' />,
            onClick: (_row: any) => {
                rejectMutation(_row.id);
            },
        });
    }


    return (
        <BaseCrudPage
            title={transactionStatusOptions.find(option => option.value === status)?.label || 'Tất cả'}
            unitName={'giao dịch'}
            name="transaction"
            service={transactionService}
            columns={tableColumns}
            updateFields={editFields}
            filterFields={filterFields}
            hideExportExcelBtn
            hideImportExcelBtn
            hideDeleteManyBtn
            hideSelectRowCheckbox
            hideAddBtn
            hideViewAction={true}
            getAllPath="/GetAll"
            updatePath='/Update'
            deletePath='/Delete'
            beautyView
            defaultGetAllParams={{
                status: status,
            }}

            extendActions={ extendActions }
        />
    );
};

export default TransactionMainPage;
