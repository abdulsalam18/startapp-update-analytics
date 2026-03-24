module.exports = function (ctx) {

    const fs = require('fs');
    const path = require('path');

    const rootConfig = path.join(ctx.opts.projectRoot, 'config.xml');

    if (!fs.existsSync(rootConfig)) return;

    let xml = fs.readFileSync(rootConfig, 'utf8');

    // update version + add android-versionCode in same line
    xml = xml.replace(
        /<widget([^>]*)version="([^"]*)"/,
        '<widget$1version="1.0.0" android-versionCode="100"'
    );

    fs.writeFileSync(rootConfig, xml);

    console.log("✅ Root config.xml updated");

    // Android platform config
    const androidConfig = path.join(
        ctx.opts.projectRoot,
        'platforms/android/app/src/main/res/xml/config.xml'
    );

    if (fs.existsSync(androidConfig)) {
        let xml2 = fs.readFileSync(androidConfig, 'utf8');

        xml2 = xml2.replace(
            /<widget([^>]*)version="([^"]*)"/,
            '<widget$1version="1.0.0" android-versionCode="100"'
        );

        fs.writeFileSync(androidConfig, xml2);

        console.log("✅ Android config.xml updated");
    }

};
