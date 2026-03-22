pipeline {
    agent any

    tools {
        maven 'Maven_3.9'
        jdk   'JDK_21'
    }

    options {
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }

    stages {

        stage('Checkout') {
            steps {
                echo '--- Cloning latest code from GitHub ---'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '--- Compiling project with Maven ---'
                bat 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo '--- Executing Selenium test suite ---'
                bat 'mvn test'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml',
                          allowEmptyResults: true
                }
            }
        }

        stage('Publish Report') {
            steps {
                echo '--- Publishing HTML test report ---'
                publishHTML([
                    allowMissing:          true,
                    alwaysLinkToLastBuild: true,
                    keepAll:               true,
                    reportDir:             'target/surefire-reports',
                    reportFiles:           'index.html',
                    reportName:            'Selenium Test Report'
                ])
            }
        }
    }

    post {
        success { echo 'BUILD PASSED — All Selenium tests green!' }
        failure { echo 'BUILD FAILED — Check console output.' }
        always  { cleanWs() }
    }s
}

