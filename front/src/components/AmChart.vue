<template>
    <div id="container" style="width: 100%; height: 400px;" ref="chartdiv"></div>
</template>
 
<script>
    import 'amcharts3'
    import AmSerial from 'amcharts3/amcharts/serial'
    
    export default {
        name: 'AmChart',
        data:function(){ 
            return {
                options : {}
            }
        },
        methods:{
            deleteTodo:function(index){
                this.todos.splice(index,1);
            },
            createTodo:function(val){
                this.todos.push({name: val});
            }
        },
        mounted:function(){
            this.options = {
                "type": "serial",
                "categoryField": "type",
                "chartCursor": {},
                "graphs": [
                    {
                        "type": "column",
                        "title": "Pizza types",
                        "valueField": "sold",
                        "fillAlphas": 0.8
                    }
                ],
                "dataProvider": [
                    { "type": "Margherita", "sold": 120 },
                    { "type": "Funghi", "sold": 82 },
                    { "type": "Capricciosa", "sold": 78 },
                    { "type": "Quattro Stagioni", "sold": 71 }
                ]
            }
            AmCharts.makeChart(this.$refs.chartdiv, this.options);
        }
    }
</script>