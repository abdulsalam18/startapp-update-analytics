#!/usr/bin/env node

module.exports = function (ctx) {

    const fs = require('fs');
    const path = require('path');

    const configPath = path.join(ctx.opts.projectRoot, 'config.xml');

    if (!fs.existsSync(configPath)) {
        console.log("❌ config.xml not found");
        return;
    }

    let xml = fs.readFileSync(configPath, 'utf8');

    // already exists check
    if (xml.includes('GradlePluginGoogleServicesEnabled')) {
        console.log("✅ Preference already exists");
        return;
    }

    // insert before </widget>
    xml = xml.replace(
        '</widget>',
        '    <preference name="GradlePluginGoogleServicesEnabled" value="true" />\n</widget>'
    );

    fs.writeFileSync(configPath, xml, 'utf8');

    console.log("✅ Preference added to config.xml");

};
