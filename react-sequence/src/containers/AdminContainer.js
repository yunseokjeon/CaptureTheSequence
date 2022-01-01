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
    Button, Link
} from "@material-ui/core";
import { signout } from "../service/ApiService";
import Header from "./Header.js";


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
            <Header/>
            <Container component="main" style={{ marginTop: "5%" }}>
                <Grid container spacing={2} columns={16}>
                    <Grid item xs={10}>
                        <Typography component="h5" variant="h5">Capture The Sequence</Typography>
                    </Grid>

                </Grid>
            {/* {JSON.stringify(users)} */}
            {users.map((user, i) => {
                return <DisplayUser user={user} key={i} />
            })}

            </Container>
        </div>
    )

}

export default AdminContainer;