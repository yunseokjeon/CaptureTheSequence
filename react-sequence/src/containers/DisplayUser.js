import { useEffect, useRef, useState } from "react";
import {
    TextField,
    Grid,
    Container,
    Typography,
    Swith
} from "@material-ui/core";

const DisplayUser = (inputUser) => {


    console.log("=====================================")
    console.log(inputUser.user.username);
    const [user, setUser] = useState();


    // const [user, setUser] = useState();
    // useEffect(() => {
    //     console.log('userEffect_setUser_start'); // delete
    //     setUser(inputUser.inputUser);
    //     console.log('userEffect_setUser_end'); // delete
    // }, []);
    // console.log(user);

    // console.log(inputUser.inputUser);
    //console.log('user???' + inputUser);

    return (
        // <div> {user.username} </div>
        <div> test </div>
    )
}

export default DisplayUser;