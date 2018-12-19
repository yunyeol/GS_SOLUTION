package com.project.alarmeweb.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageMaker {

    private static final int PAGE_GROUP = 5;
    private static final int ROW_GROUP = 10;
//    private int startPage;
//    private int endPage;
    private int startRow;
    private int endRow;
    private int totPage;
    private int totCnt;

    List<?> contentList;

//    public void setStartPage(int currIdx){
//        this.startPage = ( currIdx / this.PAGE_GROUP ) * this.PAGE_GROUP + 1;
//    }
//    public void setEndPage(int currIdx){
//        this.endPage = ( currIdx / this.PAGE_GROUP ) * this.PAGE_GROUP + this.PAGE_GROUP;
//    }
    public void setStartRow(int currIdx){
        this.startRow = ( currIdx == 1 ) ? 0 : (currIdx-1) * this.ROW_GROUP;
    }
    public void setEndRow(){
        this.endRow = this.ROW_GROUP;
    }
    public void setTotPage(){
       this.totPage = (int)Math.ceil((double)this.getTotCnt() / this.ROW_GROUP) ;
    }
    public void setPaging(int currIdx){
//        this.setStartPage(currIdx);
//        this.setEndPage(currIdx);
        this.setStartRow(currIdx);
        this.setEndRow(currIdx);
    }
    public static PageMaker getInstance(){
        return new PageMaker();
    }
}
