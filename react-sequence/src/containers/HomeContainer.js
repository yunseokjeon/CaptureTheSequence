import React from "react";
import { connect } from 'react-redux';
import { Navigate, Route } from 'react-router-dom';

const HomeContainer = ({ loginStatus }) => {

    console.log("HomeContainer");

    return (
        loginStatus ? (
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
    loginStatus: state.authentication.loginStatus,
});

export default connect(
    mapStateToProps,
)(HomeContainer);