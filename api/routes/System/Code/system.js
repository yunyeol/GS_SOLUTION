const express = require('express');
var fs = require('fs');
var xml_digester = require("xml-digester");
var util = require('util');

var digester = xml_digester.XmlDigester({});
const router = express.Router();

var queryResult = '';
var queryPath = 'sql/code.xml.tld';

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

/* SystemCode Select. */
router.get('/code', async function(req, res, next) {
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = queryResult.selectCode;

    if(params.whereTypeGubun == 'Y'){
        sqlQuery += queryResult.whereTypeGubun;
        sqlQuery = util.format( sqlQuery , params.type , params.gubun );
    }

    const rows = await utilFact.dbWrap.query(sqlQuery);
    console.log(rows);
    res.status(HttpStatus.OK).json(rows);
});

/* SystemCode Select. */
router.get('/selectSystemCodeTest', async function(req, res, next) {
    const query = req.query;

    const utilFact = utilFactory(['pageMaker','dbWrap']);
    const pageMaker = utilFact.PageMaker;
    pageMaker.setPageOrRow(query.currPage);

    let searchParams = ( query.searchParams ) ? JSON.parse(query.searchParams) : undefined;

    let sqlQuery = queryResult.selectCode;
    let sqlCntQuery = queryResult.selectCodeCnt;

    if( searchParams && searchParams.keyword ){
        sqlQuery += queryResult.whereSearch;
        sqlCntQuery += queryResult.whereSearch;
        sqlQuery = util.format(sqlQuery, searchParams.keyword, searchParams.keyword, searchParams.keyword, searchParams.keyword, searchParams.keyword);
    }
    sqlQuery += queryResult.limit;
    sqlQuery = util.format( sqlQuery , pageMaker.startRow,  pageMaker.ROW_GROUP);

    const cntRows  = await utilFact.dbWrap.query(sqlCntQuery);
    const rows = await utilFact.dbWrap.query(sqlQuery);
    console.log(sqlQuery);
    console.log(pageMaker.startRow);
    console.log(pageMaker.ROW_GROUP);

    const pageObj = pageMaker.getPagingObj(rows, cntRows);

    res.status(HttpStatus.OK).json(pageObj);
});

router.delete('/code', async function(req, res, next){
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);

    let sqlQuery = queryResult.deleteCode;
    sqlQuery = util.format( sqlQuery , params.type , params.gubun );

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

router.post('/code', async function(req, res, next){
    const params = req.body;

    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = queryResult.insertCode;

    sqlQuery = util.format( sqlQuery , params.type , params.gubun, params.data1, params.data2, params.data3 );

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

router.put('/code', async function(req, res, next) {
    const params = req.body;

    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = queryResult.updateCode;

    sqlQuery = util.format( sqlQuery , params.type , params.gubun, params.data1, params.data2, params.data3,
                            params.type, params.gubun);


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
