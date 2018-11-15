<template>
  <modal name="dynamic-modal" width="350" height="400" @before-open="beforeOpen" @before-close="beforeClose">
    <div class="size-modal-content">
      <div>A new paragraph will be added every 5 sec to show how <b>height</b> scales.</div>
      <div>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla dictum purus egestas libero ornare venenatis.
        Maecenas pharetra tortor eu tortor imperdiet, a faucibus quam finibus. Nulla id lacinia quam.
        Praesent imperdiet sed magna non finibus. Aenean blandit, mauris vitae lacinia rutrum,
        nunc mi scelerisque sem, in laoreet sem lectus ut orci. Ut egestas nulla in vehicula feugiat.
        Vivamus tincidunt nisi vel risus dictum suscipit. Nulla id blandit mi, vulputate aliquam enim.
      </div>
      <div class="row col-md-6">
        <input class="btn btn-default col-md-3" @click="fnDialog('알림','테스트')" type="button" value="Alert"> 
        <input class="btn btn-default col-md-3" @click="close" type="button" value="닫기">
      </div>
    </div>
</modal>
</template>
<script>
  export default {
    name: 'SizeModalTest',
    data () {
      return {
        paragraphs: [true],
        timer: null,
        todoList : []
      }
    },
    methods: {
      beforeOpen (e) {
        console.log('beforeOpen !!!');
        //파라미터
        this.todoList = e.params.todoList;
      },
      beforeClose () {},
      close () {
        console.log('close');
        this.$modal.hide('dynamic-modal');
      },
      fnDialog (titl, txt) { 
          this.$modal.show('dialog', {
              title: titl,
              text: txt,
              buttons: [
                  {
                  title: 'Close'
                  }
              ]
          })
      }
    }
  }
</script>
<style>
  .size-modal-content {
    padding: 10px;
    font-style: 13px;
  }
  .v--modal-overlay[data-modal="size-modal"] {
    background: rgba(0, 0, 0, 0.5);
  }
  .demo-modal-class {
    border-radius: 5px;
    background: #F7F7F7;
    box-shadow: 5px 5px 30px 0px rgba(46,61,73, 0.6);
  }
</style>