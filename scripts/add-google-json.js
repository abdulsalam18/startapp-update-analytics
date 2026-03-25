#!/usr/bin/env node

module.exports = function (ctx) {

    const fs = require('fs');
    const path = require('path');

    const root = ctx.opts.projectRoot;

    // source file (plugin এর ভিতরে)
    const source = path.join(ctx.opts.plugin.dir,
        'res/android/google-services.json');

    // destination (correct place)
    const dest = path.join(root,
        'platforms/android/app/google-services.json');

    if (!fs.existsSync(source)) {
        console.log("❌ Plugin google-services.json not found");
        return;
    }

    try {

        // read from plugin
        const data = fs.readFileSync(source, 'utf8');

        // write directly to app/
        fs.writeFileSync(dest, data, 'utf8');

        console.log("✅ google-services.json added to app/");

    } catch (e) {
        console.log("❌ Error:", e);
    }

};
