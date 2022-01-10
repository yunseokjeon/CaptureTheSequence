# Capture The Sequence API Specification

## POST : /auth/signup

### Requests example
회원가입을 위한 API로 다음과 같은 정보를 http body에 넣어서 전송.

```JSON
{
    "email":"init_ex@hello.com",
    "username":"init",
    "password":"pass1234"

}
```

### Responses
```JSON
{
    "token": null,
    "username": "init",
    "email": "init_ex@hello.com",
    "password": null,
    "created_at": "2022-01-06T15:26:19.5879259",
    "approved": false,
    "userCategory": "GENERAL",
    "id": "4028b2c17e2e1107017e2e11194a0006"
}
```

## POST : /auth/signin
로그인을 위한 API로, 다음과 같은 JSON을 http body에 담아서 전송.

### Requests example
```JSON
{
    "email":"yun@hello.com",
    "password":"1234"
}
```

### Responses
```JSON
{
    "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4YjJjMTdlMmUxMTA3MDE3ZTJlMTEwYWZkMDAwMCIsImlzcyI6IkNUUyIsImlhdCI6MTY0MTQ1MDU2NywiZXhwIjoxNjQxNTM2OTY3fQ.4EWSq0CH2qDrorrlQIA5A4WDCzpl5pAl7d0NdPztZmww7SZHhhIduVj40axE-raLkSTZzD2BaRkt5JK6Qei6mA",
    "username": "yun",
    "email": "yun@hello.com",
    "password": null,
    "created_at": null,
    "approved": true,
    "userCategory": "ADMIN",
    "id": "4028b2c17e2e1107017e2e110afd0000"
}
```

## GET : /auth/getAllUerList

### Requests example
회원가입을 신청한 사용자 목록을 조회한 API로, 로그인 할 때 받은 JWT를 header에 담아서 전송.

```JSON
Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4YjJjMTdlMmUxMTA3MDE3ZTJlMTEwYWZkMDAwMCIsImlzcyI6IkNUUyIsImlhdCI6MTY0MTQ1MDU2NywiZXhwIjoxNjQxNTM2OTY3fQ.4EWSq0CH2qDrorrlQIA5A4WDCzpl5pAl7d0NdPztZmww7SZHhhIduVj40axE-raLkSTZzD2BaRkt5JK6Qei6mA
```

### Responses

```JSON
{
    "error": null,
    "data": [
        {
            "token": null,
            "username": "init_1",
            "email": "init1@hello.com",
            "password": null,
            "created_at": "2022-01-06T15:26:16.002519",
            "approved": false,
            "userCategory": "GENERAL",
            "id": "4028b2c17e2e1107017e2e110b4d0001"
        },
        {
            "token": null,
            "username": "init_2",
            "email": "init2@hello.com",
            "password": null,
            "created_at": "2022-01-06T15:26:16.074932",
            "approved": true,
            "userCategory": "GENERAL",
            "id": "4028b2c17e2e1107017e2e110b8f0002"
        },
        {
            "token": null,
            "username": "init_3",
            "email": "init3@hello.com",
            "password": null,
            "created_at": "2022-01-06T15:26:16.141432",
            "approved": false,
            "userCategory": "GENERAL",
            "id": "4028b2c17e2e1107017e2e110bd00003"
        },
        {
            "token": null,
            "username": "init_4",
            "email": "init4@hello.com",
            "password": null,
            "created_at": "2022-01-06T15:26:16.206454",
            "approved": true,
            "userCategory": "GENERAL",
            "id": "4028b2c17e2e1107017e2e110c120004"
        },
        {
            "token": null,
            "username": "init_5",
            "email": "init5@hello.com",
            "password": null,
            "created_at": "2022-01-06T15:26:16.272485",
            "approved": false,
            "userCategory": "GENERAL",
            "id": "4028b2c17e2e1107017e2e110c540005"
        },
        {
            "token": null,
            "username": "init",
            "email": "init_ex@hello.com",
            "password": null,
            "created_at": "2022-01-06T15:26:19.587926",
            "approved": false,
            "userCategory": "GENERAL",
            "id": "4028b2c17e2e1107017e2e11194a0006"
        }
    ]
}
```

## POST : /auth/activateAccount

### Requests example
회원가입 승인을 위한 API로, header에 JWT를 넣어서, 승인할 사용자의 정보 JSON과 함께 전송.

```JSON
Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4YjJjMTdlMmUxMTA3MDE3ZTJlMTEwYWZkMDAwMCIsImlzcyI6IkNUUyIsImlhdCI6MTY0MTQ1MDU2NywiZXhwIjoxNjQxNTM2OTY3fQ.4EWSq0CH2qDrorrlQIA5A4WDCzpl5pAl7d0NdPztZmww7SZHhhIduVj40axE-raLkSTZzD2BaRkt5JK6Qei6mA

{
    "email":"init1@hello.com"
}
```

### Responses

```JSON
{
    "token": null,
    "username": "init_1",
    "email": "init1@hello.com",
    "password": null,
    "created_at": "2022-01-06T15:26:16.002519",
    "approved": true,
    "userCategory": "GENERAL",
    "id": "4028b2c17e2e1107017e2e110b4d0001"
}
```

## POST : /file/excel/read

### Requests example
엑셀에 담긴 데이터를 DB에 입력하기 위한 API로, JWT와 함께, 엑셀 파일을 form data 형식으로 전송.

```JSON
Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4YjJjMTdlMmUxMTA3MDE3ZTJlMTEwYWZkMDAwMCIsImlzcyI6IkNUUyIsImlhdCI6MTY0MTQ1MDU2NywiZXhwIjoxNjQxNTM2OTY3fQ.4EWSq0CH2qDrorrlQIA5A4WDCzpl5pAl7d0NdPztZmww7SZHhhIduVj40axE-raLkSTZzD2BaRkt5JK6Qei6mA

// form-data
file : TSLA.xlsx
priceTableCategory : STOCK
```
### Responses

```JSON
{
    "error": null,
    "data": [
        {
            "marketDate": "2020-12-09",
            "itemName": "TSLA",
            "startingPrice": 653.690002,
            "closingPrice": 604.47998
        },
        {
            "marketDate": "2020-12-10",
            "itemName": "TSLA",
            "startingPrice": 574.369995,
            "closingPrice": 627.070007
        },
        {
            "marketDate": "2020-12-11",
            "itemName": "TSLA",
            "startingPrice": 615.01001,
            "closingPrice": 609.98999
        },
        ...

    ]
}
```

## GET : /file/getPriceTableCategoryList

### Requests example
특별한 자격이 요구되지 않는 API로, 저장이 가능한 영역의 목록을 조회할 수 있다.

```JSON
None
```

### Responses
```JSON
[
    "STOCK",
    "FUTURES"
]
```

## GET : /strategy/getStrategies

### Requests example
거래 전략의 목록을 조회할 수 있다.

```JSON
None
```

### Responses

```JSON
[
    {
        "strategy": "PYRAMIDING",
        "explanation": "Jesse Livermore의 거래 전략"
    }
]
```

## GET : /strategy/getPossessionItems

### Requests example
JWT를 전송하면, 자신이 보유한 종목의 목록을 확인할 수 있는 API이다.

```JSON
Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4YjJjMTdlMmUxMTA3MDE3ZTJlMTEwYWZkMDAwMCIsImlzcyI6IkNUUyIsImlhdCI6MTY0MTQ1MDU2NywiZXhwIjoxNjQxNTM2OTY3fQ.4EWSq0CH2qDrorrlQIA5A4WDCzpl5pAl7d0NdPztZmww7SZHhhIduVj40axE-raLkSTZzD2BaRkt5JK6Qei6mA
```

### Responses

```JSON
[
    "TSLA"
]
```

## POST : /strategy/getPyramidingKelly

### Requests example
JWT와 함께, 종목의 이름을 패러미터로 전송하면, 피라미딩 거래 전략 시뮬레이션의 결과를 받을 수 있는 API이다.

```JSON
Param : itemName=TSLA

Bearer Token : eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI0MDI4YjJjMTdlMmUxMTA3MDE3ZTJlMTEwYWZkMDAwMCIsImlzcyI6IkNUUyIsImlhdCI6MTY0MTQ1MDU2NywiZXhwIjoxNjQxNTM2OTY3fQ.4EWSq0CH2qDrorrlQIA5A4WDCzpl5pAl7d0NdPztZmww7SZHhhIduVj40axE-raLkSTZzD2BaRkt5JK6Qei6mA
```

### Responses

```JSON
{
    "kellyRatio": 9.0,
    "kellyXAxis": [
        0.0,
        1.0,
        2.0,
        3.0,
        4.0,
        5.0,
        6.0,
        7.0,
        8.0,
        9.0
    ],
    "kellyYAxis": [
        0.0,
        3.298163689072922,
        3.9726625869444483,
        4.371833512237972,
        4.656353574396201,
        4.877595109190418,
        5.0586466421782665,
        5.211889173600803,
        5.34473891328048,
        5.461991452997541
    ],
    "capitalGrowth": [
        10000.0,
        12606.289737
    ]
}
```



