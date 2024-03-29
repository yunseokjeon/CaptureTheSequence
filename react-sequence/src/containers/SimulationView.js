import * as React from 'react';
import {getStrategies} from "../service/ApiService";
import {getPossessionItems} from "../service/ApiService";
import {calculatePyramiding} from "../service/ApiService";
import OptimalKellyChart from "./OptimalKellyChart";
import "./SimulationView.css";


import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';
import {Container, Grid, Typography} from "@material-ui/core";


const refineDataForOptimalKelly = (kellyXAxis, kellyYAxis) => {
    let arr = [];
    for (let i = 0; i < kellyXAxis.length; i++) {
        let obj = {
            "x": kellyXAxis[i],
            "y": kellyYAxis[i]
        };
        arr.push(obj);
    }

    let obj = {
        "id": "Optimal Kelly",
        "color": "hsl(293, 70%, 50%)",
        "data": arr
    };

    let result = [];
    result.push(obj);
    return result;
}

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
    const [optimalData, setOptimalData] = React.useState([]);

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
                let result = refineDataForOptimalKelly(res.kellyXAxis, res.kellyYAxis);
                setOptimalData(result);
            })
        }


    }

    return (

        <div>
            <Container style={{margin: "2%"}} spacing={5}>
                <Grid container spacing={5} direction="column">
                    <Grid container spacing={5} direction="row">
                        <Grid item>
                            <Box sx={{width: 150, height: 20}}>
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
                                            return <div value={obj} key={i}>{obj.strategy}</div>
                                        })}
                                    </Select>
                                </FormControl>
                            </Box>
                        </Grid>
                        <Grid item>
                            <Box sx={{width: 150, height: 20}}>
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
                        </Grid>

                        <Grid item>
                            <Typography variant="subtitle1" gutterBottom component="div">
                                {explanation}
                            </Typography>
                        </Grid>

                        <Grid item>
                            <button onClick={calculateKelly}>켈리 비율 계산</button>
                        </Grid>
                    </Grid>


                    <Grid container direction="column"
                          alignItems="left"
                          justify="left"
                    >
                        <div className={"optimalDiv"}>
                            <OptimalKellyChart data={optimalData}/>
                        </div>

                        <div>최적 베팅 비율은 {kellyRatio} 입니다.</div>
                    </Grid>
                </Grid>
            </Container>
        </div>


    )
}

export default SimulationView;
