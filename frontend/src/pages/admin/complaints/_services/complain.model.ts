export enum EComplainStatus {
    PENDING = 'PENDING',
    RESOLVED = 'RESOLVED',
}

export const complainStatusOptions = [
    { label: 'Đang chờ xử lý', value: EComplainStatus.PENDING },
    { label: 'Đã xử lý', value: EComplainStatus.RESOLVED},
];