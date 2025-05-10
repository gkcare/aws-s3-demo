# AWS S3 Example – Spring Boot Integration

This project demonstrates how to upload, download, and delete files in an AWS S3 bucket using a Spring Boot REST API.

## 📌 Use Case

In a distributed system, it's often necessary to store files (such as reports, invoices, or logs) in a centralized location accessible by all services. Amazon S3 is a reliable global storage solution ideal for this purpose.

## 🚀 Features

- Upload a file to S3
- Download a file from S3
- Delete a file from S3
- RESTful API endpoints
- Configuration via `application.yaml`

## 🔧 Tech Stack

- Java 17
- Spring Boot
- AWS SDK v2 (S3 Async Client)
- Maven

## 🧑‍💻 Prerequisites

1. **AWS Account** – Create one at [https://aws.amazon.com](https://aws.amazon.com)
2. **IAM User** – Create an IAM user with the following permissions:
   - `s3:PutObject`
   - `s3:GetObject`
   - `s3:DeleteObject`
3. **Create an S3 Bucket** – Make a bucket where files will be stored.
4. **Generate AWS Credentials** – Get the **Access Key ID** and **Secret Access Key**.

## ⚙️ Configuration

Add the following to your `application.yaml`:

```yaml
aws:
  s3:
    bucket-name: your-bucket-name
    region: your-aws-region
    access-key: your-access-key
    secret-key: your-secret-key
