# Home Menu Planner Backend

**NOTE: This is incomplete and hasn't been tested!!**

This is the backend for the Home Menu Planner application, built with Spring Boot.

## Prerequisites

- AWS CLI
- kubectl
- eksctl
- helm
- Docker
- GitHub Actions
- Docker Hub repository

## Note about scripts
I'm using scripts mostly written in PowerShell. If you're using a different shell, you may need to modify the scripts.

## Setting Up Kubernetes (EKS) Cluster

### Create EKS Cluster

See: [Getting started with Amazon EKS](https://docs.aws.amazon.com/eks/latest/userguide/getting-started.html)

I recommend going to the above link and following the instructions there. The following notes are to help me remember what I did. :-)

**IMPORTANT NOTE: This will cost money. Either make sure to delete the cluster after you're done or set up a budget alert!!**

This will create an EKS cluster with 2 nodes of type t3.small. The cluster will be in the us-east-1 region. The cluster will be managed by AWS.
```bash
eksctl create cluster --name menu-planner --region us-east-1 --nodes 2 --node-type t3.small --nodes-min 1 --nodes-max 2 --managed
```

### Create or update a kubectl config file for the cluster
```bash
aws eks --region us-east-1 update-kubeconfig --name menu-planner
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
I did this using the AWS Console. You can also do it using eksctl.

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
Cloud Formation teamplate example: [menu-planner-db-cloudformation.yaml](scripts/aws/menu-planner-db-cloudformation.yaml)
```bash
aws cloudformation create-stack --stack-name MenuPlannerDatabaseStack --template-body file://menu-planner-db-cloudformation.yaml --region us-east-1 --parameters ParameterKey=DBInstanceClass,ParameterValue=db.t3.micro ParameterKey=DBAllocatedStorage,ParameterValue=20
```


### Create Kubernetes Secret for Database Credentials
```bash
kubectl create secret generic menuplanner-secrets --from-literal=username=your_db_username --from-literal=password=your_db_password --from-literal=jdbc-url=jdbc:mysql://your_db_endpoint:3306/your_db_name --from-literal=jwt-secret=your_jwt_secret --from-literal=jwt-cookie-same-site=None --from-literal=jwt-cookie-secure=true
```

## Creating Docker Image and Pushing to Docker Hub
Make sure your Dockerfile is properly set up to build the Spring Boot application.
See: [Dockerfile](Dockerfile)

## GitHub Actions for CI/CD
Create a .github/workflows/build-and-deploy.yml file with the following content:

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
          docker build -t rmstruthers1/homemenuplanner:latest .
          docker push rmstruthers1/homemenuplanner:latest

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

## Setting Up Kubernetes Deployment and Service
### Deployment and Service YAML
Create k8s/homemenuplanner-deployment.yml with the following content:

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: homemenuplanner-deployment
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
          image: rmstruthers1/homemenuplanner:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              value: prod
            - name: CORS_ALLOWED_ORIGIN
              value: "Add react frontend url here"
            - name: JWT_COOKIE_SECURE
              value: "true"
            - name: JWT_COOKIE_SAME_SITE
              value: "None"
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
    - name: https
      protocol: TCP
      port: 443
      targetPort: 8080
  type: LoadBalancer
```

## Generate and Add a Certificate to AWS

### Step 1: Generate a Self-Signed Certificate (for testing purposes)

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
CN                  = your-domain.com

[ req_ext ]
subjectAltName      = @alt_names

[ v3_req ]
keyUsage            = keyEncipherment, dataEncipherment
extendedKeyUsage    = serverAuth
subjectAltName      = @alt_names

[ alt_names ]
DNS.1               = your-domain.com
DNS.2               = www.your-domain.com
```

Commands to generate the certificate and key:

**NOTE: This is for testing purposes only! For production, you should get a certificate from a trusted Certificate Authority (CA).**

You may need to install OpenSSL on your system. I used Windows Subsystem for Linux (WSL) to generate the certificate:
(How to install Linux on Windows with WSL)[https://learn.microsoft.com/en-us/windows/wsl/install]
```bash     
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -config openssl.cnf
```

### Upload the Certificate to AWS Certificate Manager (ACM)
1. Go to the AWS Certificate Manager (ACM) in the AWS Management Console.
2. Click on "Import a certificate".
3. Upload the certificate (tls.crt), private key (tls.key), and the certificate chain (if applicable).

### Update Kubernetes Secrets with the Certificate
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

TODO: how to generate and add certificate to AWS

```yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: homemenuplanner-ingress
  annotations:
    alb.ingress.kubernetes.io/scheme: internet-facing
    alb.ingress.kubernetes.io/ssl-redirect: '443'
    alb.ingress.kubernetes.io/listen-ports: '[{"HTTP": 80}, {"HTTPS": 443}]'
    alb.ingress.kubernetes.io/certificate-arn: <your certificate arn>
    kubernetes.io/ingress.class: alb
spec:
  rules:
    - host: your-domain.com
      http:
        paths:
          - path: /*
            pathType: ImplementationSpecific
            backend:
              service:
                name: homemenuplanner-service
                port:
                  name: http
  tls:
    - hosts:
        - your-domain.com
      secretName: homemenuplanner-tls-secret
```

## Conclusion
This setup allows you to automatically build and deploy your Spring Boot backend to an AWS EKS cluster whenever changes are pushed to the main branch on GitHub.




