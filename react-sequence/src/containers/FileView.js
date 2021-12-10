import * as React from 'react';
import Paper from '@mui/material/Paper';
import Stack from '@mui/material/Stack';
import {styled} from '@mui/material/styles';

import Box from '@mui/material/Box';
import InputLabel from '@mui/material/InputLabel';
import MenuItem from '@mui/material/MenuItem';
import FormControl from '@mui/material/FormControl';
import Select from '@mui/material/Select';

import {getPriceTableCategoryList} from "../service/ApiService";
import {sendExcel} from "../service/ApiService";


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
                console.log(res);
                setPriceTableCatagoryList(res);
            }).catch((error) => {
        })
    }, []);


    const [priceTableCategory, setPriceTableCategory] = React.useState(null);
    const handleChange = (event) => {
        setPriceTableCategory(event.target.value);
    };


    const [selectedFile, setSelectedFile] = React.useState(null);
    const onFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    }
    const onFileUpload = () => {
        let formData = new FormData();
        formData.append("file", selectedFile);
        formData.append("priceTableCategory", priceTableCategory);
        console.log(formData.get("file"));
        console.log(formData.get("priceTableCategory"))

        sendExcel(formData).then(res => {
            console.log(res);
        });

    };


    return (
        <div>
            <Stack spacing={2}>
                <Item>Excel 파일 업로드</Item>
                <Item>
                    <Box sx={{minWidth: 120}}>
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
                </Item>
                <Item> '2020-12-09 | 종목 코드 | 시가 | 종가' 형식의 데이터여야 합니다.</Item>
                <Item>
                    <input type="file" onChange={onFileChange}/>
                    <button onClick={onFileUpload}>
                        Upload!
                    </button>
                </Item>
            </Stack>
        </div>
    );
}

export default FileView;