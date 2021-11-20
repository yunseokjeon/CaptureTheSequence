import { createStore } from "redux";
import authentication from "./authentication";

const store = createStore(authentication);

export default store;