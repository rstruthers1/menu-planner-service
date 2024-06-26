# Home Menu Planner Backend - AWS EKS, MySQL, Spring Boot

**NOTE: The backend setup instructions have not been tested. Steps could be missing or incorrect as I wrote this up after finishing the setup**

This is the backend for the Home Menu Planner application, built with Spring Boot.

The application is deployed to AWS EKS (Elastic Kubernetes Service) and uses MySQL as the database.

## TODO
* Test the backend setup steps with small hello world application
* Update the setup steps in the README.md file of the front end repo
* Move the prod setup steps in this file to another file
* Add another readme file for local setup steps
* Have this README have overview of the backend app functionality
* Add documentation for the database model
* Add links to prod and local setup steps

## Overview
- [Prerequisites](#prerequisites)
- [Setting Up Kubernetes (EKS) Cluster](#setting-up-kubernetes-eks-cluster)
  - [Create EKS Cluster](#create-eks-cluster)
  - [Create or update a kubectl config file for the cluster](#create-or-update-a-kubectl-config-file-for-the-cluster)
  - [Add AWS Load Balancer Controller](#add-aws-load-balancer-controller)
    - [Create AWSLoadBalancerControllerIAMPolicy](#create-awsloadbalancercontrolleriampolicy)
    - [Create an IAM OIDC provider for your cluster](#create-an-iam-oidc-provider-for-your-cluster)
    - [Create IAM Service Account for Load Balancer Controller](#create-iam-service-account-for-load-balancer-controller)
    - [Add the eks-charts repository and update it](#add-the-eks-charts-repository-and-update-it)
    - [Use helm to install the AWS Load Balancer](#use-helm-to-install-the-aws-load-balancer)
- [Setting Up MySQL on AWS](#setting-up-mysql-on-aws)
  - [Create MySQL Database](#create-mysql-database)
  - [Adding Database and JWT Secrets to Kubernetes](#adding-database-and-jwt-secrets-to-kubernetes)
    - [Step 1: Encode and Prepare Secrets](#step-1-encode-and-prepare-secrets)
    - [Step 2: Apply the Secret to the Kubernetes Cluster](#step-2-apply-the-secret-to-the-kubernetes-cluster)
- [Setting Up Kubernetes Deployment and Service](#setting-up-kubernetes-deployment-and-service)
  - [Deployment and Service YAML](#deployment-and-service-yaml)
- [Generate and Add a Certificate to AWS](#generate-and-add-a-certificate-to-aws)
  - [Step 1: Generate a Self-Signed Certificate (for testing purposes)](#step-1-generate-a-self-signed-certificate-for-testing-purposes)
  - [Step 2: Upload the Certificate to AWS Certificate Manager (ACM)](#step-2-upload-the-certificate-to-aws-certificate-manager-acm)
  - [Step 3: Update Kubernetes Secrets with the Certificate](#step-3-update-kubernetes-secrets-with-the-certificate)
- [Ingress Configuration](#ingress-configuration)
- [Creating Docker Image and Pushing to Docker Hub](#creating-docker-image-and-pushing-to-docker-hub)
  - [Create a Dockerfile](#create-a-dockerfile)
- [GitHub Actions for CI/CD](#github-actions-for-cicd)
  - [Creating a repository in github](#creating-a-repository-in-github)
  - [You wil need to add the following secrets to your GitHub repository:](#you-wil-need-to-add-the-following-secrets-to-your-github-repository)
  - [Create a .github/workflows/build-and-deploy.yml file with the following content:](#create-a-githubworkflowsbuild-and-deployyml-file-with-the-following-content)
  - [NOTE: the application has flyway migrations enabled, so the database will be updated automatically when the application starts.](#note-the-application-has-flyway-migrations-enabled-so-the-database-will-be-updated-automatically-when-the-application-starts)
  
## Prerequisites

- AWS CLI: [Install AWS CLI](https://docs.aws.amazon.com/cli/latest/userguide/install-cliv2.html)
- kubectl: [Install kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- eksctl: [Install eksctl](https://docs.aws.amazon.com/eks/latest/userguide/eksctl.html)
- helm: [Install Helm](https://helm.sh/docs/intro/install/)
- Docker: [Install Docker](https://docs.docker.com/get-docker/)
- GitHub Actions: [GitHub Actions](https://docs.github.com/en/actions)
- Docker Hub repository: [Docker Hub](https://hub.docker.com/)

## Note about scripts
I'm using scripts mostly written in PowerShell. If you're using a different shell, you may need to modify the scripts.

## Setting Up Kubernetes (EKS) Cluster

### Create EKS Cluster

See: [Getting started with Amazon EKS](https://docs.aws.amazon.com/eks/latest/userguide/getting-started.html)

I recommend going to the above link and following the instructions there. The following notes are to help me remember what I did. :-)

**IMPORTANT NOTE: This will cost money. Either make sure to delete the cluster after you're done or set up a budget alert!!**

This will create an EKS cluster with 2 nodes of type t3.small. The cluster will be in the us-east-1 region.
```bash
eksctl create cluster --name menu-planner --region us-east-1 --nodes 2 --node-type t3.small --nodes-min 1 --nodes-max 2 --managed
```

### Create or update a kubectl config file for the cluster
```bash
aws eks --region us-east-1 update-kubeconfig --name menu-planner
```

## Add S3 bucket for storing images
Run the following command to create a new S3 bucket. Update the bucket name and region
```shell
aws s3api create-bucket --bucket homemenuplanner-images --region us-east-1
```

## Add AWS Load Balancer Controller
Note: I tried to do nginx-ingress first, but couldn't get it to work. I then switched to the AWS Load Balancer Controller
and got it to work with the following steps.


### Create AWSLoadBalancerControllerIAMPolicy

Get iam-policy.json from [here](https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/main/docs/install/iam_policy.json)

```bash
aws iam create-policy --policy-name AWSLoadBalancerControllerIAMPolicy --policy-document file://iam-policy.json
```
  
### [Create an IAM OIDC provider for your cluster](https://docs.aws.amazon.com/eks/latest/userguide/enable-iam-roles-for-service-accounts.html)
Follow the instruction linked in the section header. I did this using the AWS Console. You can also do it using eksctl.

### Create IAM Service Account for Load Balancer Controller
**Replace the policy ARN with the one you created in the previous step.**
```bash
eksctl create iamserviceaccount --cluster=menu-planner --namespace=kube-system --name=aws-load-balancer-controller --role-name AmazonEKSLoadBalancerControllerRole --attach-policy-arn=arn:aws:iam::111122223333:policy/AWSLoadBalancerControllerIAMPolicy --approve
```

### Add the eks-charts repository and update it:
```bash
helm repo add eks https://aws.github.io/eks-charts
helm repo update
```

### Use helm to install the AWS Load Balancer
```bash
helm install aws-load-balancer-controller eks/aws-load-balancer-controller -n kube-system --set clusterName=menu-planner --set serviceAccount.create=false --set serviceAccount.name=aws-load-balancer-controller
```

## Setting Up MySQL on AWS
### Create MySQL Database
Use Cloud Formation, AWS RDS console or CLI to create a MySQL database instance.

AWS CLI example
Cloud Formation template example: [menu-planner-db-cloudformation.yaml](scripts/aws/menu-planner-db-cloudformation.yaml)
```bash
aws cloudformation create-stack --stack-name MenuPlannerDatabaseStack --template-body file://menu-planner-db-cloudformation.yaml --region us-east-1 --parameters ParameterKey=DBInstanceClass,ParameterValue=db.t3.micro ParameterKey=DBAllocatedStorage,ParameterValue=20
```

**Make sure to change the master password after the database is created**

## Adding Database and JWT Secrets to Kubernetes

### Step 1: Encode and Prepare Secrets

Create a Kubernetes Secret Manifest Template file called `k8s-secrets-template.yml` with the following content:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: menuplanner-secrets
type: Opaque
data:
  username: <base64-encoded-username>
  password: <base64-encoded-password>
  jdbc-url: <base64-encoded-jdbc-url>
  jwt-secret: <base64-encoded-jwt-secret>
  jwt-cookie-same-site: <base64-encoded-jwt-cookie-same-site>
  jwt-cookie-secure: <base64-encoded-jwt-cookie-secure>
  aws-access-key-id: <base64-encoded-aws-access-key-id>
  aws-secret-key: <base64-encoded-aws-secret-key>

```

If you are using Windows OS, use the following PowerShell script to encode your secrets in Base64 format and create a Kubernetes secret manifest file.

**If you are on a different OS than Windows, you will need to modify the script accordingly.**

```powershell
# Functions to encode and decode Base64
Function ConvertFrom-Base64($base64) {
    return [System.Text.Encoding]::ASCII.GetString([System.Convert]::FromBase64String($base64))
}

Function ConvertTo-Base64($plain) {
    return [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes($plain))
}

# Prompt user for secrets
$username = Read-Host "Enter your database username"
$password = Read-Host "Enter your database password"
$jdbcUrl = Read-Host "Enter your JDBC URL"
$jwtSecret = Read-Host "Enter your JWT secret"
$jwtCookieSameSite = Read-Host "Enter your JWT cookie same site"
$jwtCookieSecure = Read-Host "Enter your JWT Cookie Secure"
$awsAccessKey = Read-Host "Enter your AWS Access Key"
$awsSecret = Read-Host "Enter your AWS Secret"

# Encode secrets in Base64
$encodedUsername = ConvertTo-Base64 $username
$encodedPassword = ConvertTo-Base64 $password
$encodedJdbcUrl = ConvertTo-Base64 $jdbcUrl
$encodedJwtSecret = ConvertTo-Base64 $jwtSecret
$encodedJwtCookieSameSite = ConvertTo-Base64 $jwtCookieSameSite
$encodedJwtCookieSecure = ConvertTo-Base64 $jwtCookieSecure
$encodedAwsAccessKey = ConvertTo-Base64 $awsAccessKey
$encodedAwsSecret = ConvertTo-Base64 $awsSecret

# Read the template file
$templateFilePath = "k8s-secrets-template.yml"
$templateContent = Get-Content $templateFilePath

# Replace placeholders with Base64 encoded secrets
$templateContent = $templateContent -replace "<base64-encoded-username>", $encodedUsername
$templateContent = $templateContent -replace "<base64-encoded-password>", $encodedPassword
$templateContent = $templateContent -replace "<base64-encoded-jdbc-url>", $encodedJdbcUrl
$templateContent = $templateContent -replace "<base64-encoded-jwt-secret>", $encodedJwtSecret
$templateContent = $templateContent -replace "<base64-encoded-jwt-cookie-same-site>", $encodedJwtCookieSameSite
$templateContent = $templateContent -replace "<base64-encoded-jwt-cookie-secure>", $encodedJwtCookieSecure
$templateContent = $templateContent -replace "<base64-encoded-aws-access-key>", $encodedAwsAccessKey
$templateContent = $templateContent -replace "<base64-encoded-aws-secret>", $encodedAwsSecret

# Write the updated content to new file
$outputFile = "k8s-secrets.yml"
$templateContent | Set-Content $outputFile


Write-Output "Secrets have been encoded and new file has been created based on the template"

```
Run the above script in PowerShell and enter the required secrets when prompted. The script will create a new file named `k8s-secrets.yml` with the encoded secrets.

### Step 2: Apply the Secret to the Kubernetes Cluster
```bash
kubectl apply -f k8s-secrets.yml
```

**Make sure to add the k8s-secrets.yml file to your .gitignore file so that it doesn't get checked into source control.**


## Setting Up Kubernetes Deployment and Service

### Deployment and Service YAML
Create k8s/homemenuplanner-deployment.yml with the following content:

**NOTE: Make sure to replace the placeholders with your actual values.**

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: homemenuplanner-deployment
  annotations:
    service.beta.kubernetes.io/aws-load-balancer-ssl-cert: Use the ARN of the certificate here
    service.beta.kubernetes.io/aws-load-balancer-backend-protocol: https
    service.beta.kubernetes.io/aws-load-balancer-ssl-ports: "443"
    # Note that the backend talks over HTTP.
    service.beta.kubernetes.io/aws-load-balancer-type: external
spec:
  replicas: 2
  selector:
    matchLabels:
      app: homemenuplanner
  template:
    metadata:
      labels:
        app: homemenuplanner
    spec:
      containers:
        - name: homemenuplanner
          image: your_docker_hub_user/homemenuplanner:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: prod
            - name: CORS_ALLOWED_ORIGIN
              value: "Will be filled in with your react app domain"
            - name: JWT_COOKIE_SECURE
              value: "true"
            - name: JWT_COOKIE_SAME_SITE
              value: "None"
            - name: AWS_REGION
              value: "use your aws region here"
            - name: AWS_BUCKET_NAME
              value: "homemenuplanner"
            - name: JDBC_URL
              valueFrom:
                secretKeyRef:
                  name: menuplanner-secrets
                  key: jdbc-url
            - name: JDBC_USERNAME
              valueFrom:
                secretKeyRef:
                  name: menuplanner-secrets
                  key: username
            - name: JDBC_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: menuplanner-secrets
                  key: password
            - name: JWT_SECRET
              valueFrom:
                secretKeyRef:
                  name: menuplanner-secrets
                  key: jwt-secret
            - name: AWS_ACCESS_KEY_ID
              valueFrom:
                secretKeyRef:
                  name: menuplanner-secrets
                  key: aws-access-key-id
            - name: AWS_SECRET_ACCESS_KEY
              valueFrom:
                secretKeyRef:
                  name: menuplanner-secrets
                  key: aws-secret-key
          volumeMounts:
            - name: tls-secret
              mountPath: "/etc/tls"
              readOnly: true
      volumes:
        - name: tls-secret
          secret:
            secretName: homemenuplanner-tls-secret
      imagePullSecrets:
        - name: dockerhub-secret
---
apiVersion: v1
kind: Service
metadata:
  name: homemenuplanner-service
spec:
  selector:
    app: homemenuplanner
  ports:
    - name: http
      protocol: TCP
      port: 80
      targetPort: 8080
  type: LoadBalancer
```

## Generate and Add a Certificate to AWS

### Step 1: Generate a Self-Signed Certificate (for testing purposes)
**NOTE: This is for testing purposes only! For production, you should get a certificate from a trusted Certificate Authority (CA).**

You can generate a self-signed certificate using OpenSSL. Here's an example configuration file (`openssl.cnf`) and commands to generate the certificate and key.

#### `openssl.cnf`:
```ini
[ req ]
default_bits        = 2048
distinguished_name  = req_distinguished_name
req_extensions      = req_ext
x509_extensions     = v3_req
prompt              = no

[ req_distinguished_name ]
C                   = US
ST                  = State
L                   = City
O                   = Organization
OU                  = Organizational Unit
# Had to comment out the CN because the load balancer domain name was too long (over 64 characters)
#CN                  = Common Name

[ req_ext ]
subjectAltName      = @alt_names

[ v3_req ]
# I had to comment out keyUsage or I got an error in Chrome when trying to connect to the backend.
#keyUsage            = keyEncipherment, dataEncipherment
extendedKeyUsage    = serverAuth
subjectAltName      = @alt_names

[ alt_names ]
DNS.1               = Use the load balancer domain name here
```

Get the load balancer domain from aws console using these steps:
1. Go to the EC2 console.
2. Click on Load Balancers.
3. Click on the load balancer created by the service.
4. Copy the DNS name.

Commands to generate the certificate and key:

You may need to install openssl on your system. I used Windows Subsystem for Linux (WSL) to generate the certificate:
[How to install Linux on Windows with WSL](https://learn.microsoft.com/en-us/windows/wsl/install)
```bash     
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -config openssl.cnf
```

### Step 2: Upload the Certificate to AWS Certificate Manager (ACM)
1. Go to the AWS Certificate Manager (ACM) in the AWS Management Console.
2. Click on "Import a certificate".
3. Upload the certificate (tls.crt), private key (tls.key), and the certificate chain (if applicable).

### Step 3: Update Kubernetes Secrets with the Certificate
Create a Kubernetes secret with the certificate and key:
```bash
kubectl create secret tls homemenuplanner-tls-secret --cert=tls.crt --key=tls.key
```

Verify that the secret was created:
```bash
kubectl get secrets homemenuplanner-tls-secret
```

## Ingress Configuration
Create an Ingress resource to manage external access to the services.

Get the service domain name by running the following command:
```bash 
kubectl get svc homemenuplanner-service
```

Get the load balancer domain from aws console using these steps:
1. Go to the EC2 console.
2. Click on Load Balancers.
3. Click on the load balancer created by the service.
4. Copy the DNS name.


```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: homemenuplanner-ingress
  annotations:
    kubernetes.io/ingress.class: alb
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/ssl-redirect: '443'
    alb.ingress.kubernetes.io/certificate-arn: <your-certificate-arn>
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP": 80}, {"HTTPS":443}]'
spec:
  tls:
    - hosts:
        - <load balancer domain name>
        - <service domain name>
      secretName: homemenuplanner-tls-secret
  rules:
    - host: <load balancer domain name>
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: homemenuplanner-service
                port:
                  number: 80
    - host: <service domain name>
      http:
        paths:
          - path: /
            pathType: Prefix
            backend:
              service:
                name: homemenuplanner-service
                port:
                  number: 80
```
Apply the above configuration:
```bash
kubectl apply -f k8s/homemenuplanner-ingress.yml
```

## Creating Docker Image and Pushing to Docker Hub
### Create an account on Docker Hub and a repository
1. Create an account on Docker Hub: [Docker Hub](https://hub.docker.com/)
2. Create a repository for your Docker images.

### Create a Dockerfile
Make sure your Dockerfile is properly set up to build the Spring Boot application.
See: [Dockerfile](Dockerfile)

## GitHub Actions for CI/CD

### Create a repository in github
Create a repository in GitHub and push your code to it. Use a branch instead of main if you want to test the CI/CD pipeline first.

### You wil need to add the following secrets to your GitHub repository:
- DOCKER_HUB_USERNAME
- DOCKER_HUB_PASSWORD
- AWS_ACCESS_KEY_ID
- AWS_SECRET_ACCESS_KEY

The AWS credentials should have permissions to deploy to the EKS cluster.

### Create a .github/workflows/build-and-deploy.yml file with the following content:

```yaml
name: Build and Deploy

on:
  push:
    branches:
      - main

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout code
        uses: actions/checkout@v2

      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Build with Gradle
        run: ./gradlew build

      - name: Log in to Docker Hub
        run: echo "${{ secrets.DOCKER_HUB_PASSWORD }}" | docker login -u "${{ secrets.DOCKER_HUB_USERNAME }}" --password-stdin

      - name: Build and push Docker image
        run: |
          docker build -t <your dockerhub username>/homemenuplanner:latest .
          docker push <your dockerhub username>/homemenuplanner:latest

  deploy:
    needs: build
    runs-on: ubuntu-latest

    steps:
      - name: Configure AWS credentials
        uses: aws-actions/configure-aws-credentials@v1
        with:
          aws-access-key-id: ${{ secrets.AWS_ACCESS_KEY_ID }}
          aws-secret-access-key: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
          aws-region: us-east-1

      - name: Deploy to EKS
        run: |
          kubectl apply -f k8s/homemenuplanner-deployment.yml
```

The above action will build and deploy after pushes to main. You can modify it to suit your needs.

### NOTE: the application has flyway migrations enabled, so the database will be updated automatically when the application starts.




