var mysql = require('mysql');

module.exports = {
    dbConfig : function(){
        var connection = mysql.createConnection({
            host:'210.89.191.78',
            port:'3306',
            user: 'msvc_acs',
            password: 'GSghatyvld@3',
            database: 'MSVC'
        });

        return connection;
    }
}
