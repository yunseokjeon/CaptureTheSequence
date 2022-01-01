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
import AccountCircle from '@mui/icons-material/AccountCircle';
import MenuList from '@mui/material/MenuList';
import { makeStyles } from "@material-ui/core/styles";
import {Link} from "@material-ui/core";

const Header = (props) => {

    const [auth, setAuth] = React.useState(true);
    const handleMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const [anchorEl, setAnchorEl] = React.useState(null);

    const handleClose = () => {
        setAnchorEl(null);
        window.location.href = "/admin";
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
            <AppBar position="static">
                <Container maxWidth="xl">
                    <Toolbar disableGutters>
                        <Grid container direction="row">
                            <Grid item xs={11.5}>
                                <Link href="/" to="first" style={{ textDecoration: 'none', color: 'inherit'}}>
                                <Typography
                                    variant="h6"
                                    noWrap
                                    component="div"
                                    sx={{mr: 2, display: {xs: 'none', md: 'flex'}}}
                                >
                                    Capture The Sequence
                                </Typography>
                                </Link>
                            </Grid>
                            {auth && (
                                <Grid item xs={0.5} alignItems="flex-end">
                                    <IconButton
                                        size="large"
                                        aria-label="account of current user"
                                        aria-controls="menu-appbar"
                                        aria-haspopup="true"
                                        onClick={handleMenu}
                                        color="inherit"
                                    >
                                        <AccountCircle />
                                    </IconButton>
                                    <Menu
                                        id="menu-appbar"
                                        anchorEl={anchorEl}
                                        keepMounted
                                        anchorOrigin={{
                                            vertical: 'top',
                                            horizontal: 'right',
                                        }}

                                        transformOrigin={{
                                            vertical: 'top',
                                            horizontal: 'right',
                                        }}
                                        open={Boolean(anchorEl)}
                                        onClose={handleClose}
                                    >
                                        <MenuList >
                                            <MenuItem onClick={handleClose}>Admin</MenuItem>
                                            <MenuItem label="로그아웃" onClick={signout}>Logout</MenuItem>
                                        </MenuList>
                                    </Menu>
                                </Grid>
                            )}
                        </Grid>
                    </Toolbar>
                </Container>
            </AppBar>
        </div>
    );
}

export default Header;