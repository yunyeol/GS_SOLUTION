<template>
    <div class="row">
        <div class="col-sm-12 col-md-5">
            <div class="dataTables_info" id="datatable_info" role="status" aria-live="polite" v-if="pageList && pageList.length > 0">Showing {{startPage}} to {{endPage}} of {{totalCnt}} entries</div>
        </div>
        <div class="col-sm-12 col-md-7">
            <div class="dataTables_paginate paging_full_numbers" id="datatable_paginate">
                <ul class="pagination" v-if="pageList && pageList.length > 0">
                    <li class="paginate_button page-item first" :class="{'disabled': this.query.currPage == 1}" id="datatable_first" @click="pageMove('first')" >
                        <a href="#" aria-controls="datatable" class="page-link">First</a>
                    </li>
                    <li class="paginate_button page-item previous" :class="{'disabled': this.query.currPage == 1}" id="datatable_previous" @click="pageMove('prev')" >
                        <a href="javascript:;" aria-controls="datatable" class="page-link">Previous</a>
                    </li>
                    <li class="paginate_button page-item" :class="{'active': query.currPage == item.startPage}" v-for="(item, index) in pageList" v-bind:key="index" @click="pageMove('',++index)">
                        <a href="#" aria-controls="datatable" class="page-link">{{item.startPage}}</a>
                    </li>
                    <li class="paginate_button page-item next" :class="{'disabled': this.query.currPage == totalPage}" id="datatable_next" @click="pageMove('next')">
                        <a href="#" aria-controls="datatable" class="page-link">Next</a>
                    </li>
                    <li class="paginate_button page-item last" :class="{'disabled': this.query.currPage == totalPage}" id="datatable_last" @click="pageMove('last')" >
                        <a href="#" aria-controls="datatable" class="page-link">Last</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script>
// @ is an alias to /src
export default {
	props: ['url','params','options','callback'],
    data : function(){
		return {
			query:{
				currPage : 1,
                rowGroup : 10
            },
            totalCnt : 0,
            totalPage : 0,
            startPage:0,
            endPage:0,
            pageList : []
		}
	},
	methods : {
		init(){
			this.getAxios();
        },
        pageMove(type, id){
            if(type === 'next'){
                this.query.currPage++;
            }else if(type === 'prev'){
                this.query.currPage--;
            }else if(type === 'first'){
                this.query.currPage = 1;
            }else if(type === 'last'){
                this.query.currPage = this.totalPage;
            }else{
                this.query.currPage = id;
            }

            this.getAxios();
        },
        search(){
            let searchQuery;
            if( this.params ){
               this.query.currPage = 1;
               searchQuery = Object.assign(this.query, this.params);
            }
            this.getAxios(searchQuery);
        },
        getAxios: async function(searchQuery){
            let self = this;
            const rv = await this.$axios({
                url: self.$API_URL+self.url,
                method: 'get',
                timeout: 3000,
                headers: {
                    'Content-Type': 'application/json'
                },
                params: this.query || searchQuery
            }).catch (err => console.error(err))
            
            if(rv && rv['data'] && rv['data'].rows.length > 0) {
                console.log(rv['data']);
                this.startPage = rv['data']['startPage'];
                this.endPage = rv['data']['endPage'];
                this.totalCnt = parseInt(rv['data'].rows[0].TOTAL_CNT);
                this.setTotalPage();
                this.setPageList([]);

                this.$emit('paginated', rv['data'].rows);
            }else{
                this.setPageList();
                this.$emit('paginated', undefined);
            }
        },
        setTotalPage(){
            this.totalPage = Math.ceil( this.totalCnt / this.query.rowGroup );
            if(this.totalPage < this.endPage){
                this.endPage = this.totalPage;
            }
        },
        setPageList(pageList){
            if( pageList ){
                let startPage = this.startPage;
                let endPage = this.endPage;
                for (startPage; startPage <= endPage; startPage++) {
                    let pageRecord = {'startPage' : startPage};
                    pageList.push(pageRecord);
                }
            }
            this.pageList = pageList || [];
        }
	},
	mounted : function(){
        this.init();
	}
}
</script>