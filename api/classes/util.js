const DbWrap = require('./utils/dbwrap');
const PageMaker = require('./utils/pagemaker');
const FileSystem = require('./utils/fileSystem');

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
            case 'fs' : this.fs = new FileSystem(); break;
            default : break;
        }
    }
}

module.exports = Util
