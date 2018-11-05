<template>
    <div class="col-lg-3 col-md-6">
        <div class="card card-stats">
            <div class="card-body">
                <div class="row">
                    <div class="col-5">
                        <div class="info-icon text-center icon-danger">
                            <i class="tim-icons icon-molecule-40"></i>
                        </div>
                    </div>
                    <div class="col-7">
                        <div class="numbers">
                            <p class="card-category">Errors</p>
                            <h3 class="card-title">12</h3>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card-footer">
                <hr>
                <div class="stats">
                    <i class="tim-icons icon-watch-time"></i> In the last hours
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "DailyCount"
    }
</script>

<style scoped>

</style>
