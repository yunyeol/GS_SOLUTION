<template>
    <div class="row">
        <div class="col-sm-12 col-md-5">
            <div class="dataTables_info" id="datatable_info" role="status" aria-live="polite">Showing {{startPage}} to {{endPage}} of {{totalCnt}} entries</div>
        </div>
        <div class="col-sm-12 col-md-7">
            <div class="dataTables_paginate paging_full_numbers" id="datatable_paginate">
                <ul class="pagination" >
                    <li class="paginate_button page-item first"  id="datatable_first" :class="{'disabled': currentPage+1 == 1}"  v-on:click="pageMove('first','')">
                        <a href="#" aria-controls="datatable" class="page-link" >First</a>
                    </li>
                    <li class="paginate_button page-item previous" id="datatable_previous"  :class="{'disabled': currentPage+1 == 1}" v-on:click="pageMove('previous','')">
                        <a href="#" aria-controls="datatable" class="page-link">Previous</a>
                    </li>
                    <li class="paginate_button page-item" :class="{'active':currentPage+1 == item.page}" v-for="(item, index) in pageList" v-bind:key="index" >
                        <a href="#" aria-controls="datatable" class="page-link" v-on:click="pageMove('page',item)">{{item.page}}</a>
                    </li>
                    <li class="paginate_button page-item next"  id="datatable_next" :class="{'disabled':currentPage+1 == totalPage}" v-on:click="pageMove('next','')">
                        <a href="#" aria-controls="datatable" class="page-link">Next</a>
                    </li>
                    <li class="paginate_button page-item last" id="datatable_last" :class="{'disabled':currentPage+1 == totalPage}" v-on:click="pageMove('last','')">
                        <a href="#" aria-controls="datatable" class="page-link">Last</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "paging",
        props:[
            'url',
            'listMethodName',
            'keyWord',
            'option'
        ],
        data:function(){
            return {
                totalCnt:[],
                totalPage:[],
                pageList:[],

                currentPage:0,
                startPage:0,
                endPage:this.option
            }
        },
        methods:{
            getTotalpageData : async function () {
                let self = this;
                const rv = await this.$axios({
                    url: self.$API_URL + self.url,
                    method: 'get',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{
                        "keyWord" : this.keyWord
                    }
                }).catch (err => console.error(err));

                if(rv && rv['data'] && this.option > 0) {
                    this.totalCnt = rv['data'];
                    this.totalPage = rv['data'] % this.option == 0 ? parseInt(rv['data']/this.option) : parseInt(rv['data']/this.option) + 1;
                }else{
                    this.totalCnt = rv['data'];
                    this.totalPage = 1;
                }

                this.setPageList(this.totalPage);
                this.$emit('startPage', this.startPage);
                this.setStartEndPage();
            },
            setPageList(totalPage){
                var pageList = [];
                if( totalPage > 0 ){
                    for (var i=0; i<totalPage; i++) {
                        let page = {'page' : i+1};
                        pageList.push(page);
                    }
                }
                this.pageList = pageList || [];
            },
            setStartEndPage(){
                if(this.totalCnt < this.option){
                    this.startPage = 1;
                    this.endPage = this.totalCnt % this.option;
                    this.currentPage = 0;

                }else if(this.currentPage +1 < this.totalPage){
                    this.startPage = this.currentPage * this.option +1;
                    this.endPage = (this.currentPage +1)* this.option;

                }else if(this.totalPage <= this.currentPage && this.option > 0){
                    this.currentPage = 0;
                    this.startPage = 1;
                    this.endPage = (this.currentPage +1)* this.option;

                }else{
                    if(this.option < 0){
                        this.currentPage = 0;
                        this.startPage = 1;
                        this.endPage = this.totalCnt;

                    }else{
                        this.startPage = this.currentPage * this.option +1;
                        if(this.totalCnt%this.option == 0){
                            this.endPage = (this.currentPage+1)* this.option + this.totalCnt%this.option;
                        }else{
                            this.endPage = (this.currentPage)* this.option + this.totalCnt%this.option;
                        }
                    }
                }
            },
            pageMove(gubun, item){
                var startPage = 0;

                if(gubun == 'page'){
                    startPage = (item.page -1) * this.option;
                    this.currentPage = item.page -1;

                    this.$emit(this.listMethodName, this.option, startPage);
                }else if(gubun == 'first'){
                    startPage = 0;
                    this.currentPage = 0;

                    this.$emit(this.listMethodName, this.option, startPage);
                }else if(gubun == 'previous'){
                    var page = --this.currentPage;
                    if(page > 0){
                        startPage = (page) * this.option;

                        this.$emit(this.listMethodName, this.option, startPage);
                    }else{
                        startPage = 0;
                        this.currentPage = 0;

                        this.$emit(this.listMethodName, this.option, startPage);
                    }
                }else if(gubun == 'next'){
                    var page = ++this.currentPage;

                    if(page < this.totalPage){
                        startPage = (page) * this.option;

                        this.$emit(this.listMethodName, this.option, startPage);
                    }else{
                        startPage = (this.totalPage -1) * this.option;
                        this.currentPage = this.totalPage -1;

                        this.$emit(this.listMethodName, this.option, startPage);
                    }
                }else if(gubun == 'last'){
                    startPage = (this.totalPage -1) * this.option;
                    this.currentPage = this.totalPage -1;

                    this.$emit(this.listMethodName, this.option, startPage);
                }

                this.$emit('getStartPage', startPage);
                this.setStartEndPage();
            }
        },
        mounted : function(){
            this.getTotalpageData();
        }
    }
</script>

<style scoped>

</style>
