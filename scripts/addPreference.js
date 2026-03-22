module.exports = function (context) {

    const fs = require('fs');
    const path = require('path');

    const configPath = path.join(context.opts.projectRoot, 'config.xml');

    let xml = fs.readFileSync(configPath, 'utf8');

    if (xml.includes('STARTAPP_APP_ID')) {
        console.log('Already added');
        return;
    }

    const insert = '<preference name="STARTAPP_APP_ID" value="205489527" />';

    xml = xml.replace('</widget>', '    ' + insert + '\n</widget>');

    fs.writeFileSync(configPath, xml);

    console.log('STARTAPP_APP_ID added to root config.xml');
};