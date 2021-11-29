import { SettingsBackupRestoreSharp } from "@material-ui/icons";
import React, { useLayoutEffect, useEffect, useState, useRef } from "react";
import { useResolvedPath } from "react-router";
import useFetchUsers from "./useFetchUsers";
import DisplayUser from "./DisplayUser";

//  export const allUserList = () => {
//     getAllUserList().then((value) => {

//         console.log('getAllUserList 먼저!!!');
//         console.log(value);
//         // for(var i = 0; i < value.data.length; i++) {
//         //     var obj = value.data[i];
//         //     console.log(obj.username);
//         //     console.log(obj.id);
//         // }
//         console.log('getAllUserList 내부 실행 종료!!!');
//     });



const AdminContainer = () => {

    if (localStorage.getItem('SequenceEmail') !== "yun@hello.com") {
        window.location.href = "/";
    }

    const { loading, users, error } = useFetchUsers();

    /*
    approved: false
    created_at: "2021-11-27T12:16:34.142664"
    email: "init1@hello.com"
    id: "40285bee7d5f64fb017d5f64fef10001"
    password: null
    token: null
    userCategory: "GENERAL"
    username: "init_1"
    */

    if (loading) return <div>Loading...</div>;
    if (error) return <div>Error!</div>;

    return (
        <div>
            {/* {JSON.stringify(users)} */}
            {users.map((user, i) => {
                return <DisplayUser user={user} key={i} />
            })}
        </div>
    )

}

export default AdminContainer;