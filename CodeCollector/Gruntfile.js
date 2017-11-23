//Follow https://medium.com/@SlyFireFox/micro-services-with-aws-lambda-and-api-gateway-part-1-f11aaaa5bdef
//Run
//npm install -g grunt-cli
//npm install grunt-aws-lambda grunt-pack --save-dev

const grunt = require('grunt');
grunt.loadNpmTasks('grunt-aws-lambda');

grunt.initConfig({
    lambda_invoke: {
        default: {}
    },
    lambda_deploy: {
        codeCollector: {
            arn: 'arn:aws:lambda:us-east-1:894598711988:function:CodeCollector-AnswerCollectorFunction-1X5XCC21U7AX3',
            options: {
                region: 'us-east-1',
                handler: 'codeCollector.put'
            }
        }
    },
    lambda_package: {
        codeCollector: {
            options: {
                include_time: false,
                include_version: false
            }
        }
    }
});

grunt.registerTask('deploy', ['lambda_package:codeCollector', 'lambda_deploy:codeCollector']);