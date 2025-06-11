pipeline {
    agent any
    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
        }
        stage ('Test') {
            steps {
                sh 'mvn clean test allure:report'
            }
        }
    }
    post {
        always {
            allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
            ])
        }
    }
}