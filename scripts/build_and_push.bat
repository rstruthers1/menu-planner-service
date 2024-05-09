REM TODO: build and push using CI/CD pipeline

@echo off
cd ..
REM Login to Docker Hub
echo Logging into Docker Hub...
docker login

REM Build the Docker image
echo Building Docker image...
docker build -t homemenuplanner:latest .

REM Tag the image for upload to Docker Hub
echo Tagging image...
docker tag homemenuplanner:latest rmstruthers1/homemenuplanner:latest

REM Push the image to Docker Hub
echo Pushing image to Docker Hub...
docker push rmstruthers1/homemenuplanner:latest

echo Done.


