import { SettingsBackupRestoreSharp } from "@material-ui/icons";
import React, { useLayoutEffect, useEffect, useState, useRef } from "react";
import { useResolvedPath } from "react-router";
import useFetchUsers from "../service/useFetchUsers";
import DisplayUser from "./DisplayUser";
import {
    TextField,
    Grid,
    Container,
    Typography,
    Swith,
    Button
} from "@material-ui/core";
import { signout } from "../service/ApiService";


const AdminContainer = () => {

    if (localStorage.getItem('SequenceEmail') !== "yun@hello.com") {
        window.location.href = "/";
    }

    const { loading, users, error } = useFetchUsers();

    /*
    approved: false
    created_at: "2021-11-27T12:16:34.142664"
    email: "init1@hello.com"
    id: "40285bee7d5f64fb017d5f64fef10001"
    password: null
    token: null
    userCategory: "GENERAL"
    username: "init_1"
    */

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error!</div>;

    return (
        <div>
            <Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>
                <Grid container spacing={2} columns={16}>
                    <Grid item xs={2}>
                        <Typography component="h5" variant="h5">CAPTURE THE SEQUENCE </Typography>
                    </Grid>

                    <Grid item xs={8}></Grid>

                    <Grid item xs={2}><Button
                        fullWidth
                        variant="contained"
                        color="primary"
                        onClick={signout}>로그아웃</Button></Grid>
                </Grid>
            </Container>
            {/* {JSON.stringify(users)} */}
            {users.map((user, i) => {
                return <DisplayUser user={user} key={i} />
            })}
        </div>
    )

}

export default AdminContainer;