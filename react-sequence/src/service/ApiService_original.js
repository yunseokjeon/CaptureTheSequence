
/*

export const ApiCall = (api, method, request) => {
    // export function ApiCall(api, method, request) {

    // let headers = new Headers({
    //     "Content-Type": "application/json",
    // });

    // 로컬 스토리지에서 ACCESS TOKEN 가져오기
    const accessToken = localStorage.getItem("ACCESS_TOKEN");
    // if (accessToken && accessToken !== null) {
    //     headers.append("Authorization", `Bearer ${accessToken}`);
    // }

    if (request) {
        // GET method
        options.body = JSON.stringify(request);
    }

    let url = API_BASE_URL + api

    let options = { headers: { Authorization: `Bearer ${accessToken}` } };


    // const [data, setData] = useState();
   

    const call2 = async () => {
        try {
            const response = await axios.get(url, options);
            let data;
            data = response.data;
            console.log(data);
            return data;
        } catch (e) {
            console.log(e);
        }
    }

    call2();
};
*/
