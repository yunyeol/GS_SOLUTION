<template>
    <div class="wrapper wrapper-full-page ">
        <div class="full-page login-page ">
            <!--   you can change the color of the filter page using: data-color="blue | purple | green | orange | red | rose " -->
            <div class="content">
                <div class="container">
                    <div class="col-lg-4 col-md-6 ml-auto mr-auto">
                        <form class="form">
                            <div class="card card-login card-white">
                                <div class="card-header">
                                    <img src="../assets/img/card-primary.png" alt="">
                                    <h1 class="card-title">Log in</h1>
                                </div>
                                <div class="card-body">
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="tim-icons icon-email-85"></i>
                                            </div>
                                        </div>
                                        <input type="text" class="form-control" placeholder="Email">
                                    </div>
                                    <div class="input-group">
                                        <div class="input-group-prepend">
                                            <div class="input-group-text">
                                                <i class="tim-icons icon-lock-circle"></i>
                                            </div>
                                        </div>
                                        <input type="text" placeholder="Password" class="form-control">
                                    </div>
                                </div>
                                <div class="card-footer">
                                    <a href="#pablo" class="btn btn-primary btn-lg btn-block mb-3">Login</a>
                                    <div class="pull-left">
                                        <h6>
                                            <a href="javascript:void(0)" class="link footer-link">Create Account</a>
                                        </h6>
                                    </div>
                                    <div class="pull-right">
                                        <h6>
                                            <a href="javascript:void(0)" class="link footer-link">Need Help?</a>
                                        </h6>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
            <footer class="footer">
                <div class="container-fluid">
                    <ul class="nav">
                        <li class="nav-item">
                            <a href="javascript:void(0)" class="nav-link">
                                Creative Tim
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="javascript:void(0)" class="nav-link">
                                About Us
                            </a>
                        </li>
                        <li class="nav-item">
                            <a href="javascript:void(0)" class="nav-link">
                                Blog
                            </a>
                        </li>
                    </ul>
                </div>
            </footer>
        </div>
    </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'login',
    components: {
  }
}
</script>
