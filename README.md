# Note-Share UTP

Note-Share UTP is a high-performance, decoupled microservice architecture designed for reliable file handling and sharing. Built primarily with Java and Spring Boot, the system separates API logic, messaging, and security services to ensure a scalable and responsive user experience.

This repository serves as the **Main API Service**. The full product is a distributed system comprising three repositories orchestrated via Docker Compose:
1. **Note-Share-UTP** (This repo): Core application logic and API gateway.
2. **[file-scanner](https://github.com/Toostew/file-scanner)**: Security service for real-time virus scanning.
3. **[Noteshare-Thumbnail-service](https://github.com/Toostew/Noteshare-Thumbnail-service)**: Background worker for image processing.

## Key Features

* **Event-Driven Pipeline:** Utilizes Apache Kafka to manage message brokering, reducing API latency by 40% by offloading heavy processing to background workers.
* **Security Orchestration:** Integrated containerized ClamAV for real-time virus scanning on user-uploaded content before persistence.
* **Storage:** Leverages R2 Cloudflare Object Storage for scalable and reliable file persistence.
* **Observability:** Implements Prometheus for service health metrics and Grafana for dashboard visualization.
* **Reliability:** Automated email alerts for system crashes or high-load events.

## Tech Stack

* **Backend:** Java (Spring Boot)
* **Messaging:** Apache Kafka
* **Database:** MySQL
* **Storage:** Cloudflare R2
* **Monitoring:** Prometheus, Grafana
* **Infrastructure:** Docker, Nginx, Bash scripts

---

## Local Setup Guide

Follow these steps to get the full stack running on your local machine.

### 1. Prerequisites
Ensure you have the following installed:
* [Docker & Docker Desktop](https://www.docker.com/products/docker-desktop/)
* [Git](https://git-scm.com/)

### 2. Clone the Repositories
You need all three services in the same parent directory:
```bash
git clone [https://github.com/Toostew/Note-Share-UTP.git](https://github.com/Toostew/Note-Share-UTP.git)
git clone [https://github.com/Toostew/file-scanner.git](https://github.com/Toostew/file-scanner.git)
git clone [https://github.com/Toostew/Noteshare-Thumbnail-service.git](https://github.com/Toostew/Noteshare-Thumbnail-service.git)

```
### 3. Configure Environment Variables

Each service requires specific environment variables to communicate with Kafka, MySQL, and Cloudflare R2.

You have the option to hard-code the variables directly within the application-prod.properties or application-test.properties found in src/main/resources. the latter 
of which is used specifically for testing. Be sure to change the current active .properties file withint application.properties also found in the same directory!

Optionally (and most recommended for prod environments), you could consolidate these within a .env file and reference it within the docker file.

### 4. Run with Docker Compose

Navigate to the Note-Share-UTP root directory and trigger the build:
```bash
docker compose build up
```
This command will:

* Spin up a MySQL instance.

* Start Apache Kafka and Zookeeper.

* Initialize the ClamAV scanner.

* Build and run the Main API, Thumbnail, and Scanner services.

### 5. Access the Services

  Main API: http://localhost:8080

  Grafana Dashboard: http://localhost:3000

  Prometheus: http://localhost:9090
