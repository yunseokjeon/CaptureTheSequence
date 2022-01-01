import * as React from 'react';
import Paper from '@mui/material/Paper';
import {styled} from '@mui/material/styles';

import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

import {getPriceTableCategoryList} from "../service/ApiService";
import {sendExcel} from "../service/ApiService";
import DisplayPrice from "./DisplayPrice";
import {Container, Grid, Typography} from "@material-ui/core";


const Item = styled(Paper)(({theme}) => ({
    ...theme.typography.body2,
    padding: theme.spacing(1),
    textAlign: 'center',
    color: theme.palette.text.secondary,
}));


const FileView = () => {

    const [priceTableCategoryList, setPriceTableCatagoryList] = React.useState([]);
    React.useEffect(() => {
        getPriceTableCategoryList()
            .then((res) => {
                setPriceTableCatagoryList(res);
            }).catch((error) => {
        })
    }, []);


    const [priceTableCategory, setPriceTableCategory] = React.useState(null);
    const handleChange = (event) => {
        setPriceTableCategory(event.target.value);
    };

    const [priceObjectList, setPriceObjectList] = React.useState([]);
    const [needPriceView, setNeedPriceView] = React.useState(false);
    const [candidatesToDisplay, setCandidatesToDisplay] = React.useState([]);

    const [selectedFile, setSelectedFile] = React.useState(null);
    const onFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    }
    const onFileUpload = () => {
        let formData = new FormData();
        formData.append("file", selectedFile);
        formData.append("priceTableCategory", priceTableCategory);

        sendExcel(formData).then(res => {
            setPriceObjectList(res);
            setNeedPriceView(true);
        })
    };


    return (
        <div>
            < Container style={{ margin: "2%" }} spacing={5}>
            <Grid container spacing={5} direction="column">
                <Grid Item xs={5}>
                    <Typography component="h6" variant="h6">Excel 파일 업로드</Typography>
                </Grid>
                <Grid item>
                    <Box sx={{width: 150, height: 20}}>
                        <FormControl fullWidth>
                            <InputLabel id="priceTableCategory">테이블명 선택</InputLabel>
                            <Select
                                labelId="priceTableCategory"
                                id="priceTableCategory"
                                value={priceTableCategory}
                                label="priceTableCategory"
                                onChange={handleChange}
                            >
                                <MenuItem value={priceTableCategoryList[0]}>미국 주식</MenuItem>
                                <MenuItem value={priceTableCategoryList[1]}>선물</MenuItem>

                            </Select>
                        </FormControl>
                    </Box>
                </Grid>
                <Grid item variant="subtitle2"> 테이블 입력 스키마는 'YYYY-MM-DD | 종목 코드 | 시가 | 종가' 입니다. 파일이 업로드 되면 하단에 샘플이 출력됩니다.</Grid>
                <Grid item>
                    <input type="file" onChange={onFileChange}/>
                    <button onClick={onFileUpload}>
                        Upload!
                    </button>
                </Grid>
                <Grid item>
                    {needPriceView ? priceObjectList.map((obj, i) => {
                        if (i < 5) {
                            return <DisplayPrice data={obj} key={i}/>;
                        }
                    }) : null}
                </Grid>
            </Grid>
            </Container>
        </div>
    );
}

export default FileView;