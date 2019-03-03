/**
 * 공통 페이지 js
 */
(function($, alarmeCommon){

    /**
     * 세션 스토리지
     */
    alarmeCommon.session_storage = {
        getItem : function(key){
            var rtn = undefined;
            if( window.sessionStorage ){
                rtn = window.sessionStorage.getItem(key);
            }

            return rtn;
        },
        setItem : function(key, value){
            var rtn = undefined;
            if( window.sessionStorage ){
                rtn = window.sessionStorage.setItem(key, value);
            }

            return rtn;
        },
        removeItem : function(key){
            if( window.sessionStorage ){
                window.sessionStorage.removeItem(key);
            }
        },
        clear : function(){
            if( window.sessionStorage ){
                window.sessionStorage.clear();
            }
        },
        length : function(){
            var rtn = 0;
            if ( window.sessionStorage ) {
                rtn = window.sessionStorage.length;
            }
            return rtn;
        }
    }

    /**
     * 로컬 스토리지
     */
    alarmeCommon.local_storage = {
        getItem : function(key){
            var rtn = undefined;
            if( window.localStorage ){
                rtn = window.localStorage.getItem(key);
            }

            return rtn;
        },
        setItem : function(key, value){
            var rtn = undefined;
            if( window.localStorage ){
                rtn = window.localStorage.setItem(key, value);
            }

            return rtn;
        },
        removeItem : function(key){
            if( window.localStorage ){
                window.localStorage.removeItem(key);
            }
        },
        clear : function(){
            if( window.localStorage ){
                window.localStorage.clear();
            }
        },
        length : function(){
            var rtn = 0;
            if ( window.localStorage ) {
                rtn = window.localStorage.length;
            }
            return rtn;
        }
    }

    /**
     * ajax 처리
     */
    alarmeCommon.ajaxCall = function(type, url, JsonParam, dataType, contentType, sCallback, eCallback){
        $.ajax({
            type : type,
            url : url,
            data : JsonParam || '',
            dataType : dataType || 'json',
            contentType: contentType || 'application/json;charset=utf-8',
            timeout:3000,
            success: function(reusultData) {
                if(jQuery.isFunction(sCallback)){
                    sCallback(reusultData);
                }
            },
            error: function(data){
                console.log(data);
                if(jQuery.isFunction(eCallback)){
                    eCallback();
                }
            }
        });
    }

    /**
     * 현재스크롤 위치
     */
    alarmeCommon.getScrollNowY = function(){
        var de = document.documentElement;
        var b = document.body;
        var now_Y = document.all ? (!de.scrollTop ? b.scrollTop : de.scrollTop) : (window.pageYOffset ? window.pageYOffset : window.scrollY);

        return now_Y;

    }

    /**
     * 화면 높이값
     */
    alarmeCommon.scrollHeight = function(){
        var de = document.documentElement;
        var b = document.body;
        var scrollHeight = (de.scrollTop == 0 ? b.scrollTop : de.scrollTop) + window.screen.availHeight;

        return scrollHeight;
    }

    /**
     * 쿠키 값
     */
    alarmeCommon.getCookieValue = function(c_name) {
        var i, x, y, ARRcookies = document.cookie.split(";");
        for (i = 0; i < ARRcookies.length; i++) {
            x = ARRcookies[i].substr(0, ARRcookies[i].indexOf("="));
            y = ARRcookies[i].substr(ARRcookies[i].indexOf("=") + 1);
            x = x.replace(/^\s+|\s+$/g, "");
            if (x == c_name) {
                return unescape(y);
            }
        }
        return "0";
    }

    window.alarmeCommon = alarmeCommon;
})(jQuery, {});
