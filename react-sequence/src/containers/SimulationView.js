import * as React from 'react';
import {getStrategies} from "../service/ApiService";
import {getPossessionItems} from "../service/ApiService";
import {calculatePyramiding} from "../service/ApiService";

import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import {Typography} from "@mui/material";

const SimulationView = () => {

    const [strategies, setStrategies] = React.useState([]);
    const [items, setItems] = React.useState([]);
    const [strategy, setStrategy] = React.useState('');
    const [explanation, setExplanation] = React.useState('');
    const [item, setItem] = React.useState('');
    const [capitalGrowth, setCapitalGrowth] = React.useState([]);
    const [kellyRatio, setKellyRatio] = React.useState(0);
    const [kellyXAxis, setKellyXAxis] = React.useState([]);
    const [kellyYAxis, setKellyYAxis] = React.useState([]);

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

    const handleStrategyChange = (event) => {
        setStrategy(event.target.value.strategy);
        setExplanation(event.target.value.explanation);
    };

    const handleItemChange = (event) => {
        setItem(event.target.value);
    };

    const calculateKelly = () => {
        if (strategy.length === 0 || strategy === null || item.length === 0 || item === null) {
            return;
        }
        console.log(strategy);
        console.log(item);

        if (strategy === "PYRAMIDING") {
            let obj = new FormData();
            obj.append("itemName", item);
            calculatePyramiding(obj).then(res => {
                console.log(res);
                setCapitalGrowth(res.capitalGrowth);
                setKellyRatio(res.kellyRatio);
                setKellyXAxis(res.kellyXAxis);
                setKellyYAxis(res.kellyYAxis);
            })
        }


    }

    return (

        <div>
            <Box sx={{minWidth: 60}}>
                <FormControl fullWidth>
                    <InputLabel id="strategies">보유 전략</InputLabel>
                    <Select
                        labelId="strategy"
                        id="strategy"
                        value={strategies[0]}
                        label="strategy"
                        onChange={handleStrategyChange}
                    >
                        {strategies.map((obj, i) => {
                            return <MenuItem value={obj} key={i}>{obj.strategy}</MenuItem>
                        })}

                    </Select>
                </FormControl>
            </Box>

            <div>
                <Typography variant="subtitle1" gutterBottom component="div">
                    {explanation}
                </Typography>
            </div>

            <Box sx={{minWidth: 60}}>
                <FormControl fullWidth>
                    <InputLabel id="strategies">보유 종목</InputLabel>
                    <Select
                        labelId="strategy"
                        id="strategy"
                        value={items[0]}
                        label="strategy"
                        onChange={handleItemChange}
                    >
                        {items.map((obj, i) => {
                            return <MenuItem value={obj} key={i}>{obj}</MenuItem>
                        })}

                    </Select>
                </FormControl>
            </Box>

            <button onClick={calculateKelly}>켈리 비율 계산</button>


            <div>
                {JSON.stringify(kellyRatio)}
                {JSON.stringify(capitalGrowth)}
                {JSON.stringify(kellyXAxis)}
                {JSON.stringify(kellyYAxis)}
            </div>
        </div>


    )
}

export default SimulationView;
