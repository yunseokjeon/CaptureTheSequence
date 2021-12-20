import * as React from 'react';
import {getStrategies} from "../service/ApiService";
import {getPossessionItems} from "../service/ApiService";

const SimulationView = () => {

    const [strategies, setStrategies] = React.useState([]);
    const [items, setItems] = React.useState([]);

    React.useEffect(() => {
        getStrategies().then((res) => {
            setStrategies(res);
        }).catch((error) => {
        });

        getPossessionItems().then((res) => {
            setItems(res);
        }).catch((error) => {

        });
    }, [])

    return (

        <div>
            <div>{JSON.stringify(strategies)}</div>
            <div>{JSON.stringify(items)}</div>
        </div>


    )
}

export default SimulationView;
