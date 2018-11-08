const express = require('express');
const router = express.Router();

/* SystemCode Select. */
router.get('/selectSystemCode', async function(req, res, next) {
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = 'SELECT '
                    +'TYPE, '
                    +'GUBUN, '
                    +'DATA1, '
                    +'DATA2, '
                    +'DATA3, '
                    +'REG_DATE, '
                    +'UPT_DATE '
                +'FROM GS_BASE_CODE ';

    var rows = '';
    if(params.whereAdd == 'Y'){
        sqlQuery = sqlQuery + "WHERE TYPE = ? AND GUBUN = ?";
        rows = await utilFact.dbWrap.query(sqlQuery,[params.type,params.gubun]);
    }else{
        rows = await utilFact.dbWrap.query(sqlQuery);
    }

    console.log(rows);

    res.status(HttpStatus.OK).json(rows);
});

router.delete('/deleteSystemCode', async function(req, res, next){
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = 'DELETE FROM GS_BASE_CODE WHERE TYPE = ? AND GUBUN = ?';

    const result = await utilFact.dbWrap.query(sqlQuery,[params.type,params.gubun]);
    console.log(result);

    var msg;
    if(result.affectedRows > 0){
        msg = "success";
    }else{
        msg = "fail";
    }

    res.status(HttpStatus.OK).json(msg);
});

router.post('/insertSystemCode', async function(req, res, next){
    const params = req.body;

    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = 'INSERT INTO GS_BASE_CODE(TYPE, GUBUN, DATA1, DATA2, DATA3, REG_DATE)' +
                    'VALUES(?, ?, ?, ?, ?, SYSDATE())';

    console.log(params.type+", "+params.gubun+", "+params.data1+", "+params.data2+", "+params.data3);

    const result = await utilFact.dbWrap.query(sqlQuery,
                                                [params.type, params.gubun,
                                                params.data1, params.data2, params.data3]);
    console.log(result);

    var msg;
    if(result.affectedRows > 0){
        msg = "success";
    }else{
        msg = "fail";
    }

    res.status(HttpStatus.OK).json(msg);
});

module.exports = router;
