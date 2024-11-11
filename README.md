![Java](https://badgen.net/badge/Java/17/orange)
![PostgreSQL](https://badgen.net/badge/PostgreSQL/latest/cyan)
![Terraform](https://badgen.net/badge/Terraform/latest/purple)
![Docker](https://badgen.net/badge/Docker/27.3.1/blue)
![RabbitMQ](https://badgen.net/badge/RabbitMQ/latest/red)
![K3D](https://badgen.net/badge/k3d/latest/green)

# Demo Project

## 🦊 **Purpose**
This demo project showcases the integration of various modern development tools and technologies, including Java, PostgreSQL, RabbitMQ, Kubernetes (k3d), and Terraform. It provides different deployment options from local development to containerized environments.

## 💻 **Prerequisites**

### Required Tools
1. **Java 17**
```bash
brew install openjdk@17
```

2. **Docker**
```bash
brew install docker
```

### Optional Tools
3. **Tmux** - Terminal multiplexer for managing multiple terminal sessions
```bash
brew install tmux
```

4. **K3D** - Lightweight wrapper to run k3s in Docker
```bash
brew install k3d
```

5. **Terraform** - Infrastructure as Code tool
```bash
brew install terraform
```

6. **Istioctl** - Service mesh control tool
```bash
brew install istioctl
```

## 🚀 **Getting Started**

### Local Development
1. Start required services (database, message queue):
```bash
./docker-starter.sh
```

2. Launch the webserver application through your IDE

### Environment Setup
- Create a `.env` file based on the template for SaaS connections (Supabase and RabbitMQ)
- Configure your environment variables according to your service credentials

## 🛠 **Available Scripts**

| Script | Description |
|--------|-------------|
| `terraform-starter.sh` | Deploys services using Infrastructure as Code (Terraform) |
| `docker-starter.sh` | Launches services using Docker containers |
| `k3d-starter.sh` | Deploys services on local Kubernetes cluster (includes webserver and optional Istio integration) |
| `docker-ci-tester.sh` | Runs tests in CI environment |
| `local-starter.sh` | Configures local development environment |

## 📦 **Architecture**
![Architecture](architecture.png)

## 🔧 **Deployment Options**
1. **Local Development**: Using `local-starter.sh` with direct IDE integration
2. **Containerized**: Using Docker with `docker-starter.sh`
3. **Kubernetes**: Using k3d with `k3d-starter.sh`
4. **Infrastructure as Code**: Using Terraform with `terraform-starter.sh`
