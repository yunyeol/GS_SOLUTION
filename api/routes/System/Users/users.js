const express = require('express');
var fs = require('fs');
var xml_digester = require("xml-digester");
var util = require('util');

var digester = xml_digester.XmlDigester({});
const router = express.Router();

var queryResult = '';
var queryPath = 'sql/users.xml.tld';

fs.readFile(queryPath, "utf-8", function(error, data){
    if(error){
        console.log(error);
    }else{
        digester.digest(data, function(error, xmlResult) {
            if (error) {
                console.log(error);
            } else {
                queryResult = xmlResult.query;
            }
        });
    }
});

router.get('/users', async function(req, res, next) {
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = queryResult.selectUsers;

    if(params && params.keyWord){
        sqlQuery += queryResult.whereAddSearch;
        sqlQuery = util.format( sqlQuery , params.keyWord,  params.keyWord, params.keyWord, params.keyWord);
    }

    if(params && params.rowGroup > -1){
        sqlQuery += queryResult.limit;
        sqlQuery = util.format( sqlQuery , params.startPage,  params.rowGroup);
    }

    const rows = await utilFact.dbWrap.query(sqlQuery);
    console.log(rows);

    res.status(HttpStatus.OK).json(rows);
});

router.get('/users/page', async function(req, res, next) {
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);
    let sqlCntQuery = queryResult.selectUsersCnt;

    if(params && params.keyWord){
        sqlCntQuery += queryResult.whereAddSearch;
        sqlCntQuery = util.format( sqlCntQuery , params.keyWord,  params.keyWord, params.keyWord, params.keyWord);
    }

    const totalCnt = await utilFact.dbWrap.query(sqlCntQuery);

    console.log(totalCnt);

    var obj = totalCnt[0].CNT;
    res.status(HttpStatus.OK).json(obj);
});

router.delete('/users', async function(req, res, next){
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);

    let sqlQuery = queryResult.deleteUsers;
    sqlQuery = util.format( sqlQuery , params.loginId );

    const result = await utilFact.dbWrap.query(sqlQuery);
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
