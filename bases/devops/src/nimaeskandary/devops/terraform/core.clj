(ns nimaeskandary.devops.terraform.core
  (:import (com.hashicorp.cdktf App LocalBackend$Builder TerraformStack)
           (com.hashicorp.cdktf.providers.aws.provider
            AwsProvider$Builder
            AwsProviderEndpoints$Builder)
           (com.hashicorp.cdktf.providers.aws.s3_bucket S3Bucket$Builder)
           (com.hashicorp.cdktf.providers.aws.vpc Vpc$Builder)
           (java.util ArrayList)))

(defn create-app [] (App.))

(defn create-stack [scope] (TerraformStack. scope "application-stack"))

(defn create-backend
  [scope]
  (-> (LocalBackend$Builder/create scope)
      (.path "application-stack.tfstate")
      (.workspaceDir "terraform")
      (.build)))

(defn create-provider
  [scope region]
  ;; https://docs.localstack.cloud/user-guide/integrations/terraform/
  (let [localstack-endpoint "http://localhost:4566"]
    (-> (AwsProvider$Builder/create scope "AWS")
        (.region region)
        (.profile "dev")
        (.s3UsePathStyle true)
        (.skipCredentialsValidation true)
        (.skipMetadataApiCheck "true")
        (.skipRequestingAccountId true)
        (.endpoints
         (ArrayList.
          [(-> (AwsProviderEndpoints$Builder.)
               (.apigateway localstack-endpoint)
               (.apigatewayv2 localstack-endpoint)
               (.cloudformation localstack-endpoint)
               (.cloudwatch localstack-endpoint)
               (.dynamodb localstack-endpoint)
               (.ec2 localstack-endpoint)
               (.es localstack-endpoint)
               (.elasticache localstack-endpoint)
               (.firehose localstack-endpoint)
               (.iam localstack-endpoint)
               (.kinesis localstack-endpoint)
               (.lambda localstack-endpoint)
               (.rds localstack-endpoint)
               (.redshift localstack-endpoint)
               (.route53 localstack-endpoint)
               (.s3 localstack-endpoint)
               (.secretsmanager localstack-endpoint)
               (.ses localstack-endpoint)
               (.sns localstack-endpoint)
               (.sqs localstack-endpoint)
               (.ssm localstack-endpoint)
               (.stepfunctions localstack-endpoint)
               (.sts localstack-endpoint)
               (.build))]))
        (.build))))

(defn create-bucket
  [scope]
  (-> (S3Bucket$Builder/create scope "bucket")
      (.build)))

(defn create-vpc
  [scope]
  (-> (Vpc$Builder/create scope "private-vpc")
      (.build)))

(defn synth
  [_]
  (let [app (create-app)
        application-stack (create-stack app)]
    (create-backend application-stack)
    (create-provider application-stack "us-east-1")
    (create-bucket application-stack)
    (create-vpc application-stack)
    (.synth app)))
