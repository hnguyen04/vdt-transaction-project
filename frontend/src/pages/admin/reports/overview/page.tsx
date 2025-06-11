import useTranslation from "@/hooks/use-translation";
import { Box, Grid, Typography } from "@mui/material";
import { styled } from '@mui/material/styles';
import useAbp from "@/hooks/use-abp";


const ReportPage = () => {
    const { t } = useTranslation();
    const { abpQuery } = useAbp();

    return (
        <>
            <StyledMain component="main">
                <Typography variant="h5" component="h1">
                    {t('Thống kê')}
                </Typography>

            </StyledMain>
        </>
    )
}

const StyledMain = styled(Box)(({ theme }) => ({
    flex: 1,
    padding: theme.spacing(2),
    paddingLeft: theme.spacing(4),
    paddingRight: theme.spacing(4),
}));

export default ReportPage;