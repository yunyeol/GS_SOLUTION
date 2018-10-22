<template>
    <div class="container">
        <h2> Todo List </h2>
        <div class ="input-group" style="margin-bottom:10px;">
            <input type="text" class="form-control" placeholder="검색을 해주세요." v-model="str" @keyup.enter ="createTodo(str)">
            <span class="input-group-btn">
                <button class="btn btn-default" type="button" @click="createTodo(str)"> 검색 </button>
                <button class="btn btn-default" type="button" @click="popupShow"> Alert 테스트 공용 </button>
                <button class="btn btn-default" type="button" @click="fnDynamicModal"> DynamicModal 팝업 </button>
            </span>
        </div>
        <div class="container">
            <div class="row">
                <div class="col-sm">
                JOB_INSTANCE_ID
                </div>
                <div class="col-sm">
                JOB_EXECUTION_ID
                </div>
                <div class="col-sm">
                CREATE_TIME
                </div>
            </div>
            <div class="row" v-for="(item, index) in todoList">
                <div class="col-sm">
                    {{item.JOB_INSTANCE_ID}}
                </div>
                <div class="col-sm">
                    {{item.JOB_EXECUTION_ID}}
                </div>
                <div class="col-sm">
                    {{item.CREATE_TIME}}
                </div>
            </div>
        </div>
        <Pagination :url="'/list'" @paginated="paginated"></Pagination>
        <dynamic-modal></dynamic-modal>
    </div>
</template> 
<script>
    import Vue from 'vue'
    import DynamicModal from '@/components/popup/DynamicModal.vue'
    import Pagination from '@/components/util/Pagination.vue'
    Vue.component('dynamic-modal',DynamicModal);
    Vue.component('Pagination',Pagination);

    export default {
        name: 'TodoPage',
        data(){ 
            return {
                str:null,
                todoList: []
            }
        },
        methods:{
            popupShow:function(){
                this.fnDialog('알림','팝업테스트야');
            },
            fnDialog:function(titl, txt){
                this.$modal.show('dialog', {
                    title: titl,
                    text: txt,
                    buttons: [
                        {
                        title: 'Close'
                        }
                    ]
                })
            },
            fnDynamicModal:function(){
                console.log('동적 모달 팝업 파라미터 넘기기용');
                this.$modal.show('dynamic-modal',{
                    openType : 'qwe',
                    todoList : this.todos
                });
            },
            getAxios: async function(){
                const rv = await this.$axios({
                    url: this.$API_URL+'/test',
                    method: 'get',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{ test:1, test1: 't' }
                }).catch (err => console.error(err))
                
                if(rv && rv['data']) {
                    console.log(rv['data']);
                }
            },
            paginated(data){
                this.todoList = data;
            }
        },
        mounted:function(){
            this.getAxios();
        }
    }
</script>