const PORT = 10009
const DEF_URL = '/api'

const HttpStatus = require('http-status-codes');
const dotEnv = require('dotenv');
const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const app = express();
const indexRouter = require('./routes/test');
const usersRouter = require('./routes/users');
const systemRouter = require('./routes/system');
const utilFactory = require('./classes/utilFactory');

//static variable
global.HttpStatus = HttpStatus;
global.utilFactory = utilFactory;

// app.use(require('connect-history-api-fallback')())

//production vue set
app.use(express.static('public/vue_production'));

//cors
app.use(cors());

//bodyParser
app.use(bodyParser.json());

//router set
app.use(DEF_URL, indexRouter);
app.use(DEF_URL+'/users', usersRouter);
app.use(DEF_URL+'/system', systemRouter);
app.use((req, res, next) => {
    console.error('404 not Found');
    res.status(HttpStatus.NOT_FOUND).send('404 not Found');
});
app.use((err, req, res, next) => {
    console.error(err.stack);
    res.status(HttpStatus.INTERNAL_SERVER_ERROR).send('Something broke!');
});

// NODE_ENV (production or develpment)
process.env.NODE_ENV = ( process.env.NODE_ENV && process.env.NODE_ENV.trim().toLowerCase() == 'production' ) ? 'production' : 'development';
if (process.env.NODE_ENV == 'production') {
    dotEnv.config({path: __dirname + '/env/prod.env'});
    console.log("Production Mode");
} else if (process.env.NODE_ENV == 'development') {
    dotEnv.config({path: __dirname + '/env/dev.env'});
    console.log("Development Mode");
}

app.listen(PORT, () => console.log(`Example app listening on port ${PORT}!`));
