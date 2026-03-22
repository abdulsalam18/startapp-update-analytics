module.exports = function (context) {

    const fs = require('fs');
    const path = require('path');

    const filePath = path.join(
        context.opts.projectRoot,
        'platforms/android/app/src/main/res/xml/cdv_core_file_provider_paths.xml'
    );

    if (!fs.existsSync(filePath)) return;

    let xml = fs.readFileSync(filePath, 'utf8');

    if (!xml.includes('external-path')) {

        xml = xml.replace('</paths>', '    <external-path name="apk" path="." />\n</paths>');

        fs.writeFileSync(filePath, xml);
        console.log('external-path added');
    }
};