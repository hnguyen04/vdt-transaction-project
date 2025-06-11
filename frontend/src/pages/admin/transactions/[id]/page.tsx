import { useNavigate, useParams } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import BaseTabsPage from '@/base/base-tabs-page';
import transactionService from '../_services/transaction.service';
import TransactionInfo from './_components/transaction-info';
import UserInfo from './_components/user-info';
import { Button, Stack } from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import ComplainMainPage from '../../complaints/_components/main-complain-page';

const TransactionSinglePage = () => {
    const navigate = useNavigate();
    const { id } = useParams<{ id: string }>();

    const { data: transactionInfo } = useQuery({
        queryFn: () => transactionService.getOne(id as string),
        queryKey: ['transaction/getById', id]
    });

    return (
        <>
            <Stack direction="row" alignItems="center" className="mt-4 mb-2">
                <Button
                    variant="text"
                    startIcon={<ArrowBackIcon />}
                    onClick={() => navigate(-1)}
                    sx={{ padding: 0, minWidth: 'auto', color: '#1e40af' }} // Tailwind: text-blue-800
                >
                    Quay lại
                </Button>
            </Stack>

            <BaseTabsPage
                title="Chi tiết"
                name="transactionPage"
                tabs={[
                    {
                        label: 'Thông tin giao dịch',
                        Component: <TransactionInfo data={transactionInfo} />
                    },
                    {
                        label: 'Người giao dịch',
                        Component: <UserInfo data={transactionInfo} />
                    },
                    {
                        label: 'Khiếu nại',
                        Component: <ComplainMainPage transactionId={id} isUnresolve={false}/>
                    },
                ]}
            />
        </>
    );
};

export default TransactionSinglePage;
