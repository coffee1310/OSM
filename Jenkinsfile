pipeline {
    agent any
    stages {
        stage ('Build') {
            steps{
                sh 'mvn clean package'
            }
        }
        stage ('Test') {
            stages {
                sh 'mvn test'
            }
        }
    }
}