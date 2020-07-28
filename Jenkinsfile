pipeline {
    agent any
    tools {
        jdk 'Jdk.1.8'
        gradle 'gradle.6.6'
    }

    environment {
        jobName = "${env.JOB_NAME}"
        buildURL = "${env.BUILD_URL}"
        buildNumber = "${env.BUILD_NUMBER}"

        buildID = "${env.BUILD_ID}"
        buildTag = "${env.BUILD_TAG}"

        timeStamp = new Date().format('dd/MM/yyyy HH:mm:ss')
    }

    stages {
        stage('Initialize-Stage') {
            steps {
                bat "java -version"
                bat "gradle -v"
                echo "Initialize Stage Running at ${timeStamp}"
            }
        }

        stage('Build-Stage') {
            steps {
                echo "Clone Repository from Github at ${timeStamp}"
                git(
                        [
                                branch       : "master",
                                credentialsId: "b5656ab7-b7ed-4c02-9833-9af6877e2b9e",
                                url          : "https://github.com/aditmail/gradleJavaProject.git"
                        ]
                )
            }
        }

        stage('Unit-Test Stage') {
            steps {
                echo "Unit Test Running at ${timeStamp}"
                bat "gradle clean check test jar"
            }
        }
    }

    post {
        success {
            echo "Build Success at ${timeStamp}"
            junit testResults: "**/build/test-results/test/TEST-*.xml"
            
        }
    }
}