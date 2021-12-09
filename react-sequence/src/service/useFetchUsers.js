import { useState, useEffect } from "react";
import { getAllUserList } from "./ApiService";

const useFetchUsers = () => {
    const [loading, setLoading] = useState(true);
    const [users, setUsers] = useState(null);
    const [error, setError] = useState(null);

    useEffect(() => {
        getAllUserList()
            .then((res) => {
                console.log(res);
                setUsers(res.data);
                setLoading(false);
            })
            .catch((error) => {
                console.log(error);
                setError(error);
                setLoading(false);
            })
    }, []);

    return { loading, users, error };
}

export default useFetchUsers;