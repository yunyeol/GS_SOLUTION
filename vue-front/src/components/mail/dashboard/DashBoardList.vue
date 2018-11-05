<template>
    <div class="card">
        <div class="card-header">
            <div class="tools float-right">
                <div class="dropdown">
                    <button type="button" class="btn btn-link dropdown-toggle btn-icon" data-toggle="dropdown">
                        <i class="tim-icons icon-settings-gear-63"></i>
                    </button>
                    <div class="dropdown-menu dropdown-menu-right">
                        <a class="dropdown-item" href="#pablo">Action</a>
                        <a class="dropdown-item" href="#pablo">Another action</a>
                        <a class="dropdown-item" href="#pablo">Something else</a>
                        <a class="dropdown-item text-danger" href="#pablo">Remove Data</a>
                    </div>
                </div>
            </div>
            <h5 class="card-title">Management Table</h5>
        </div>
        <div class="card-body">
            <div class="table-responsive ps">
                <table class="table">
                    <thead class="text-primary">
                    <tr>
                        <th class="text-center">
                            #
                        </th>
                        <th>
                            Name
                        </th>
                        <th>
                            Job Position
                        </th>
                        <th>
                            Milestone
                        </th>
                        <th class="text-right">
                            Salary
                        </th>
                        <th class="text-right">
                            Actions
                        </th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td class="text-center">
                            <div class="photo">
                                <img src="../../../assets/img/tania.jpg" alt="photo">
                            </div>
                        </td>
                        <td>
                            Tania Mike
                        </td>
                        <td>
                            Develop
                        </td>
                        <td class="text-center">
                            <div class="progress-container progress-sm">
                                <div class="progress">
                                    <span class="progress-value">25%</span>
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 25%;"></div>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            € 99,225
                        </td>
                        <td class="text-right">
                            <button type="button" rel="tooltip" class="btn btn-success btn-link btn-icon btn-sm btn-neutral" data-original-title="Refresh" title="">
                                <i class="tim-icons icon-refresh-01"></i>
                            </button>
                            <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-icon btn-sm btn-neutral" data-original-title="Delete" title="">
                                <i class="tim-icons icon-simple-remove"></i>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-center">
                            <div class="photo">
                                <img src="../../../assets/img/robi.jpg" alt="photo">
                            </div>
                        </td>
                        <td>
                            John Doe
                        </td>
                        <td>
                            CEO
                        </td>
                        <td class="text-center">
                            <div class="progress-container progress-sm">
                                <div class="progress">
                                    <span class="progress-value">77%</span>
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 77%;"></div>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            € 89,241
                        </td>
                        <td class="text-right">
                            <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm btn-icon btn-neutral" data-original-title="Refresh" title="">
                                <i class="tim-icons icon-refresh-01"></i>
                            </button>
                            <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon btn-neutral" data-original-title="Delete" title="">
                                <i class="tim-icons icon-simple-remove"></i>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-center">
                            <div class="photo">
                                <img src="../../../assets/img/lora.jpg" alt="photo">
                            </div>
                        </td>
                        <td>
                            Alexa Mike
                        </td>
                        <td>
                            Design
                        </td>
                        <td class="text-center">
                            <div class="progress-container progress-sm">
                                <div class="progress">
                                    <span class="progress-value">41%</span>
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 41%;"></div>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            € 92,144
                        </td>
                        <td class="text-right">
                            <button type="button" rel="tooltip" class="btn btn-success btn-link btn-icon btn-sm btn-neutral" data-original-title="Refresh" title="">
                                <i class="tim-icons icon-refresh-01"></i>
                            </button>
                            <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-icon btn-sm btn-neutral" data-original-title="Delete" title="">
                                <i class="tim-icons icon-simple-remove"></i>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-center">
                            <div class="photo">
                                <img src="../../../assets/img/jana.jpg" alt="photo">
                            </div>
                        </td>
                        <td>
                            Jana Monday
                        </td>
                        <td>
                            Marketing
                        </td>
                        <td class="text-center">
                            <div class="progress-container progress-sm">
                                <div class="progress">
                                    <span class="progress-value">50%</span>
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 50%;"></div>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            € 49,990
                        </td>
                        <td class="text-right">
                            <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm btn-icon" data-original-title="Refresh" title="">
                                <i class="tim-icons icon-refresh-01"></i>
                            </button>
                            <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon" data-original-title="Delete" title="">
                                <i class="tim-icons icon-simple-remove"></i>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-center">
                            <div class="photo">
                                <img src="../../../assets/img/mike.jpg" alt="photo">
                            </div>
                        </td>
                        <td>
                            Paul Dickens
                        </td>
                        <td>
                            Develop
                        </td>
                        <td class="text-center">
                            <div class="progress-container progress-sm">
                                <div class="progress">
                                    <span class="progress-value">100%</span>
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 100%;">
                                    </div>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            € 69,201
                        </td>
                        <td class="text-right">
                            <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm  btn-icon" data-original-title="Refresh" title="">
                                <i class="tim-icons icon-refresh-01"></i>
                            </button>
                            <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon" data-original-title="Delete" title="">
                                <i class="tim-icons icon-simple-remove"></i>
                            </button>
                        </td>
                    </tr>
                    <tr>
                        <td class="text-center">
                            <div class="photo">
                                <img src="../../../assets/img/emilyz.jpg" alt="photo">
                            </div>
                        </td>
                        <td>
                            Manuela Rico
                        </td>
                        <td>
                            Manager
                        </td>
                        <td class="text-center">
                            <div class="progress-container progress-sm">
                                <div class="progress">
                                    <span class="progress-value">15%</span>
                                    <div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: 15%;"></div>
                                </div>
                            </div>
                        </td>
                        <td class="text-right">
                            € 99,201
                        </td>
                        <td class="text-right">
                            <button type="button" rel="tooltip" class="btn btn-success btn-link btn-sm btn-icon" data-original-title="Refresh" title="">
                                <i class="tim-icons icon-refresh-01"></i>
                            </button>
                            <button type="button" rel="tooltip" class="btn btn-danger btn-link btn-sm btn-icon" data-original-title="Delete" title="">
                                <i class="tim-icons icon-simple-remove"></i>
                            </button>
                        </td>
                    </tr>
                    </tbody>
                </table>
                <div class="ps__rail-x" style="left: 0px; bottom: 0px;"><div class="ps__thumb-x" tabindex="0" style="left: 0px; width: 0px;"></div></div><div class="ps__rail-y" style="top: 0px; right: 0px;"><div class="ps__thumb-y" tabindex="0" style="top: 0px; height: 0px;"></div></div></div>
        </div>
    </div>
</template>

<script>
    export default {
        name: "DashBoardList"
    }
</script>

<style scoped>

</style>
