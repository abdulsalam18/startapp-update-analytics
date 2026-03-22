var exec = require('cordova/exec');

var startapp = {

    loadInterstitial: function(){
        exec(null,null,"StartAppAds","loadInterstitial",[]);
    },

    showInterstitial: function(){
        exec(null,null,"StartAppAds","showInterstitial",[]);
    },

    showReward: function(){
        exec(null,null,"StartAppAds","showReward",[]);
    },

    showBanner: function(){
        exec(null,null,"StartAppAds","showBanner",[]);
    },

    hideBanner: function(){
        exec(null,null,"StartAppAds","hideBanner",[]);
    },

    rewardComplete: function(){
        console.log("Reward completed");
    },

    onAdEvent: function(event){
        console.log("StartApp Event: " + event);
    }

};

module.exports = startapp;