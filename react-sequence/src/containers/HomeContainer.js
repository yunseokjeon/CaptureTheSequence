import React from "react";
import { connect } from 'react-redux';
import { login, logout } from '../modules/authentication';
import { Navigate, Route } from 'react-router-dom';
import store from '../modules/store';


const HomeContainer = ({ loginStatus, login, logout }) => {

    console.log("HomeContainer");
    console.log(localStorage.getItem('ACCESS_TOKEN'));
   
    if (localStorage.getItem('ACCESS_TOKEN') != null) {
        console.log('HomeContainer1 : ' + loginStatus);
        login();
        console.log('HomeContainer2 : ' + loginStatus);
    }

 
    return (
        loginStatus
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