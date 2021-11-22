import React from "react";
import {
    Link,
    Button,
    TextField,
    Grid,
    Container,
    Typography,
} from "@material-ui/core";
import { signup } from "../service/ApiService";

const validateEmail = (email) => {

    const regex = /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[A-Za-z]+$/;
    if (regex.test(email)) {
        console.log("correct email");
        return true;
    } else {
        console.log("incorrect email");
        return false;
    }
}

const validatePassword = (password, password2) => {
    if (password === password2) {
        return true;
    } else {
        return false;
    }
}

const validateUsername = (username) => {
    if (username === null || username.length === 0 || username.trim() === '') {
        return false;
    } else {
        return true;
    }
}

const handleSubmit = (event) => {
    event.preventDefault();
    let username = event.target[0].value;
    let email = event.target[2].value;
    let password = event.target[4].value;
    let password2 = event.target[6].value;

    let usernameResult = validateUsername(username);
    let emailResult = validateEmail(email);
    let passwordResult = validatePassword(password, password2);

    if (usernameResult === true && emailResult === true && passwordResult === true) {
        console.log("all true");
        const data = new FormData(event.target);
        const username = data.get("username");
        const email = data.get("email");
        const password = data.get("password");
        signup({ email: email, username: username, password: password }).then(
            (response) => {
                window.location.href = "/login";
            }
        );

    } else {
        console.log("something wrong");
    }


}

const SignupContainer = () => {

    if (localStorage.getItem('ACCESS_TOKEN') !== null || localStorage.getItem('SequenceEmail') !== null) {
        window.location.href = "/";
    }

    return (<Container component="main" maxWidth="xs" style={{ marginTop: "8%" }}>

        <Grid container spacing={2} columns={16}>
            <Grid item xs={2}>
                <Typography component="h5" variant="h5">CAPTURE THE SEQUENCE </Typography>
            </Grid>

            <Grid item xs={8}></Grid>

            <Grid item xs={2}><Link href="/login"><Typography component="h5" variant="h5">로그인 </Typography></Link></Grid>
        </Grid>

        <Grid container spacing={2}>
            <Grid item xs={12}>
                <Typography component="h1" variant="h5">
                    회원가입
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
                        id="username"
                        label="이름"
                        name="username"
                        autoComplete="username"
                    />
                </Grid>
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
                    <TextField
                        variant="outlined"
                        required
                        fullWidth
                        name="password2"
                        label="패스워드 확인"
                        type="password"
                        id="password2"
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
                        회원가입 승인요청
                    </Button>
                </Grid>
            </Grid>
        </form>

    </Container >);
}

export default SignupContainer;