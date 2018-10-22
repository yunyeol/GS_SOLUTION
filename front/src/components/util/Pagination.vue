<template>
        <nav aria-label="Page navigation example">
            <ul class="pagination">
                <li class="page-item">
                <template v-if="pageList && pageList.length > 0">
                    <a class="page-link" href="#" aria-label="Previous" @click="search('prev')" v-if="endPage > query.pageGroup"  >
                        <span aria-hidden="true">&laquo;</span>
                        <span class="sr-only">Previous</span>
                    </a>
                </template>
                </li>
                <template v-if="pageList && pageList.length > 0">
                    <li class="page-item" :class="{'active': query.currPage == item.startPage}" v-for="(item, index) in pageList" @click="search('',item.startPage)">
                        <a class="page-link" href="#" >{{item.startPage}}</a>
                    </li>
                </template>    
                
                <template v-if="pageList && pageList.length > 0">
                    <a class="page-link" aria-label="Next" @click="search('next')" v-if="endPage < totalPage">
                        <span aria-hidden="true">&raquo;</span>
                        <span class="sr-only">Next</span>
                    </a>
                </template>
                </li>
            </ul>
        </nav>
</template>

<script>
// @ is an alias to /src
export default {
	props: ['url','params','callback','options'],
    data : function(){
		return {
			query:{
				currPage : 1,
				pageGroup : 10
            },
            totalPage : 0,
            startPage:0,
            endPage:0,
            pageList : []
		}
	},
	methods : {
		init : function(){
			this.getAxios();
        },
        search(type, id){
            if(type === 'next'){
                this.query.currPage = this.endPage+1;
            }else if(type === 'prev'){
                this.query.currPage = this.startPage-1;
            }else{
                this.query.currPage = id;
            }

            this.getAxios();
        },
        getAxios: async function(){
            let self = this;
            const rv = await this.$axios({
                url: self.$API_URL+self.url,
                method: 'get',
                timeout: 5000,
                headers: {
                    'Content-Type': 'application/json'
                },
                params: this.query
            }).catch (err => console.error(err))
            
            if(rv && rv['data']) {
                this.startPage = rv['data']['startPage'];
                this.endPage = rv['data']['endPage'];
                this.totalCnt = parseInt(rv['data'].rows[0].TOTAL_CNT);
                this.setTotalPage();
                this.setPageList([]);

                this.$emit('paginated', rv['data'].rows);
            }else{
                this.setPageList();
            }
        },
        setTotalPage(){
            this.totalPage = parseInt( this.totalCnt / this.query.pageGroup );
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