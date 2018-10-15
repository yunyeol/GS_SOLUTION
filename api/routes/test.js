var express = require('express');
var router = express.Router();
var test1 = require('./dbConfig.js');

test1.dbConfig().connect();

router.get('/', function(req, res, next) {
    //connection.query("SELECT * FROM GS_JOB_INSTANCE", function (err, result, fields) {
        test1.dbConfig().query("SELECT * FROM GS_JOB_INSTANCE", function (err, result, fields){
        if(err){
            console.log(err);
        }else{
            res.send(result);

            for(var i=0; i<result.length; i++){
                console.log(result[i].JOB_KEY);
            }
        }
    });

});

module.exports = router;
