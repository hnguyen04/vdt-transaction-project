import { Chip, ChipProps } from '@mui/material';
import { ETransactionType } from '../_services/transaction.model';

interface TypeChipProps {
    type: ETransactionType | any;
}

const typeMap: Record<ETransactionType, { label: string; color: ChipProps['color'] }> = {
    [ETransactionType.DEPOSIT]: { label: 'Nạp tiền', color: 'success' },
    [ETransactionType.WITHDRAW]: { label: 'Rút tiền', color: 'error' },
    [ETransactionType.TRANSFER]: { label: 'Chuyển khoản', color: 'info' },
};

export const TransactionTypeChip: React.FC<TypeChipProps> = ({ type }) => {
    const { label, color } = type !== undefined && type in typeMap
        ? typeMap[type as ETransactionType]
        : { label: 'Không xác định', color: 'default' as ChipProps['color'] };

    return (
        <Chip
            label={label}
            color={color}
            size="small"
            variant="outlined"
        />
    );
};
