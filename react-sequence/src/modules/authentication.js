import {createAction, handleActions} from 'redux-actions';

const LOGIN = 'authentication/LOGIN';
const LOGOUT = 'authentication/LOGOUT';

export const login = createAction(LOGIN);
export const logout = createAction(LOGOUT);

const initialState = {
    loginStatus: false,
};

const authentication = handleActions(
    {
        [LOGIN]: (state, action) => ({loginStatus: !state.loginStatus}),
        [LOGOUT]: (state, action) => ({loginStatus: !state.loginStatus}),
    },
    initialState
);

export default authentication;


