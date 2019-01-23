(function($, campaign){

	campaign.init = function(){
        this.setEvent();
    };

    campaign.setEvent = function(){
        $('button[name="campaignSetting"]').on('click.setting', function() {
        	var schdlId = $(this).attr("schdlId");
        	location.href="/mail/send/campaign/setting?schdlId="+schdlId;
        });
    };

    campaign.init();
})(jQuery, {});
