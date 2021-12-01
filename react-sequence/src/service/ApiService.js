import { connect } from 'react-redux';
import { login } from '../modules/authentication';
import store from '../modules/store';
import { API_BASE_URL } from "./app-config";
import { resolvePath } from 'react-router';
import { Sync } from '@material-ui/icons';

const ACCESS_TOKEN = "ACCESS_TOKEN";

export function call(api, method, request) {

    let headers = new Headers({
        "Content-Type": "application/json",
    });

    // 로컬 스토리지에서 ACCESS TOKEN 가져오기
    const accessToken = localStorage.getItem("ACCESS_TOKEN");
    if (accessToken && accessToken !== null) {
        headers.append("Authorization", "Bearer " + accessToken);
    }

    let options = {
        headers: headers,
        url: API_BASE_URL + api,
        method: method,
    };

    if (request) {
        // GET method
        options.body = JSON.stringify(request);
    }
    return fetch(options.url, options)
        .then((response) =>
            response.json().then((json) => {

                if (!response.ok) {

                    // response.ok가 true이면 정상적인 리스폰스를 받은것, 아니면 에러 리스폰스를 받은것.
                    return Promise.reject(json);
                }
                return json;
            })
        )
        .catch((error) => {
            // 추가된 부분
            console.log(error.status);
            if (error.status === 403) {
                window.location.href = "/login"; // redirect
            }
            return Promise.reject(error);
        });
}


export function signin(userDTO) {

    console.log(store.getState());

    return call("/auth/signin", "POST", userDTO).then((response) => {
        if (response.token) {
            // 로컬 스토리지에 토큰 저장
            localStorage.setItem(ACCESS_TOKEN, response.token);
            localStorage.setItem("SequenceEmail", response.email);
            localStorage.setItem("LOGIN_STATUS", true);
            window.location.href = "/";
        }
    });

}

export function signup(userDTO) {
    return call("/auth/signup", "POST", userDTO);
}



export async function getAllUserList() {
    let result = await call("/auth/getAllUerList", "GET");
    return result;
}

export function signout() {
    // localStorage.setItem(ACCESS_TOKEN, null);
    // localStorage.setItem("SequenceEmail", null);
    // localStorage.setItem("LOGIN_STATUS", false);
    localStorage.clear();
    window.location.href = "/";
}


export function activateAccount(userDTO){
    return call("/auth/activateAccount", "POST", userDTO);
}
