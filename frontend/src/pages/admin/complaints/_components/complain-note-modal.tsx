import NiceModal, { muiDialogV5, useModal } from '@ebay/nice-modal-react';
import { yupResolver } from '@hookform/resolvers/yup';
import {
    Box,
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
} from '@mui/material';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { enqueueSnackbar } from 'notistack';
import { useForm } from 'react-hook-form';
import * as yup from 'yup';

import BaseFormInput from '@/base/base-form-input';
import appService from '@/services/app/app.service';
import complainService from '../_services/complain.service';


type TComplainNoteModalProps = {
    complainId: string;
    resolverId?: string;
    transactionId?: string;
};

type FormData = {
    note: string;
};

// ✅ Schema kiểm tra bằng Yup
const schema = yup.object({
    note: yup.string().required('Vui lòng nhập ghi chú'),
});

const ComplainNoteModal = NiceModal.create(
    (props: TComplainNoteModalProps) => {
        const modal = useModal();
        const queryClient = useQueryClient();


        const {
            control,
            handleSubmit,
            formState: { isValid },
        } = useForm<FormData>({
            defaultValues: {
                note: '',
            },
            resolver: yupResolver(schema),
        });

        const { mutate, isLoading: confirmLoading } = useMutation({
            mutationFn: (note: string) =>
                complainService.note({
                    id: props.complainId,
                    note: note,
                }),
            onSuccess: () => {
                appService.hideLoadingModal();
                enqueueSnackbar('Tạo ghi chú thành công', {
                    variant: 'success',
                });
                queryClient.refetchQueries([
                    'complain/getAll',
                    {
                        page: 0,
                        pageSize: 10,
                        resolverId: props.resolverId,
                        transactionId: props.transactionId,
                    },
                ]);
                queryClient.refetchQueries([
                    'complain/getAllUnresolved',
                    {
                        page: 0,
                        pageSize: 10,
                        resolverId: props.resolverId,
                        transactionId: props.transactionId,
                    },
                ]);
                modal.hide();
            },
            onError: (err: any) => {
                appService.hideLoadingModal();
                enqueueSnackbar(err.response?.data?.message || 'Đã có lỗi xảy ra', {
                    variant: 'error',
                });
            },
        });

        const onSubmit = (data: FormData) => {
            appService.showLoadingModal();
            mutate(data.note);
        };

        return (
            <Dialog {...muiDialogV5(modal)} fullWidth maxWidth="sm">
                <DialogTitle>{'Lưu ghi chú'}</DialogTitle>

                <DialogContent>
                    <Box sx={{ mt: 2 }}>
                        <BaseFormInput
                            control={control}
                            field={{
                                name: 'note',
                                label: 'Ghi chú',
                                required: true,
                                type: 'textarea',
                                colSpan: 12,
                            }}
                        />
                    </Box>
                </DialogContent>

                <DialogActions>
                    <Button onClick={modal.hide} color="secondary">
                        {'Hủy'}
                    </Button>
                    <Button
                        onClick={handleSubmit(onSubmit)}
                        variant="contained"
                        color="primary"
                        disabled={!isValid || confirmLoading} // trong nút Submit
                        startIcon={confirmLoading && <CircularProgress size={20} />}
                    >
                        {'Xác nhận'}
                    </Button>
                </DialogActions>
            </Dialog>
        );
    },
);

ComplainNoteModal.displayName = 'ComplainNoteModal';

export default ComplainNoteModal;
