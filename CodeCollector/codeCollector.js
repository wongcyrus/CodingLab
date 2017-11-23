'use strict';
const AWSXRay = require('aws-xray-sdk');
const AWS = AWSXRay.captureAWS(require('aws-sdk'));
const dynamo = new AWS.DynamoDB.DocumentClient();
const s3 = new AWS.S3();
const csv = require('csvtojson');

const finalAnswerTable = process.env.finalAnswerTable;
const answerHistoryTable = process.env.answerHistroryTable;
const codeBucket = process.env.codeBucket;

const createResponse = (statusCode, body) => {
    return {
        statusCode: statusCode,
        body: body
    }
};

const addSpaceToEmptyStringElements = obj => {
    for (let prop in obj) {
        if (typeof obj[prop] === 'object') {// dive deeper in
            addSpaceToEmptyStringElements(obj[prop]);
        } else if (obj[prop] === '') {// delete elements that are empty strings
            obj[prop] = " ";
        }
    }
    return obj;
};

exports.put = (event, context, callback) => {
    console.log(JSON.stringify(event));
    console.log(event.body);

    let email;
    let finalAnswerParams;
    let answerHistoryParams;


    let dbPut = params => {
        return dynamo.put(params).promise()
    };

    let getEmail = key => {
        let getStudentEmail = key => new Promise((resolve, reject) => {
            csv({noheader: false})
                .fromFile('ApiKey.csv')
                .on('json', (jsonObj, rowIndex) => {
                    if (jsonObj.key === key) resolve(jsonObj.Name);
                }).on('done', () => {
                //parsing finished
                reject();
            });
        });
        return getStudentEmail(key);
    };

    getEmail(event.headers['x-api-key']).then((data) => {
        if (!data) {
            callback(null, createResponse(404, "ITEM NOT FOUND"));
        }
        email = data;
        let body = JSON.parse(event.body);
        let email_question = email + "-" + body.filePathName;

        body.email_question = email_question;
        finalAnswerParams = {
            TableName: finalAnswerTable,
            Item: addSpaceToEmptyStringElements(body)
        };

        let body1 = JSON.parse(event.body);
        body1.email_question_time = email + "-" + body.filePathName + "-" + Date.now();
        answerHistoryParams = {
            TableName: answerHistoryTable,
            Item: addSpaceToEmptyStringElements(body1)
        };

        return s3.putObject({
            Bucket: codeBucket,
            Key: body.lab + "-" + email_question,
            Body: event.queryStringParameters.code
        }).promise();
    }).then(c => dbPut(finalAnswerParams))
        .then(c => dbPut(answerHistoryParams))
        .then(item => {
            console.log(`PUT ITEM SUCCEEDED WITH doc = ${item}`);
            callback(null, createResponse(200, null));
        }).catch(err => {
        console.log(`PUT ITEM FAILED WITH ERROR: ${err}`);
        callback(null, createResponse(500, err));
    });
};