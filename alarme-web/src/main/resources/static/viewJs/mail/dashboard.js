(function($, dashboard){

    var myChart;

    dashboard.init = function(){
	    getChartData("INIT", "ALL");
        this.getCampSending();
        this.setEvent();
    };

    dashboard.setEvent = function(){
    	// 버튼 이벤트
    	// 2주간 현황 차트 데이터 변경(전체/대량/실시간)
        $("#2WA").on("click.setting", function() {
            // 최근 2주간 전체 캠페인 데이터
            getChartData("UPDATE", "A");
        });

        $("#2WC").on("click.setting", function() {
            // 최근 2주간 대량 캠페인 데이터
            getChartData("UPDATE", "C");
        });

        $("#2WR").on("click.setting", function() {
            // 최근 2주간 자동 캠페인 데이터
            getChartData("UPDATE", "R");
        });
    };

    dashboard.getCampSending = function(){
        var params = {};
        params.sendFlag = 30;
        params.orderType = 'regDt';
        params.limit = 6;

        var sCallBack = function(resultData){
            var $html = '';
            if( resultData.data && resultData.data.length > 0){
                resultData.data.forEach(function(elem, index){
                    console.log(elem);
                    var calc = ( elem.sendCnt == 0 ) ? 0 : Math.floor( (elem.sendCnt / elem.successCnt) * 100 )
                    $html += '<tr>';
                    $html += '<td class="text-center"><div class="photo">img</div></td>';
                    $html += '<td class="text-center">'+elem.schdlId+'</td>';
                    $html += '<td class="text-center">진행</td>';
                    $html += '<td class="text-center"><div class="progress-container progress-sm"><div class="progress"><span class="progress-value">'+calc+'%</span><div class="progress-bar" role="progressbar" aria-valuenow="60" aria-valuemin="0" aria-valuemax="100" style="width: '+calc+'%;"></div></div></div></td>';
                    $html += '</tr>';
                });
            }else{
                $html = '<tr><td class="text-center" colspan="4">발송 데이터가 존재하지 않습니다.</td></tr>'
            }
            $('div#campSending tbody').html($html);
        }

        alarmeCommon.ajaxCall('get','/mail/dashboard/campaSending',params, null,null,sCallBack,null);
        // 대기
        // setInterval(function(){
        //     alarmeCommon.ajaxCall('get','/mail/dashboard/campaSending',params, null,null,sCallBack,null);
        // }, 5000);
    }

    // 차트 데이터 조회
    function getChartData(drawType, chartType) {
        var data = {
            "chartType": chartType
        };

        $.ajax({
            method: "post",
            url: "/mail/dashboard/get2wChartData",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function(data) {
                var jData = JSON.parse(data);

                draw2wChart(drawType, chartType, jData);
            },
            error: function(jqXHR, textStatus, errorThrown) {
                console.log(jqXHR);
            }
        });
    }

    // 타입별 최근 2주간 차트 draw
    function draw2wChart(drawType, chartType, chartData) {
        console.log("chart start");

        if ("INIT" == drawType) {
            var ctx = document.getElementById("chart2w").getContext("2d");

            var gradientStroke = ctx.createLinearGradient(0,230,0,50);

            gradientStroke.addColorStop(1, 'rgba(72,72,176,0.2)');
            gradientStroke.addColorStop(0.2, 'rgba(72,72,176,0.0)');
            gradientStroke.addColorStop(0, 'rgba(119,52,169,0)'); //purple colors

            var gradientStroke1 = ctx.createLinearGradient(0,230,0,50);

            gradientStroke1.addColorStop(1, 'rgba(255,217,250,0.2)');
            gradientStroke1.addColorStop(0.2, 'rgba(255,217,250,0.0)');
            gradientStroke1.addColorStop(0, 'rgba(243,97,220,0)'); //pink

            var gradientStroke2 = ctx.createLinearGradient(0,230,0,50);

            gradientStroke2.addColorStop(1, 'rgba(250,224,212,0.2)');
            gradientStroke2.addColorStop(0.2, 'rgba(250,224,212,0.0)');
            gradientStroke2.addColorStop(0, 'rgba(242,150,97,0)'); //orange

            var gradientStroke3 = ctx.createLinearGradient(0,230,0,50);

            gradientStroke3.addColorStop(1, 'rgba(217,229,255,0.2)');
            gradientStroke3.addColorStop(0.2, 'rgba(217,229,255,0.0)');
            gradientStroke3.addColorStop(0, 'rgba(103,153,255,0)'); //blue

            var gradientStroke4 = ctx.createLinearGradient(0,230,0,50);

            gradientStroke4.addColorStop(1, 'rgba(228,247,186,0.2)');
            gradientStroke4.addColorStop(0.2, 'rgba(228,247,186,0.0)');
            gradientStroke4.addColorStop(0, 'rgba(188,229,92,0)'); //green

            var gradientStroke5 = ctx.createLinearGradient(0,230,0,50);

            gradientStroke5.addColorStop(1, 'rgba(255,216,216,0.2)');
            gradientStroke5.addColorStop(0.2, 'rgba(255,216,216,0.0)');
            gradientStroke5.addColorStop(0, 'rgba(241,95,95,0)'); //red

            var data = {
                labels: chartData.dateArray,
                datasets: [{
                    label: "발송",
                    backgroundColor: gradientStroke1,
                    borderColor: "#F361DC",
                    pointBackgroundColor: '#F361DC',
                    pointBorderColor:'rgba(255,255,255,0)',
                    pointHoverBackgroundColor: '#F361DC',
                    data: chartData.sendCntList
                }, {
                    label: "성공",
                    backgroundColor: gradientStroke2,
                    borderColor: "#F29661",
                    pointBackgroundColor: '#F29661',
                    pointBorderColor:'rgba(255,255,255,0)',
                    pointHoverBackgroundColor: '#F29661',
                    data: chartData.successCntList
                }, {
                    label: "실패",
                    backgroundColor: gradientStroke3,
                    borderColor: "#6799FF",
                    pointBackgroundColor: '#6799FF',
                    pointBorderColor:'rgba(255,255,255,0)',
                    pointHoverBackgroundColor: '#6799FF',
                    data: chartData.failCntList
                }, {
                    label: "오픈",
                    backgroundColor: gradientStroke4,
                    borderColor: "#BCE55C",
                    pointBackgroundColor: '#BCE55C',
                    pointBorderColor:'rgba(255,255,255,0)',
                    pointHoverBackgroundColor: '#BCE55C',
                    data: chartData.openCntList
                }, {
                    label: "클릭",
                    backgroundColor: gradientStroke5,
                    borderColor: "#F15F5F",
                    pointBackgroundColor: '#F15F5F',
                    pointBorderColor:'rgba(255,255,255,0)',
                    pointHoverBackgroundColor: '#F15F5F',
                    data: chartData.clickCntList
                }]
            }

            myChart = new Chart(ctx, {
                type: 'line',
                data: data
            });
        } else {
            var data = myChart.config.data;
            data.datasets[0].data = chartData.sendCntList;
            data.datasets[1].data = chartData.successCntList;
            data.datasets[2].data = chartData.failCntList;
            data.datasets[3].data = chartData.openCntList;
            data.datasets[4].data = chartData.clickCntList;
            myChart.update();
        }

    }

    dashboard.init();
})(jQuery, {});
