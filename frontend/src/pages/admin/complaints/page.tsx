import BaseTabsPage from "@/base/base-tabs-page";
import ComplainMainPage from "./_components/main-complain-page";
import useAuth from "@/hooks/use-auth";

const ComplainPage = () => {
    const authQuery = useAuth();
    const { id } = authQuery.data || {}; 
    return (
        <>
            <BaseTabsPage
                title="Quản lý khiếu nại"
                name="transactionPage"
                tabs={[
                    {
                        label: 'Khiếu nại chưa giải quyết',
                        Component: <ComplainMainPage isUnresolve={true} />,
                    },
                    {
                        label: 'Khiếu nại của tôi',
                        Component: (
                            <ComplainMainPage userId={id} isUnresolve={false} />
                        ),
                    },
                ]}
            />
        </>
    )
}

export default ComplainPage;