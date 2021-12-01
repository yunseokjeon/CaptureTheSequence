import React, { useState, useEffect } from "react";
import { connect } from 'react-redux';
import { login, logout } from '../modules/authentication';
import { Navigate, Route } from 'react-router-dom';
import store from '../modules/store';


const HomeContainer = () => {

    console.log(localStorage.getItem('ACCESS_TOKEN'));
    console.log(localStorage.getItem('SequenceEmail'));
    console.log(localStorage.getItem('LOGIN_STATUS'));

    if (localStorage.getItem('LOGIN_STATUS')) {
        return <div>Hello World</div>
    } else {
        return (
            <Navigate to="/login" />
        );
    }
};

/*
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
*/

export default HomeContainer;