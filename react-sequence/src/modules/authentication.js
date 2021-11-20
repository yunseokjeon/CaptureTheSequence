import { createStore } from 'redux';
import { createAction, handleActions } from 'redux-actions';

const LOGIN = 'authentication/LOGIN';
const LOGOUT = 'authentication/LOGOUT';

export const login = createAction(LOGIN);
export const logout = createAction(LOGOUT);


const initialState = {
    loginStatus: false,
};

const authentication = handleActions(
    {
        [LOGIN]: (state, action) => ({ loginStatus: true }),
        [LOGOUT]: (state, action) => ({ loginStatus: false }),
      
    },
    initialState
);


export default authentication;


