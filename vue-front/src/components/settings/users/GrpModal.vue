<template>
    <div class="modal modal-black fade" id="grpModal" tabindex="-1" role="dialog" aria-labelledby="authModalLabel" aria-hidden="true" >
        <div class="modal-dialog  modal-sm" role="document">
            <div class="modal-content">
                <div class="modal-header">
                    <h4 class="modal-title" id="authModalLabel">그룹 수정</h4>
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                        <i class="tim-icons icon-simple-remove"></i>
                    </button>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="col-sm-4">그룹</div>
                        <div class="col-sm-8">
                            <select  v-model="selected" class="" name="grp"  data-style="select-with-transition" ref="select" @change="">
                                <option v-for="list in grpList"></option>
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
        name: "GrpModal",
        props:[

        ],
        data: function () {
            return {
                grpList : [],
                selected : ''
            }
        },
        updated () {
            $(this.$refs.select)
                .selectpicker({title:this.checkedAuthName})
                .selectpicker('render');
        },
        methods : {

        },
        created : function(){
        },
        mounted : function () {
        }
    }
</script>

<style scoped>

</style>
