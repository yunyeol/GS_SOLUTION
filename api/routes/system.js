const express = require('express');
const router = express.Router();

/* SystemCode Select. */
router.get('/selectSystemCode', async function(req, res, next) {
  const utilFact = utilFactory(['dbWrap']);
  let sqlQuery = 'SELECT '
                    +'TYPE, '
                    +'GUBUN, '
                    +'DATA1, '
                    +'DATA2, '
                    +'DATA3, '
                    +'REG_DATE, '
                    +'UPT_DATE '
                +'FROM GS_BASE_CODE '
                +'WHERE 1=1';
  const rows = await utilFact.dbWrap.query(sqlQuery);

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

module.exports = router;
