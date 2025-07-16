pipeline {
    agent any

    parameters {
        choice(
            name: 'ENV',
            choices: ['Dev', 'ProdANZ', 'ProdAPAC', 'ProdGCP', 'ProdUK', 'ProdUS'],
            description: 'Select the environment to run tests against'
        )
    }

    environment {
        SELENIUM_GRID_URL = 'http://test:test-password@192.168.50.173:4444/wd/hub'
    }

    tools {
        maven 'M3'
    }

    stages {
        stage('Build') {
            steps {
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                echo "Running tests on ENV: ProdAPAC"
                echo "Using Selenium Grid URL: ${SELENIUM_GRID_URL}"
                script {
                    def testStatus = sh(script: "mvn test -Denv=ProdAPAC -DhubUrl=$SELENIUM_GRID_URL", returnStatus: true)
                    if (testStatus != 0) {
                        echo "Test failures detected, proceeding to rerun failed tests."
                    }
                }
                // Create ZIP of Extent report for artifact download
                sh 'zip -j test-output/ExtentReport.zip test-output/ExtentReport.html || true'
            }
        }
        stage('Login for Rerun') {
            when {
                expression { fileExists('target/surefire-reports/testng-failed.xml') }
            }
            steps {
                echo "Running LoginTest to refresh cookies before rerun..."
                sh "mvn test -Dtest=UnbxdTests.testNG.login.LoginTest -Denv=ProdAPAC -DhubUrl=$SELENIUM_GRID_URL"
            }
        }
        stage('Rerun Failed Tests') {
            when {
                expression { fileExists('target/surefire-reports/testng-failed.xml') }
            }
            steps {
                echo "Rerunning only failed tests from previous run..."
                sh 'mvn test -Dsurefire.suiteXmlFiles=target/surefire-reports/testng-failed.xml -Denv=ProdAPAC'
            }
        }
    }

    post {
        success {
            slackSend(
                channel: '#qa-automation-reports',
                color: 'good',
                message: "✅ *SUCCESS* | `${env.JOB_NAME}` #${env.BUILD_NUMBER} ran on `${params.ENV}`\n<${env.BUILD_URL}|View Build>\n<${env.BUILD_URL}artifact/target/extent-report/ExtentReport.html|View Extent Report>",
                tokenCredentialId: 'slackID',
                teamDomain: 'unbxd',
                botUser: true
            )
        }
        failure {
            slackSend(
                channel: '#qa-automation-reports',
                color: 'danger',
                message: "❌ *FAILED* | `${env.JOB_NAME}` #${env.BUILD_NUMBER} on `${params.ENV}`\n<${env.BUILD_URL}|View Build>\n<${env.BUILD_URL}artifact/target/extent-report/ExtentReport.html|View Extent Report>",
                tokenCredentialId: 'slackID',
                teamDomain: 'unbxd',
                botUser: true
            )
        }
        always {
            junit '**/target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'extent.html,test-output/ExtentReport.html', onlyIfSuccessful: false
            archiveArtifacts artifacts: 'test-output/ExtentReport.zip', allowEmptyArchive: true
            archiveArtifacts artifacts: 'target/screenshots/*.png', allowEmptyArchive: true
            publishHTML(target: [
                allowMissing: false,
                alwaysLinkToLastBuild: true,
                keepAll: true,
                reportDir: 'test-output',
                reportFiles: 'ExtentReport.html',
                reportName: 'Extent HTML Report'
            ])
            slackSend(
                channel: '#qa-automation-reports',
                color: currentBuild.currentResult == 'SUCCESS' ? 'good' : 'danger',
                message: """${currentBuild.currentResult == 'SUCCESS' ? '✅' : '❌'} *${currentBuild.currentResult}* | `${env.JOB_NAME}` #${env.BUILD_NUMBER}\n<${env.BUILD_URL}|View Build>\n<${env.BUILD_URL}artifact/test-output/ExtentReport.html|View Extent HTML Report>\n<${env.BUILD_URL}artifact/test-output/ExtentReport.html|Download Extent HTML Report Artifact>\n<${env.BUILD_URL}artifact/test-output/ExtentReport.zip|Download Full Extent Report ZIP>\n<${env.BUILD_URL}testngreports/|View TestNG Report>\n""",
                tokenCredentialId: 'slackID',
                teamDomain: 'unbxd',
                botUser: true
            )
        }
    }
}