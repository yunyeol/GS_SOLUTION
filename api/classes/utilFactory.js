const Util = require('./util');

/** 
 * @description Util 클래스를 생성하여 리턴
 * @author      dblee
 * @date        18.10.18  
 * @param       array   objNames
 * @return      object
 * 
*/ 
const utilFactory = (objNames) => {
    return new Util(objNames);
}

module.exports = utilFactory