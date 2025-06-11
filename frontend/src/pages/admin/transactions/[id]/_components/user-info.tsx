import { Paper, Grid, Typography, Divider } from '@mui/material';
import {
    Person as PersonIcon,
    Phone as PhoneIcon,
    Badge as BadgeIcon,
    AccountCircle as AccountIcon,
    CreditCard as CMNDIcon,
} from '@mui/icons-material';
import InfoItem from './info-item';

type UserInfoProps = {
    data: any;
};

export default function UserInfo({ data }: UserInfoProps) {
    return (
        <Paper elevation={3} className="p-6">
            <Typography variant="h6" color="primary" gutterBottom>Thông tin người giao dịch</Typography>
            <Divider className="mb-4" />
            <Grid container spacing={3}>
                <InfoItem icon={PersonIcon} label="Họ tên" value={data?.fullName} />
                <InfoItem icon={PhoneIcon} label="Số điện thoại" value={data?.phoneNumber} />
                <InfoItem icon={CMNDIcon} label="CMND" value={data?.cmnd} />
                <InfoItem icon={AccountIcon} label="Tên đăng nhập" value={data?.userName} />
                <InfoItem icon={BadgeIcon} label="Mã người dùng" value={data?.userCode} />
            </Grid>
        </Paper>
    );
}
