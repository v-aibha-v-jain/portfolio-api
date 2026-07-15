# Portfolio API

This is a serverless portfolio API built with Spring Boot and AWS. The app reads a portfolio JSON document from Amazon S3, keeps it cached in memory, and serves sections of the portfolio through Lambda and API Gateway.

## What It Does

- Loads portfolio data from an S3 JSON file at startup.
- Exposes portfolio sections such as about, experience, skills, projects, certifications, achievements, socials, and help.
- Runs as an AWS Lambda function behind API Gateway.

## Project Structure

- `PortfolioApplication.java` starts the Spring Boot app.
- `PortfolioService.java` loads and caches the JSON file from S3.
- `PortfolioController.java` exposes the API routes.
- `StreamLambdaHandler.java` connects Spring Boot to AWS Lambda proxy events.

## API Routes

All routes are under `/api`.

- `GET /api/` or `GET /api` - landing section
- `GET /api/exp` - experience
- `GET /api/about` - about / basics
- `GET /api/skills` - skills
- `GET /api/education` - education
- `GET /api/projects` - projects
- `GET /api/certifications` - certifications
- `GET /api/achievements` - achievements
- `GET /api/socials` - social links
- `GET /api/help` - help

## Setup

### 1. Update the S3 settings in the service

Open [src/main/java/com/example/portfolio/PortfolioService.java](src/main/java/com/example/portfolio/PortfolioService.java) and update these values to match your AWS setup:

- Region: `Region.AP_SOUTH_1`
- Bucket name: `vaibhav-jain-portfolio`
- JSON file name: `portfolio.json`

If you use a different AWS region, bucket, or file name, change them in that file before building.

### 2. Create the S3 bucket and upload the JSON file

Create an S3 bucket in your chosen region, then upload your portfolio JSON file with the same name used in the service code.

The JSON file should contain the sections expected by the API, for example `help`, `experience`, `basics`, `skills`, `education`, `projects`, `certifications`, `achievements`, and `social_links`.

### 3. Build the Lambda artifact

Use Maven to package the application without tests:

```bash
./mvnw clean package -DskipTests
```

After the build finishes, the deployable artifact is in the `target` folder. In this project, the shaded JAR is:

- `target/portfolio-0.0.1-SNAPSHOT.jar`

### 4. Create the Lambda function

Create a new AWS Lambda function with these settings:

- Runtime: Java 17
- Architecture: arm64
- Handler: change the handler text to `com.example.portfolio.StreamLambdaHandler::handleRequest` to set your execution entrypoint
- Source code: upload the JAR from the `target` folder

If you are using the console, choose upload from `.zip` or `.jar` source code and point it to `target/portfolio-0.0.1-SNAPSHOT.jar`.

### 5. Give the Lambda access to S3

Attach a policy that allows the Lambda execution role to read the S3 bucket.

At minimum, add `AmazonS3ReadOnlyAccess` to the Lambda IAM role (Add policy).

If you want tighter permissions, use a custom policy that allows only `s3:GetObject` on the specific bucket and JSON file.

### 6. Create the API Gateway route

Create an API Gateway and connect it to the Lambda function.

Use these settings:

- Route: `ANY /{proxy+}`
- Integration target: the Lambda function
- Payload format version: `1.0`

This lets API Gateway forward every route under `/api` to the Spring Boot Lambda handler.

### 7. Create auto trigger for lambda function upon s3 data update

Create an event on S3 for updating lambda function variables

- Go to Properties and scroll down to event notification
- Create a new event, give it a name and add a prefix
- Event types: Check the box for `All object create events`
- Under destinarion section select the lambda function and save changes

## Local Build Notes

- Java version: 17
- Spring Boot: 3.3.0
- AWS Lambda adapter: `aws-serverless-java-container-springboot3`

## Example Flow

1. Put the portfolio JSON file in S3.
2. Update the bucket name, region, and JSON file name in `PortfolioService.java`.
3. Build the JAR with `./mvnw clean package -DskipTests`.
4. Upload `target/portfolio-0.0.1-SNAPSHOT.jar` to Lambda.
5. Attach `AmazonS3ReadOnlyAccess` to the Lambda role.
6. Point API Gateway `ANY /{proxy+}` to the Lambda with payload version `1.0`.

## Result

Once deployed, this becomes a simple portfolio API that is easy to update by replacing the JSON file in S3 instead of redeploying the whole application.
