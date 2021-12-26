import React from "react";
import './LoginContainer.css';
import { signin } from "../service/ApiService";
import { connect } from 'react-redux';
import { login, logout } from '../modules/authentication';

import {
    Link,
    Button,
    TextField,
    Grid,
    Container,
    Typography,
} from "@material-ui/core";

const handleSubmit = (event) => {
    console.log("handleSubmit");
    event.preventDefault();
    const data = new FormData(event.target);
    const email = data.get("email");
    const password = data.get("password");
    // ApiService의 signin 메서드를 사용 해 로그인.
    signin({ email: email, password: password });

}

const LoginContainer = () => {

    if (localStorage.getItem('LOGIN_STATUS') === true) {
        window.location.href = "/";
    }


    return (

        <Container component="main" maxWidth="xs" style={{ marginTop: "10%" }} fixed>

            <Grid container spacing={5} direction="column">
                <Grid item xs={12}>
                    <Typography component="h5" variant="h5">CAPTURE THE SEQUENCE </Typography>
                </Grid>

                <Grid item xs={10}>
                    <Typography component="subtitle1" variant="subtitle1">
                        로그인
                    </Typography>
                </Grid>
            </Grid>



            <form noValidate onSubmit={handleSubmit}>
                {/* {" "} */}
                {/* submit 버튼을 누르면 handleSubmit이 실행됨. */}
                <Grid container spacing={2}>
                    <Grid item xs={12}>
                        <TextField
                            variant="outlined"
                            required
                            fullWidth
                            id="email"
                            label="이메일 주소"
                            name="email"
                            autoComplete="email"
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <TextField
                            variant="outlined"
                            required
                            fullWidth
                            name="password"
                            label="패스워드"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                        />
                    </Grid>
                    <Grid item xs={12}>
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            color="primary"
                        >
                            로그인
                        </Button>
                    </Grid>
                </Grid>
            </form>

            <Grid container direction="column" alignItems="flex-end">
                <Grid item xs={4}><Link href="/signup"><Typography component="subtitle1" variant="subtitle1" align="right">회원가입 </Typography></Link></Grid>
            </Grid>

        </Container >

    );
};

/*

const mapStateToProps = state => ({
    loginStatus: state.authentication.loginStatus,
});

const mapDispatchToProps = dispatch => ({
    login: () => {
        dispatch(login());
    },
    logout: () => {
        dispatch(logout());
    },
});



export default connect(mapStateToProps, mapDispatchToProps,)(LoginContainer);
*/

export default LoginContainer;
