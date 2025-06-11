import { Chip } from '@mui/material';

export enum EComplainStatus {
    PENDING = 'PENDING',
    RESOLVED = 'RESOLVED',
}

interface StatusChipProps {
    status: EComplainStatus | any;
}

const statusMap: Record<EComplainStatus, { label: string; color: 'default' | 'primary' | 'secondary' | 'error' | 'success' | 'warning' }> = {
    [EComplainStatus.PENDING]: { label: 'Đang chờ xử lý', color: 'warning' },
    [EComplainStatus.RESOLVED]: { label: 'Đã xử lý', color: 'success' },
};

export const ComplainStatusChip: React.FC<StatusChipProps> = ({ status }) => {
    const { label, color } = statusMap[status as EComplainStatus] || {
        label: 'Không xác định',
        color: 'default',
    };

    return <Chip label={label} color={color} size="small" />;
};
