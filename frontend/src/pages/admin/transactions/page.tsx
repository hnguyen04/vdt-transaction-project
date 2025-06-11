import BaseTabsPage from '@/base/base-tabs-page';
import TransactionMainPage from './_components/main-transaction-page';
import { ETransactionStatus } from './_services/transaction.model';



const TransactionPage = () => {
    return (
        <>
            <BaseTabsPage
                title="Quản lý giao dịch"
                name="transactionPage"
                tabs={[
                    {
                        label: 'Tất cả',
                        Component: <TransactionMainPage status={undefined} />,
                    },
                    {
                        label: 'Chờ xử lý',
                        Component: (
                            <TransactionMainPage status={ETransactionStatus.PENDING} />
                        ),
                    },
                    {
                        label: 'Đã hoàn thành',
                        Component: (
                            <TransactionMainPage status={ETransactionStatus.COMPLETED} />
                        ),
                    },
                    {
                        label: 'Thất bại',
                        Component: (
                            <TransactionMainPage status={ETransactionStatus.FAILED} />
                        ),
                    },
                ]}
            />
        </>
    );
};

export default TransactionPage;
