<template>
    <div class="modal modal-black fade" id="settingsModal" tabindex="-1" role="dialog" aria-labelledby="settingsModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg" role="document">
            <div class="modal-content">
                <div class="modal-header">
                <h4 class="modal-title" id="settingsModalLabel">Settings 입력</h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                    <i class="tim-icons icon-simple-remove"></i>
                </button>
            </div>
            <div class="modal-body">
                <div class="row">
                    <div class="col-sm-2">타입</div>
                    <div class="col-sm-3"><input type="text" class="form-control" placeholder="ex)0000" minlength="4" maxlength="4" v-model="type"></div>
                    <div class="col-sm-2">구분</div>
                    <div class="col-sm-3"><input type="text" class="form-control" placeholder="ex)0000" minlength="4" maxlength="4" v-model="gubun"></div>
                    <div class="col-sm-1"><button type="button" class="btn btn-primary btn-link" v-on:click="selectDuplicateSystemCode">중복체크</button></div>
                </div>
                <div class="row">
                    <div class="col-sm-2">데이터1</div>
                    <div class="col-sm-8"><input type="text" class="form-control" maxlength="256" v-model="data1"></div>
                </div>
                <div class="row">
                    <div class="col-sm-2">데이터2</div>
                    <div class="col-sm-8"><input type="text" class="form-control" maxlength="256" v-model="data2"></div>
                </div>
                <div class="row">
                    <div class="col-sm-2">데이터3</div>
                    <div class="col-sm-8"><input type="text" class="form-control" maxlength="256" v-model="data3"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary" v-on:click="postInsertSystemCode">입력</button>
            </div>
        </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "modal",
        data:function(){
          return {
              type:'',
              gubun:'',
              data1:'',
              data2:'',
              data3:'',

              duplicateCheck:false
          }
        },
        methods:{
            selectDuplicateSystemCode:function () {
                if(this.type == '' || this.gubun == ''){
                    alert("타입과 구분을 입력해주세요.");
                    return;
                }

                this.getSelectSystemCode().then((result)=>{
                    if(result.length > 0){
                        alert("해당코드는 사용중입니다.");
                    }else{
                        alert("해당코드는 사용하셔도 됩니다.");
                        this.duplicateCheck = true;
                    }
                });
            },
            getSelectSystemCode: async function () {
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/selectSystemCode',
                    method: 'get',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    params:{
                        "whereAdd":"Y",
                        "type":this.type, "gubun":this.gubun
                    }
                }).catch (err => console.error(err))

                return rv['data'];
            },
            postInsertSystemCode:async function () {
                if(this.duplicateCheck == false){
                    alert("중복체크 해주세요.");
                    return;
                }

                const rv = await this.$axios({
                    url: this.$API_URL+'/system/insertSystemCode',
                    method: 'post',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data:{
                        "type":this.type,
                        "gubun":this.gubun,
                        "data1":this.data1,
                        "data2":this.data2,
                        "data3":this.data3
                    }
                }).catch (err => console.error(err))

                return rv['data'];
            }
        }
    }
</script>

<style scoped>

</style>
