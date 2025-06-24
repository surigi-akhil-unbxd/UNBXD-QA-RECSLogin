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
                echo "Running tests on ENV: ${params.ENV}"
                echo "Using Selenium Grid URL: ${SELENIUM_GRID_URL}"
                // Pass both env and grid URL to the test
                sh "mvn test -Denv=${params.ENV} -DhubUrl=$SELENIUM_GRID_URL"
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
            archiveArtifacts artifacts: 'target/extent-report/ExtentReport.html', onlyIfSuccessful: false
        }
    }
}