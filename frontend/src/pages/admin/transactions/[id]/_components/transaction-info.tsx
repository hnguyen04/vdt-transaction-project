import { Divider, Grid, Paper, Typography } from "@mui/material";
import InfoItem from "./info-item";
import {
    Numbers as NumbersIcon,
    MonetizationOn as MoneyIcon,
    AccountBalance as SourceIcon,
    SwapHoriz as DestIcon,
    AccessTime as TimeIcon,
    Badge as StatusIcon,
    ReceiptLong as TypeIcon,
    Business as BankIcon

} from '@mui/icons-material';
import { TransactionTypeChip } from "../../_components/transaction-type-chip";
import { TransactionStatusChip } from "../../_components/transaction-status-chip";

type TransactionInfoProps = {
    data: any;
};

export default function TransactionInfo({ data }: TransactionInfoProps) {
    return (
        <Paper elevation={3} className="p-6">
            <Typography variant="h6" color="primary" gutterBottom>Thông tin giao dịch</Typography>
            <Divider className="mb-4" />
            <Grid container spacing={3}>
                <InfoItem icon={NumbersIcon} label="Mã giao dịch" value={data?.code} />
                <InfoItem icon={MoneyIcon} label="Số tiền" value={`${data?.amount?.toLocaleString()} VND`} />
                <InfoItem icon={SourceIcon} label="Tài khoản nguồn" value={data?.sourceAccount} />
                <InfoItem icon={DestIcon} label="Tài khoản đích" value={data?.destinationAccount} />
                <InfoItem icon={TimeIcon} label="Thời gian tạo" value={data?.createdAt ? new Date(data.createdAt).toLocaleString() : ''} />
                <InfoItem icon={StatusIcon} label="Trạng thái" value={<TransactionTypeChip type={data?.type} />} />
                <InfoItem icon={TypeIcon} label="Loại giao dịch" value={<TransactionStatusChip status={data?.status} />} />
                <InfoItem icon={BankIcon} label="Ngân hàng" value={data?.bankCode} />

            </Grid>
        </Paper>
    );
}