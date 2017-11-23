'use strict';
const AWS = require('aws-sdk');
const apigateway = new AWS.APIGateway();
const fs = require('fs');
const response = require('cfn-response');

const usagePlanId = process.env.usagePlanId;

exports.handler = (event, context, callback) => {
    console.log(JSON.stringify(event, null, '  '));
    if (event.RequestType === 'Delete') {
        let getKeyJson = () => new Promise((resolve, reject) => {
            let params = {
                usagePlanId
            };
            apigateway.getUsagePlanKeys(params, (err, data) => {
                if (err) reject(err); // an error occurred
                else resolve(data.items.map(c => c.id));           // successful response
            });
        });

        let deleteKey = apiKey => new Promise((resolve, reject) => {
            let params = {
                apiKey
            };
            console.log(params);
            apigateway.deleteApiKey(params, function (err, data) {
                if (err) reject(err); // an error occurred
                else resolve(data);           // successful response
            });
        });

        getKeyJson().then(keys => {
            console.log(keys);
            return Promise.all(keys.map(deleteKey));
        }).then(data => {
            console.log(data);
            response.send(event, context, response.SUCCESS);
            callback(null, "deleteApiKey OK!");
        }).catch(err => {
            console.log(err);
            response.send(event, context, response.SUCCESS);
            callback(null, err);
        });
    } else if (event.RequestType === 'Create') {
        let csv = fs.readFileSync('ApiKey.csv', 'utf8');
        csv = csv.replace(/dummyUsagePlanIds/g, usagePlanId);
        console.log(csv);
        let params = {
            body: csv, /* required */
            format: "csv", /* required */
            failOnWarnings: false
        };
        apigateway.importApiKeys(params, (err, data) => {
            if (err) console.log(err, err.stack); // an error occurred
            else console.log(data);           // successful response
            response.send(event, context, response.SUCCESS);
            callback(null, "importApiKeys OK!");
        });

    } else {
        console.log(event.RequestType);
        response.send(event, context, response.SUCCESS);
        callback(null, "Do Nothing");
    }
};
