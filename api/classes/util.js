const DbWrap = require('./dbwrap');
const PageMaker = require('./pagemaker');

class Util {
    constructor(objNames){
        for(let name in objNames){
            this.setUtilFields(objNames[name]);
        }
    }
    setUtilFields(objNames){
        switch(objNames){
            case 'dbWrap' : this.dbWrap = new DbWrap(); break;
            case 'pageMaker' : this.PageMaker = new PageMaker(); break;
            default : break;
        }
    }
}

module.exports = Util