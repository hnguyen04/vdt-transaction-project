import { Chip } from '@mui/material';
import { ETransactionStatus } from '../_services/transaction.model';

interface StatusChipProps {
    status: ETransactionStatus | any;
}

const statusMap: Record<ETransactionStatus, { label: string; color: 'default' | 'primary' | 'secondary' | 'error' | 'success' | 'warning' }> = {
    [ETransactionStatus.PENDING]: { label: 'Đang chờ xử lý', color: 'warning' },
    [ETransactionStatus.COMPLETED]: { label: 'Hoàn tất', color: 'success' },
    [ETransactionStatus.FAILED]: { label: 'Thất bại', color: 'error' },
};

export const TransactionStatusChip: React.FC<StatusChipProps> = ({ status }) => {
    const { label, color } = statusMap[status as ETransactionStatus] || { label: 'Không xác định', color: 'default' };
    return <Chip label={label} color={color} size="small" />;
};
