(ns nimaeskandary.devops.terraform.core
  (:import (com.hashicorp.cdktf App LocalBackend$Builder TerraformStack)
           (com.hashicorp.cdktf.providers.aws.provider AwsProvider$Builder)
           (com.hashicorp.cdktf.providers.aws.s3_bucket S3Bucket$Builder)
           (com.hashicorp.cdktf.providers.aws.vpc Vpc$Builder)))

(def app (App.))

(def stack (TerraformStack. app "application-stack"))

(def backend
  (-> (LocalBackend$Builder/create stack)
      (.path "application-stack.tfstate")
      (.workspaceDir "terraform")
      (.build)))

(def provider
  (-> (AwsProvider$Builder/create stack "AWS")
      (.region "us-east-1")
      (.build)))

(def bucket
  (-> (S3Bucket$Builder/create stack "bucket")
      (.build)))

(def vpc
  (-> (Vpc$Builder/create stack "private-vpc")
      (.build)))

(defn synth [_] (.synth app))
