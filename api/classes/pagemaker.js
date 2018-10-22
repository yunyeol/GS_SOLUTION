class PageMaker{
    constructor(options){
        this.startPage;
        this.endPage;
        this.startRow;
        this.totalCnt;
        this.PAGE_GROUP = 10;
    }
    getStartPage(current){
        return parseInt( (current - 1) / this.PAGE_GROUP ) * this.PAGE_GROUP + 1;
    }
    getEndPage(current){
        return parseInt( (current - 1) / this.PAGE_GROUP ) * this.PAGE_GROUP + this.PAGE_GROUP;
    }
    getStartRow(current){
        return (current === 1) ? this.startRow = 0 : this.startRow = current * this.PAGE_GROUP;
    }
}

module.exports = PageMaker;