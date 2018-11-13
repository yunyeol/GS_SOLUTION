const mysql = require('mysql2/promise');

class DbWrap {
    constructor(){
        this._options = {};
        this._options.host = process.env.DB_HOST;
        this._options.user = process.env.DB_USER;
        this._options.password = process.env.DB_PASS;
        this._options.database = process.env.DB_DATABASE;
    }
    async getConnection(){
        try{
            if( !this._options ) throw 'this options empty';
            const pool = await mysql.createPool(this._options);
            const conn = await pool.getConnection(async conn => conn);
            return conn;
        }catch(e){
            throw new Error(e);
        }
    }
    async query(sqlQuery, params){
        try{
            const conn = await this.getConnection();

            if( !conn ) throw 'this connection empty';
            const [rows] = await conn.query(sqlQuery, params);
            conn.destroy();
            return rows;
        }catch(e){
            throw new Error(e);
        }
    }
}

module.exports = DbWrap
