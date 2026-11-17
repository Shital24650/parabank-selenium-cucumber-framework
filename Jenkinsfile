// Declarative pipeline. Needs a Jenkins agent with JDK 17, Maven, Python 3 and Chrome,
// plus the "HTML Publisher" plugin. Not run against a live Jenkins yet.
pipeline {
    agent any

    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    parameters {
        string(name: 'BASE_URL', defaultValue: 'https://parabank.parasoft.com/parabank', description: 'ParaBank environment under test')
        string(name: 'TAGS', defaultValue: 'not @known-issue', description: 'Cucumber tag expression')
    }

    stages {
        stage('Unit tests (no network)') {
            steps {
                sh 'mvn -B test -Dtest="unit.*Test"'
            }
        }

        stage('Functional tests (UI + API + E2E)') {
            steps {
                sh 'mvn -B test -Dtest=TestRunner -DbaseUrl=${BASE_URL} "-Dcucumber.filter.tags=${TAGS}"'
            }
        }

        stage('Test metrics') {
            steps {
                sh 'python3 scripts/metrics.py target/cucumber.json | tee target/metrics.md'
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml'
            publishHTML(target: [
                reportDir: 'target',
                reportFiles: 'cucumber-report.html',
                reportName: 'Cucumber Report',
                keepAll: true,
                allowMissing: true
            ])
            archiveArtifacts artifacts: 'target/cucumber.json,target/metrics.md,target/karate-reports/**', allowEmptyArchive: true
        }
    }
}
