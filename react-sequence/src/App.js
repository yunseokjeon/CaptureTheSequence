import logo from './logo.svg';
import './App.css';
// import {Routes, Route, Switch} from "react-router-dom";
import { Routes, Route } from 'react-router-dom';
import HomeContainer from "./containers/HomeContainer";
import LoginContainer from "./containers/LoginContainer";
import SignupContainer from './containers/SignupContainer';
import AdminContainer from './containers/AdminContainer';

function App() {
    console.log("App");

    return (
        <div>
            <Routes>
                <Route path="/" element={<HomeContainer />} exact={true} />
                <Route path="/login" element={<LoginContainer />} />
                <Route path="/signup" element={<SignupContainer />} />
                <Route path="/admin" element={<AdminContainer />} />
            </Routes>
        </div>
    );
}

export default App;
