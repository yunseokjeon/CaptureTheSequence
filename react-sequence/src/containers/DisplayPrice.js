import {
    TextField,
    Grid,
    Container,
    Typography,
} from "@material-ui/core";
import Switch from '@mui/material/Switch';

const DisplayPrice = (props) => {
    return (
        <Grid container spacing={2}>
            <Grid item xs={3}>
                <Typography>
                    {props.data.marketDate}
                </Typography>
            </Grid>

            <Grid item xs={3}>
                <Typography>
                    {props.data.itemName}
                </Typography>
            </Grid>

            <Grid item xs={3}>
                <Typography>
                    {props.data.startingPrice}
                </Typography>
            </Grid>

            <Grid item xs={3}>
                <Typography>
                    {props.data.closingPrice}
                </Typography>
            </Grid>
        </Grid>
    );

}

export default DisplayPrice;