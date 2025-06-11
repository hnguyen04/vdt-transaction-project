export enum ETransactionStatus {
    PENDING = 'PENDING',
    COMPLETED = 'SUCCESS',
    FAILED = 'FAILED',
}

export enum ETransactionType {
    DEPOSIT = 'DEPOSIT',
    WITHDRAW = 'WITHDRAW',
    TRANSFER = 'TRANSFER',
}

export const transactionStatusOptions = [
    { label: 'Đang chờ xử lý', value: ETransactionStatus.PENDING },
    { label: 'Hoàn tất', value: ETransactionStatus.COMPLETED },
    { label: 'Thất bại', value: ETransactionStatus.FAILED },
];

export const transactionTypeOptions = [
    { label: 'Nạp tiền', value: ETransactionType.DEPOSIT },
    { label: 'Rút tiền', value: ETransactionType.WITHDRAW },
    { label: 'Chuyển khoản', value: ETransactionType.TRANSFER },
];