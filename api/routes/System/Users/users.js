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

    const rows = await utilFact.dbWrap.query(sqlQuery);
    console.log(rows);

    res.status(HttpStatus.OK).json(rows);
});

module.exports = router;
