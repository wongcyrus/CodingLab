SET SourceBucket=howwhofeelinvideopackage
#node C:\Users\developer\AppData\Roaming\npm\node_modules\grunt-cli\bin\grunt --gruntfile ..\Gruntfile.js lambda_package
#aws s3 cp ../dist/CodeCollector_latest.zip s3://%SourceBucket%/CodeCollector_latest.zip

del CodeCollector-packaged-template.yaml

aws cloudformation package ^
    --region us-east-1^
    --template-file CodeCollector.yaml ^
    --s3-bucket %SourceBucket% ^
    --output-template-file CodeCollector-packaged-template.yaml

aws cloudformation deploy ^
    --region us-east-1^
    --capabilities CAPABILITY_IAM ^
    --template-file CodeCollector-packaged-template.yaml --stack-name CodeCollector ^
    --parameter-overrides ^
    DynamodbAutoscaling=false ^
    SourceBucket=%SourceBucket%
