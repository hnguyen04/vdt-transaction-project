import { createBrowserRouter } from 'react-router-dom';

import AdminPage from '@/pages/admin/index.page';
import AdminLayout from '@/pages/admin/layout';
import MyAccountPage from '@/pages/admin/settings/my-account/index.page';

import AuthLayout from '@/pages/auth/layout';
import LoginPage from '@/pages/auth/login.page';
import NotFoundPage from '@/pages/not-found.page';
import SystemAccountsPage from '@/pages/admin/system/accounts/page';
import SystemStaffsPage from '@/pages/admin/system/staffs/page';
import ChangePasswordPage from '@/pages/admin/settings/change-password/index.page';
import RegisterPage from '@/pages/auth/register.page';
import ComplainPage from '@/pages/admin/complaints/page';
import TransactionPage from '@/pages/admin/transactions/page';
import TransactionSinglePage from '@/pages/admin/transactions/[id]/page';

export const router = createBrowserRouter([
  {
    path: '',
    element: <AdminLayout />,
    children: [
      { path: '/', element: <AdminPage /> },
      {
        path: '/settings',
        children: [
          { path: 'my-account', element: <MyAccountPage /> },
          { path: 'change-password', element: <ChangePasswordPage /> },
        ],
      },
      {
        path: '/system',
        children: [
          { path: 'accounts', element: <SystemAccountsPage /> },
          { path: 'staffs', element: <SystemStaffsPage /> },
        ]
      },
      {
        path: '/complaints',
        element: <ComplainPage />
      },
      {
        path: '/transactions',
        element: <TransactionPage />
      },
      {
        path: '/transactions/:id',
        element: <TransactionSinglePage />
      },
    ],
  },
  {
    path: 'auth',
    element: <AuthLayout />,
    children: [
      { path: 'login', element: <LoginPage /> },
      { path: 'register', element: <RegisterPage /> },
    ],
  },
  { path: '/404', element: <NotFoundPage /> },
  { path: '*', element: <NotFoundPage /> },
]);
