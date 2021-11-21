import React from "react";
import { connect } from 'react-redux';
import { login, logout } from '../modules/authentication';
import { Navigate, Route } from 'react-router-dom';
import store from '../modules/store';


const HomeContainer = ({ loginStatus, login, logout }) => {

    console.log(localStorage.getItem('ACCESS_TOKEN'));

    if (loginStatus === false && localStorage.getItem('ACCESS_TOKEN') !== null) {
        // login();
        loginStatus = true;

    }


    return (
        loginStatus === true
            ? (
                <div>
                    Hello World
                </div>
            ) :
            (
                <Navigate to="/login" />
            )
    );
};

const mapStateToProps = state => ({
    loginStatus: state.authentication.loginStatus
});

const mapDispatchToProps = dispatch => ({
    login: () => {
        dispatch(login());
    },
    logout: () => {
        dispatch(logout());
    },
});


export default connect(
    mapStateToProps,
    mapDispatchToProps,
)(HomeContainer);