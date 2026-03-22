var exec = require('cordova/exec');

exports.logEvent = function(name, params, success, error) {
    exec(success, error, "FirebaseAnalyticsPlugin", "logEvent", [name, params]);
};