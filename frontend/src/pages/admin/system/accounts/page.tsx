import NiceModal from '@ebay/nice-modal-react';
import { RefreshTwoTone } from '@mui/icons-material';
import { GridColDef } from '@mui/x-data-grid';
import { useMemo } from 'react';

import BaseCrudPage from '@/base/base-crud-page';
import { TCrudFormField } from '@/base/crud-form-field.type';
import useTranslation from '@/hooks/use-translation';

import ChangePassModal from './_components/change-pass-modal';
import accountsService from './_services/accounts.service';
import { hashUUIDTo8Char } from '@/services/utils';
import { ERole, roleOptions } from '@/services/auth/auth.model';

const SystemAccountsPage = () => {
  const { t } = useTranslation();

  const columns: GridColDef[] = useMemo(
    () => [
      {
        headerName: t('ID'),
        field: 'id',
        type: 'number',
        width: 300,
        flex: 1,
        editable: false,
        hide: true,
      },
      {
        field: 'code',
        headerName: 'Mã người dùng',
        type: 'string',
        width: 150,
        editable: false,
      },
      {
        field: 'userName',
        headerName: t('accounts_userName'),
        width: 150,
        type: 'string',
        editable: false,
      },
      {
        field: 'fullName',
        headerName: t('accounts_fullName'),
        type: 'string',
        editable: false,
        flex: 2,
      },
      {
        field: 'phoneNumber',
        headerName: 'Số điện thoại',
        type: 'string',
        width: 150,
        editable: false,
      },
      {
        field: 'cmnd',
        headerName: 'CMND/CCCD',
        type: 'string',
        width: 150,
        editable: false,
      },
      {
        field: 'role',
        headerName: t('Vai trò'),
        width: 200,
        renderCell: (params) => {
          const role = roleOptions.find((role) => role.value === params.row.role);
          return role ? role.label : 'Chưa xác định';
        },
        editable: false,
      }
    ],
    [t, roleOptions],
  );

  const viewFields = useMemo<TCrudFormField[]>(
    () => [
      {
        name: 'id',
        label: t('ID'),
        type: 'text',
        colSpan: 6,
        readOnly: true,
        formatValue: (value) => hashUUIDTo8Char(value),
      },
      {
        name: 'userName',
        label: t('accounts_userName'),
        type: 'text',
        required: true,
        colSpan: 6,
      },
      {
        name: 'fullName',
        label: t('accounts_fullName'),
        type: 'text',
        required: true,
        colSpan: 6,
      },
      {
        name: 'phoneNumber',
        label: 'Số điện thoại',
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
        name: 'role',
        label: t('Vai trò'),
        type: 'select',
        options: roleOptions,
        colSpan: 6,
      },
    ],
    [t, roleOptions],
  );

  const filterFields = useMemo<TCrudFormField[]>(() => [
    {
      name: 'userName',
      label: t('accounts_userName'),
      type: 'text',
      colSpan: 6,
    },
    {
      name: 'fullName',
      label: t('accounts_fullName'),
      type: 'text',
      colSpan: 6,
    },
    {
      name: 'role',
      label: t('Vai trò'),
      type: 'select',
      options: roleOptions,
      colSpan: 6,
    }
  ], [roleOptions, t]);


  return (
    <BaseCrudPage
      title={'Quản lý người dùng'}
      name="accounts"
      unitName=""
      service={accountsService}
      columns={columns}
      hideSelectRowCheckbox
      beautyView
      beautyViewFormWidth="sm"
      hideEditAction
      viewFields={viewFields}
      filterFields={filterFields}
      formWidth="lg"
      hideAddBtn={false}
      hideDeleteManyBtn={false}
      hideExportExcelBtn={true}
      hideImportExcelBtn={true}
      hasCustomActions={false}
      hideSearchInput={true}
      defaultGetAllParams={
        {
          role: ERole.USER,
        }
      }
      extendActions={[
        {
          icon: <RefreshTwoTone color="primary" />,
          title: t('Đổi mật khẩu'),
          onClick: (params) => {
            NiceModal.show(ChangePassModal, {
              row: params.row,
              mode: 'changepass',
              title: t('Đổi mật khẩu'),
            });
          },
        },
      ]}
    />
  );
};
export default SystemAccountsPage;
