import { useEffect, useRef, useState } from "react";

import {
    TextField,
    Grid,
    Container,
    Typography,
} from "@material-ui/core";
import Switch from '@mui/material/Switch';
import produce from "immer";
import { activateAccount } from "../service/ApiService";




const DisplayUser = (inputUser) => {
    const [user, setUser] = useState(inputUser.user);
    const [checked, setChecked] = useState(user.approved);


    console.log(user);

    const handleChange = (event) => {
        setChecked(event.target.checked);
        setUser(produce(user, draft => {
            draft.approved = event.target.checked
        }));
        activateAccount(user);
    };


    return (

        <Grid container spacing={2}>
            <Grid item xs={3}>
                <Typography>
                    {user.email}
                </Typography>
            </Grid>

            <Grid item xs={3}>
                <Typography variant="h6">
                    {user.username}
                </Typography>
            </Grid>

            <Grid item xs={4}>
                <Typography variant="h6">
                    {user.created_at.substring(0, 10)}
                </Typography>
            </Grid>

            <Grid item xs={2}>
                <Switch checked={checked}
                    onChange={handleChange} />
            </Grid>
        </Grid>
    )
}

export default DisplayUser;