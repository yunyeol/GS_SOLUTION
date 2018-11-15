<template>
    <div class="modal modal-black fade" id="authModal" tabindex="-1" role="dialog" aria-labelledby="authModalLabel" aria-hidden="true" >
        <div class="modal-dialog  modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="authModalLabel">권한 수정</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <i class="tim-icons icon-simple-remove"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-4">권한</div>
                        <div class="col-sm-8">
                            <select  v-model="selected" class="" name="auth"  data-style="select-with-transition" ref="select" @change="updateAuth  ">
                                <option v-for="list in authList" :value="list.MBR_AUTH_ID">{{list.AUTH_NAME}}</option>
                            </select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "AuthModal",
        props:[
            'checkedAuthName',
            'checkedAuthId',
            'loginId'
        ],
        data: function () {
            return {
                authList : [],
                selected : ''
            }
        },
        updated () {
            $(this.$refs.select)
                .selectpicker({title:this.checkedAuthName})
                .selectpicker('render');
        },
        methods : {
            getAuth : async function () {
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/users/auth',
                    method: 'get',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).catch (err => console.error(err));

                if(rv && rv['data']) {
                    this.authList = rv['data'];
                }
            },
            updateAuth:async  function(){
                const rv = await this.$axios({
                    url: this.$API_URL+'/system/users/auth',
                    method: 'put',
                    timeout: 3000,
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    data:{
                        "loginId" : this.loginId,
                        "mbrAuthId" : this.selected
                    }
                }).catch (err => console.error(err));

                this.$emit('refresh');
            }
        },
        created : function(){
            this.getAuth();
        },
        mounted : function () {
        }
    }
</script>

<style scoped>

</style>
