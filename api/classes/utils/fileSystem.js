var fs = require('fs');
var xml_digester = require("xml-digester");

var digester = xml_digester.XmlDigester({});

class FileSystem {
    constructor(){

    }
    getXml(url, result){
        try{
            fs.readFile(url, "utf-8", function(error, data){
                if(error){
                    console.log(error);
                }else{
                    digester.digest(data, function(error, xmlResult) {
                        if (error) {
                            console.log(error);
                        } else {
                            result = xmlResult.query;
                        }
                    });
                }
            });

            return result;
        }catch(e){
            throw new Error(e);
        }
    }
}

module.exports = FileSystem;



