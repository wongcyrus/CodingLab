const qrCode = require('qrcode');

exports.get = (event, context, callback) => {
    console.log(JSON.stringify(event));
    qrCode.toDataURL(event.pathParameters.jsonData, (err, url) => {
        if (err) console.err(err);
        console.log(url);

        let html = `<html><body><img width="200" height="200" src="${url}"/></body></html>`;
        let response = {
            statusCode: 200,
            headers: {
                "Content-Type": "text/html; charset=utf-8"
            },
            body: html
        };
        callback(null, response);
    });
};
