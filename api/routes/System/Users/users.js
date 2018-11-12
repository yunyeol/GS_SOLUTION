const express = require('express');
const router = express.Router();

/* GET users listing. */
router.get('/users', async function(req, res, next) {
    const params = req.query;
    const utilFact = utilFactory(['dbWrap']);
    let sqlQuery = 'SELECT ' +
                        'GM.LOGIN_ID, ' +
                        'GM.MBR_NAME, ' +
                        'GM.ACTIVE_YN, ' +
                        'GG.GRP_NAME, ' +
                        'GA.AUTH_NAME, ' +
                        'GM.REG_DATE ' +
                    'FROM GS_MBR GM '+
                    'INNER JOIN GS_GRP GG ON (GM.MBR_GRP_ID = GG.MBR_GRP_ID) '+
                    'INNER JOIN GS_AUTH GA ON (GM.MBR_AUTH_ID = GA.MBR_AUTH_ID) ';

    const rows = await utilFact.dbWrap.query(sqlQuery);
    console.log(rows);

    res.status(HttpStatus.OK).json(rows);
});

module.exports = router;
