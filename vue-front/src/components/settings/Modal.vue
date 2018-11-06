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
                    <div class="col-sm-3"><input type="text" class="form-control" placeholder="ex)0000"></div>
                    <div class="col-sm-2">구분</div>
                    <div class="col-sm-3"><input type="text" class="form-control" placeholder="ex)0000"></div>
                    <div class="col-sm-1"><button type="button" class="btn btn-primary btn-link">중복체크</button></div>
                </div>
                <div class="row">
                    <div class="col-sm-2">데이터1</div>
                    <div class="col-sm-8"><input type="text" class="form-control" ></div>
                </div>
                <div class="row">
                    <div class="col-sm-2">데이터2</div>
                    <div class="col-sm-8"><input type="text" class="form-control" ></div>
                </div>
                <div class="row">
                    <div class="col-sm-2">데이터3</div>
                    <div class="col-sm-8"><input type="text" class="form-control" ></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                <button type="button" class="btn btn-primary">입력</button>
            </div>
        </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "modal"
    }
</script>

<style scoped>

</style>
