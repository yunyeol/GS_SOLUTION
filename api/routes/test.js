const express = require('express');
const router = express.Router();

/* GET users listing. */
router.get('/test', async (req, res, next) => {
    try{
        const utilFact = utilFactory(['dbWrap']);
        
        let sqlQuery = 'select * from (SELECT 1 + 1 AS solution UNION ALL SELECT 1 +3  AS solution) as t where solution in (?,?)';
        const rows = await utilFact.dbWrap.query(sqlQuery,[2,4]);
        
        res.status(HttpStatus.OK).json(rows);
    }catch(e){
        console.log(e);
        res.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
});

/* GET users listing. */
router.get('/list', async (req, res, next) => {
    try{
        const query = req.query;
        const utilFact = utilFactory(['pageMaker','dbWrap']);
        const pageMaker = utilFact.PageMaker;
        let startRow = pageMaker.getStartRow(parseInt(query.currPage));
        let startPage = pageMaker.getStartPage(parseInt(query.currPage));
        let endPage = pageMaker.getEndPage(parseInt(query.currPage));

        const pageObj = {};
        pageObj.startPage = startPage;
        pageObj.endPage = endPage;
        
        let sqlQuery = 'SELECT' 
                     +' JOB_INSTANCE_ID,'
                     +' JOB_EXECUTION_ID,'
                     +' CREATE_TIME,'
                     +' (select count(1) cnt from GS_JOB_EXECUTION cnt) TOTAL_CNT'
                     +' from GS_JOB_EXECUTION'
                     +' order by create_time desc'
                     +' limit ?,?';
        const rows = await utilFact.dbWrap.query(sqlQuery,[startRow,pageMaker.PAGE_GROUP]);
        pageObj.rows = rows;

        res.status(HttpStatus.OK).json(pageObj);
    }catch(e){
        console.log(e);
        res.status(HttpStatus.INTERNAL_SERVER_ERROR);
    }
});

module.exports = router;
