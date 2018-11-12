class PageMaker{
    constructor(options){
        this.startPage;
        this.endPage;
        this.startRow;
        this.totalCnt;
        this.PAGE_GROUP = 5;
        this.ROW_GROUP = 10;
    }
    getStartPage(current){
        return parseInt( (current - 1) / this.PAGE_GROUP ) * this.PAGE_GROUP + 1;
    }
    getEndPage(current){
        return parseInt( (current - 1) / this.PAGE_GROUP ) * this.PAGE_GROUP + this.PAGE_GROUP;
    }
    getStartRow(current){
        return (current === 1) ? this.startRow = 0 : this.startRow = (current-1) * this.ROW_GROUP;
    }
    setPageOrRow(currPage){
        this.startRow = this.getStartRow(parseInt(currPage));
        this.startPage = this.getStartPage(parseInt(currPage));
        this.endPage = this.getEndPage(parseInt(currPage));
    }
    getPagingObj(rows, cntRows){
        if (rows.length > 0){
            rows[0].TOTAL_CNT = cntRows[0].CNT;
        }
        
        let pageObj = {};
        pageObj.startPage = this.startPage;
        pageObj.endPage = this.endPage;
        pageObj.rows = rows;
        
        return pageObj;
    }

}

module.exports = PageMaker;