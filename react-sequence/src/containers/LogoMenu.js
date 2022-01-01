import * as React from 'react';
import {Grid, Menu} from "@mui/material";
import MenuItem from '@mui/material/MenuItem';
import {signout} from "../service/ApiService";
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import Container from '@mui/material/Container';
import Typography from '@mui/material/Typography';
import IconButton from '@mui/material/IconButton';
import { useState } from "react";
import { Tabs, Tab } from "@material-ui/core";
import AccountCircle from '@mui/icons-material/AccountCircle';
import MenuList from '@mui/material/MenuList';
import { makeStyles } from "@material-ui/core/styles";
import Header from "./Header.js";


const LogoMenu = (props) => {

    const [auth, setAuth] = React.useState(true);
    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClose = () => {
        setAnchorEl(null);
    };

    const [index, setIndex] = useState(0);

    function changeTab(event, index) {
        setIndex(index);
    }

    const useStyles = makeStyles((theme) => ({
        root: {
            flexGrow: 1,
            backgroundColor: theme.palette.background.paper
        },
        leftAlign: {
            marginLeft: "auto"
        }
    }));
    const classes = useStyles();

    return (
        <div>
            <Header/>
                <Container maxWidth="xl">
                    <Tabs value={index} onChange={changeTab} className={classes.rightAlign}>
                        <Tab label="데이터업로드" onClick={props.fileViewCallback}/>
                        <Tab label="전략 시뮬레이션" onClick={props.simulationViewCallback}/>
                    </Tabs>
                </Container>
        </div>
    );
}

export default LogoMenu;