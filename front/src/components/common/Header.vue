<template>
<nav class="navbar navbar-expand-md fixed-top navbar-dark bg-dark">
      <router-link class="navbar-brand" to="/"  >G-MAS</router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active" @click="menuActive">
            <router-link class="nav-link" to="/"  >Home <span class="sr-only">(current)</span></router-link>
          </li>
          <li class="nav-item" @click="menuActive">
            <router-link class="nav-link" to="/amchart">amchart <span class="sr-only">(current)</span></router-link>
          </li>
          <li class="nav-item" @click="menuActive">
            <router-link class="nav-link" to="/todo" >todo <span class="sr-only">(current)</span></router-link>
          </li>
          <li class="nav-item">
            <a class="nav-link disabled" >Disabled</a>
          </li>
          <li class="nav-item dropdo
          
          wn" >
            <a class="nav-link dropdown-toggle" href="http://example.com" id="dropdown01" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" @click="menuActive">Dropdown</a>
            <div class="dropdown-menu" aria-labelledby="dropdown01" >
              <a class="dropdown-item" href="#">Action</a>
              <a class="dropdown-item" href="#">Another action</a>
              <a class="dropdown-item" href="#">Something else here</a>
            </div>
          </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
          <input class="form-control mr-sm-2" type="text" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>
      </div>
    </nav>
</template>

<script>
export default {
  name: 'Header',
  data(){
    return{
      menuList : [
        {}
      ]
    }
  },
  methods:{
    init : function(){
    },
    menuActive : function(e){
      let target = $(e.target).parent();
      target.siblings().removeClass('active');
      target.addClass('active');
    }
  },
  mounted(){
    this.init();
  }
}
</script>
