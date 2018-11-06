const express = require('express');
const router = express.Router();

/* GET users listing. */
router.get('/profile', async function(req, res, next) {
  const utilFact = utilFactory(['dbWrap']);
  let sqlQuery = 'SELECT TYPE, GUBUN, DATA1, DATA2, DATA3, REG_DATE, UPT_DATE FROM GS_BASE_CODE';
  const rows = await utilFact.dbWrap.query(sqlQuery);
  
  console.log(rows);

  res.status(HttpStatus.OK).json(rows);
});

module.exports = router;
