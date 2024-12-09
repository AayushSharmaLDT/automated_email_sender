pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "aayushldt/springboot-app"
    }

    stages {
        stage('Clone Code') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }
        stage('Build') {
            steps {
                echo 'Building the application...'
                sh './mvnw clean package' // Maven command to build
            }
        }
        stage('Test') {
            steps {
                echo 'Running tests...'
                sh './mvnw test' // Maven command to run tests
            }
        }
        stage('Package with Docker') {
            steps {
                echo 'Building Docker image...'
                script {
                    docker.build(DOCKER_IMAGE)
                }
            }
        }
        stage('Push Docker Image') {
            steps {
                echo 'Pushing Docker image to Docker Hub...'
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker.image(DOCKER_IMAGE).push("latest")
                    }
                }
            }
        }
        stage('Deploy to Server') {
            steps {
                echo 'Deploying to server...'
                sh 'scp target/*.jar user@server:/path/to/deploy'
                //Server Path
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check logs for errors.'
        }
    }
}
