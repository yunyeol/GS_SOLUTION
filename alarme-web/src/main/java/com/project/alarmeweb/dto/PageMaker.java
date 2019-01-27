package com.project.alarmeweb.dto;

import lombok.Data;
import java.util.List;

@Data
public class PageMaker {
    private int startRow;
    private int limitRow;
    private int totPage;
    private int totCnt;
    private int startShowRow;
    private int endShowRow;
    List<?> contentList;

    public void setStartShowRow(int currIdx, int rowValue){
        this.startShowRow = ( (currIdx-1) * rowValue ) + 1;
    }
    public void setEndShowRow(int currIdx, int rowValue){
        this.endShowRow =  ( (currIdx-1) * rowValue ) + rowValue;
    }
    public void setStartShowRow(int rowValue){
        this.startShowRow = rowValue;
    }
    public void setEndShowRow(int rowValue){
        this.endShowRow =  rowValue;
    }
    public void setStartRow(int currIdx, int rowGroup){
        this.startRow = ( currIdx == 1 ) ? 0 : (currIdx-1) * rowGroup;
    }
    public void setLimitRow(int rowGroup){
        this.limitRow = rowGroup;
    }
    public void setTotPage(boolean isSelected, int value){
        this.totPage = (isSelected) ? (int)Math.ceil((double)this.getTotCnt() / value) : value;
    }
    public void setTotPage(int totalPage){
        this.totPage = totalPage;
    }
    public void setPaging(int currIdx, int rowGroup){
        this.setStartRow(currIdx, rowGroup);
        this.setLimitRow(rowGroup);
    }
    public void setShowPaging(int totCnt, int totPage, int currIdx, int startShowRowVal, int endShowRowVal){
        this.setTotCnt(totCnt);
        this.setTotPage(totPage);
        if( currIdx > -1 ){
            this.setStartShowRow(currIdx,startShowRowVal);
            this.setEndShowRow(currIdx,endShowRowVal);
        }else{
            this.setStartShowRow(startShowRowVal);
            this.setEndShowRow(endShowRowVal);
        }
    }
    public static PageMaker getInstance(){
        return new PageMaker();
    }
}
