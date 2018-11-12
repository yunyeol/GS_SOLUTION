<template>
    <div class="row">
        <div class="col-sm-12 col-md-5">
            <div class="dataTables_info" id="datatable_info" role="status" aria-live="polite">Showing  to  of  entries</div>
        </div>
        <div class="col-sm-12 col-md-7">
            <div class="dataTables_paginate paging_full_numbers" id="datatable_paginate">
                <ul class="pagination" >
                    <li class="paginate_button page-item first"  id="datatable_first" >
                        <a href="#" aria-controls="datatable" class="page-link">First</a>
                    </li>
                    <li class="paginate_button page-item previous" id="datatable_previous"  >
                        <a href="#" aria-controls="datatable" class="page-link">Previous</a>
                    </li>
                    <li class="paginate_button page-item" v-if="totalCntList && totalCntList.length > 0" v-for="(list, index) in totalCntList" v-bind:key="index" >
                        <a href="#" aria-controls="datatable" class="page-link">{{totalPage}}</a>
                    </li>
                    <li class="paginate_button page-item next"  id="datatable_next" >
                        <a href="#" aria-controls="datatable" class="page-link">Next</a>
                    </li>
                    <li class="paginate_button page-item last" id="datatable_last"  >
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
            'totalCntList'
        ],
        data:function(){
            return {
                //totalPageCnt : this.totalCntList,
                pageList : [],
                totalPage : []
            }
        },
        methods:{
            getTotalPage : function(){

                console.log(this.totalCntList.length);

                for(var i=0; i<this.totalCntList.length; i++){
                    console.log("01 : "+this.totalCntList[i].CNT);
                }

                console.log("1 : "+this.totalCnt);
                console.log("2 : "+this.totalPageCnt);


                //this.totalPage = this.totalCntList[0].CNT % 10 == 0 ? this.totalCntList[i].CNT/10 : this.totalCntList[i].CNT/10 + 1;
            }
        },
        mounted : function(){
            this.getTotalPage();
        }
    }
</script>

<style scoped>

</style>