import { yupResolver } from '@hookform/resolvers/yup';
import {
    Badge,
    DirectionsCar,
    Lock,
    Person,
    Phone,
    Visibility,
    VisibilityOff,
} from '@mui/icons-material';
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Grid,
    IconButton,
    InputAdornment,
    TextField,
    Typography,
} from '@mui/material';
import { useMutation } from '@tanstack/react-query';
import { enqueueSnackbar } from 'notistack';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { Link } from 'react-router-dom';
import * as yup from 'yup';

import appService from '@/services/app/app.service';
import { ERole } from '@/services/auth/auth.model';
import authService from '@/services/auth/auth.service';

type RegisterFormData = {
    userName: string;
    fullName: string;
    password: string;
    confirmPassword: string;
    phoneNumber: string;
    cmnd: string;
};

const schema = yup.object({
    userName: yup.string().required('Vui lòng nhập tên đăng nhập'),
    fullName: yup.string().required('Vui lòng nhập họ tên'),
    password: yup
        .string()
        .min(6, 'Mật khẩu phải có ít nhất 6 ký tự')
        .required('Vui lòng nhập mật khẩu'),
    confirmPassword: yup
        .string()
        .oneOf([yup.ref('password')], 'Mật khẩu nhập lại không khớp')
        .required('Vui lòng nhập lại mật khẩu'),
    phoneNumber: yup
        .string()
        .required('Vui lòng nhập số điện thoại')
        .matches(/^(0|\+84)[0-9]{9,10}$/, 'Số điện thoại không hợp lệ'),
    cmnd: yup
        .string()
        .required('Vui lòng nhập CMND/CCCD')
        .matches(/^\d{9,12}$/, 'CMND/CCCD phải từ 9 đến 12 chữ số'),
});

const RegisterPage = () => {
    const [showPassword, setShowPassword] = useState(false);

    const {
        register,
        handleSubmit,
        formState: { errors },
    } = useForm<RegisterFormData>({
        resolver: yupResolver(schema),
    });

    const { mutate } = useMutation({
        mutationFn: (data: any) => authService.register(data),
        onSuccess: () => {
            enqueueSnackbar('Đăng ký thành công', { variant: 'success' });
            setTimeout(() => {
                window.location.href = '/auth/login';
            }, 5000);
            appService.hideLoadingModal();
        },
        onError: (err: any) => {
            enqueueSnackbar(err?.response?.data?.message || 'Đã có lỗi xảy ra', {
                variant: 'error',
            });
            appService.hideLoadingModal();
        },
    });

    const onSubmit = (data: RegisterFormData) => {
        const finalData = {
            ...data,
            role: ERole.STAFF,
        };
        mutate(finalData);
        appService.showLoadingModal();
    };

    const toggleShowPassword = () => setShowPassword((prev) => !prev);

    return (
        <div className="relative flex items-center justify-center min-h-screen bg-gradient-to-br from-[#e5e8f0] via-[#cbd4e3] to-[#a3b1cb] px-4">
            <div className="absolute w-80 h-80 bg-[#15305a] opacity-10 rounded-full blur-3xl animate-pulse -z-10 top-10 left-10"></div>

            <Card className="w-full max-w-4xl shadow-xl rounded-2xl animate-fade-in transition-all duration-500 hover:shadow-2xl z-10">
                <CardContent>
                    <Box className="mb-8 text-center">
                        <Typography variant="h4" className="text-[#15305a] font-bold">
                            Đăng ký
                        </Typography>
                    </Box>

                    <form onSubmit={handleSubmit(onSubmit)}>
                        <Grid container spacing={2}>
                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Tên đăng nhập *"
                                    fullWidth
                                    {...register('userName')}
                                    error={!!errors.userName}
                                    helperText={errors.userName?.message}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Person className="text-[#15305a]" />
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Họ và tên *"
                                    fullWidth
                                    {...register('fullName')}
                                    error={!!errors.fullName}
                                    helperText={errors.fullName?.message}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Badge className="text-[#15305a]" />
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Số điện thoại *"
                                    fullWidth
                                    {...register('phoneNumber')}
                                    error={!!errors.phoneNumber}
                                    helperText={errors.phoneNumber?.message}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Phone className="text-[#15305a]" />
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="CMND/CCCD *"
                                    fullWidth
                                    {...register('cmnd')}
                                    error={!!errors.cmnd}
                                    helperText={errors.cmnd?.message}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <DirectionsCar className="text-[#15305a]" />
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Mật khẩu *"
                                    type={showPassword ? 'text' : 'password'}
                                    fullWidth
                                    {...register('password')}
                                    error={!!errors.password}
                                    helperText={errors.password?.message}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Lock className="text-[#15305a]" />
                                            </InputAdornment>
                                        ),
                                        endAdornment: (
                                            <InputAdornment position="end">
                                                <IconButton onClick={toggleShowPassword} edge="end">
                                                    {showPassword ? (
                                                        <VisibilityOff className="text-[#15305a]" />
                                                    ) : (
                                                        <Visibility className="text-[#15305a]" />
                                                    )}
                                                </IconButton>
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <TextField
                                    label="Nhập lại mật khẩu *"
                                    type={showPassword ? 'text' : 'password'}
                                    fullWidth
                                    {...register('confirmPassword')}
                                    error={!!errors.confirmPassword}
                                    helperText={errors.confirmPassword?.message}
                                    InputProps={{
                                        startAdornment: (
                                            <InputAdornment position="start">
                                                <Lock className="text-[#15305a]" />
                                            </InputAdornment>
                                        ),
                                    }}
                                />
                            </Grid>
                        </Grid>

                        <CardActions className="pt-6 mt-6">
                            <Button
                                type="submit"
                                variant="contained"
                                fullWidth
                                className="!bg-[#15305a] hover:!bg-[#1f3b80] text-white font-semibold py-2 rounded-lg transition-all duration-300"
                            >
                                Đăng ký
                            </Button>
                        </CardActions>
                    </form>
                </CardContent>

                <Box className="text-center text-sm text-gray-600 pb-5 px-4">
                    Đã có tài khoản?{' '}
                    <Link
                        to="/auth/login"
                        className="text-[#15305a] hover:underline font-medium"
                    >
                        Đăng nhập
                    </Link>
                </Box>
            </Card>
        </div>
    );
};

export default RegisterPage;
