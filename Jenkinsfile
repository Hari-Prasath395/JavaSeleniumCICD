pipeline {
    agent any

    tools {
        maven 'Maven_3.9'   // Must match Jenkins Global Tool Config name
        jdk   'JDK_21'      // Must match Jenkins Global Tool Config name
    }

    environment {
        DISPLAY = ':99'     // Headless Chrome on Linux agents
        HEADLESS = 'true'   // force headless mode in CI
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
                sh 'mvn clean compile -q'
            }
        }

        stage('Test') {
            steps {
                echo '--- Executing Selenium test suite ---'
                sh 'mvn test -Dsurefire.useFile=false'
            }
              post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('Publish Report') {
            steps {
                echo '--- Publishing HTML test report ---'
                publishHTML([
                    allowMissing:          false,
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
        success {
            echo 'BUILD PASSED — All Selenium tests green!'
        }
        failure {
            echo 'BUILD FAILED — Check console output for errors.'
        }
        always {
            cleanWs()
        }
    }
}