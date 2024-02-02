terraform {
  required_providers {
    docker = {
      source  = "kreuzwerker/docker"
      version = "~> 3.0.1"
    }
  }
}

provider "docker" {}

locals {
  application_config_path = abspath("${path.cwd}/infra/docker/application.yml")
  network_name            = "dev"
}

resource "docker_network" "private_network" {
  name   = local.network_name
  driver = "bridge"
}

resource "docker_container" "spring_rabbitmq" {
  image = "rabbitmq:management"
  name  = "spring-rabbitmq"
  ports {
    internal = 5672
    external = 5672
  }
  env = ["RABBITMQ_DEFAULT_USER=user", "RABBITMQ_DEFAULT_PASS=my-rabbitmq"]
  networks_advanced {
    name = local.network_name
  }

  healthcheck {
    test     = ["CMD", "rabbitmq-diagnostics", "-q", "ping"]
    interval = "30s"
    timeout  = "30s"
    retries  = 3
  }
}

resource "docker_container" "spring_mailhog" {
  image = "mailhog/mailhog:latest"
  name  = "spring-mailhog"
  ports {
    internal = 1025
    external = 1025
  }
  env = ["MH_HOSTNAME=spring-mailhog"]
  networks_advanced {
    name = local.network_name
  }

  healthcheck {
    test     = ["CMD", "nc", "-zv", "127.0.0.1", "8025"]
    interval = "5s"
    timeout  = "5s"
    retries  = 3
  }
}

resource "docker_container" "spring_mysql_master" {
  image = "bitnami/mysql:8.0"
  name  = "spring-mysql-master"
  env = [
    "MYSQL_DATABASE=webserver",
    "MYSQL_PASSWORD=my-sql-password",
    "MYSQL_USER=user",
    "MYSQL_ROOT_PASSWORD=my-sql-password"
  ]

  ports {
    internal = 3306
    external = 3306
  }

  networks_advanced {
    name = local.network_name
  }

  restart = "always"

  healthcheck {
    test     = ["CMD", "mysqladmin", "ping", "-h", "localhost", "-u$$MYSQL_USER", "-p$$MYSQL_PASSWORD"]
    interval = "5s"
    timeout  = "30s"
    retries  = 5
  }
}

resource "docker_container" "spring_redis_master" {
  image = "redis"
  name  = "spring-redis-master"

  ports {
    internal = 6379
    external = 6379
  }
  networks_advanced {
    name = local.network_name
  }

  healthcheck {
    test     = ["CMD", "redis-cli", "ping"]
    interval = "5s"
    timeout  = "30s"
    retries  = 5
  }
}

resource "docker_container" "spring_redis_slave" {
  image = "redis"
  name  = "spring-redis-slave"

  ports {
    internal = 6380
    external = 6380
  }
  command = ["--port", "6380", "--replicaof", "spring-redis-master", "6379"]
  networks_advanced {
    name = local.network_name
  }

  healthcheck {
    test     = ["CMD", "redis-cli", "-p", "6380", "ping"]
    interval = "5s"
    timeout  = "30s"
    retries  = 5
  }
}

resource "docker_container" "spring_webserver" {

  image = "carrey1994/spring-webserver:latest"
  name  = "spring-webserver"
  volumes {
    host_path      = local.application_config_path
    container_path = "/webserver/application.yml"
    read_only      = true
  }

  ports {
    internal = 8080
    external = 8080
  }
  networks_advanced {
    name = local.network_name
  }

  //the provider does not support this block, so make the restart 'always' to let server reconnect the database until it is ready.
  restart = "always"

  depends_on = [
    docker_container.spring_mysql_master,
    docker_container.spring_redis_master,
    docker_container.spring_redis_slave,
    docker_container.spring_rabbitmq,
    docker_container.spring_mailhog,
  ]
}
