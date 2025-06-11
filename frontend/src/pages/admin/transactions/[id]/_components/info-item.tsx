import { Grid, Stack, Typography } from "@mui/material";

import { SvgIconProps } from "@mui/material";

type InfoItemProps = {
    icon: React.ComponentType<SvgIconProps>;
    label: string;
    value?: any;
};

const InfoItem: React.FC<InfoItemProps> = ({ icon: Icon, label, value }) => (
    <Grid item xs={12} sm={6} sx={{ mt: 2 }}>
        <Stack direction="row" spacing={2} alignItems="center">
            <Icon color="primary" />
            <Stack>
                <Typography variant="body2" color="textSecondary">{label}</Typography>
                <Typography variant="subtitle1">{value || '—'}</Typography>
            </Stack>
        </Stack>
    </Grid>
);

export default InfoItem;
