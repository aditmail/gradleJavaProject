def inputEmail() {
    input(
            message: 'Hey, you haven\'t set email before.. please set first',
            ok: 'Insert',
            parameters: [
                    string(
                            defaultValue: 'example@email.com',
                            description: '''<p style="color:red;">*Required</p><h5>Insert <b style="color:blue">Email Address</b> to Send Notification Email</h5>''',
                            name: 'inputEmailTo',
                            trim: true
                    )
            ]
    )
}

pipeline {
    agent any
    tools {
        jdk 'Jdk.1.8'
        gradle 'gradle.6.6'
    }

    options {
        timestamps()
    }

    environment {
        jobName = "${env.JOB_NAME}"
        buildURL = "${env.BUILD_URL}"
        buildNumber = "${env.BUILD_NUMBER}"

        buildID = "${env.BUILD_ID}"
        buildTag = "${env.BUILD_TAG}"
    }

    stages {
        stage('Initialize-Stage') {
            environment {
                def timeStamp = new Date().format('dd/MM/yyyy HH:mm:ss')
                def inputEmailTo = null
            }

            steps {
                bat "java -version"
                bat "gradle -v"
                echo "Initialize Stage Running at ${timeStamp}"

                script {
                    if (emailto == "example@email.com") {
                        echo "Seems Like you Haven\'t Set Email Yet, Requesting New Input.."
                        inputEmailTo = inputEmail()
                    }

                    if (inputEmailTo != null) {
                        emailto = inputEmailTo
                    }
                }
            }
        }

        stage('Build-Stage') {
            environment {
                def timeStamp = new Date().format('dd/MM/yyyy HH:mm:ss')
            }

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
            environment {
                def timeStamp = new Date().format('dd/MM/yyyy HH:mm:ss')
            }

            steps {
                echo "Unit Test Running at ${timeStamp}"
                bat "gradle clean build check test jar"
            }
        }
    }

    post {
        success {
            echo "Build are Successfull"
            mail(
                    [
                            body   : """Test Successfully Build at this:\n${buildURL}\n\nBuild Number\t\t: ${buildNumber}\nBuild Tag\t\t: ${buildTag}""",
                            from   : "aditya@jenkins.com",
                            subject: "Success in Build Jenkins:\n${jobName} #${buildNumber}",
                            to     : "${emailto}"
                    ]
            )
            script {
                if (params.JUnit) {
                    echo "Generating JUnit Reports"
                    junit testResults: "**/build/test-results/test/TEST-*.xml"
                }

                if (params.Checkstyle) {
                    echo "Generating Checkstyle Reports"
                    //checkstyle pattern: "**/build/reports/checkstyle/*.xml"
                    recordIssues(
                            tools:
                                    [
                                            checkStyle(pattern: '**/build/reports/checkstyle/*.xml')
                                    ]
                    )
                }
            }
        }

        failure {
            echo "Failure are Occurs"
            mail(
                    [
                            body   : """Test Failed Occurs\nCheck Console Output at below to see Detail\n${buildURL}\n\nBuild ID\t\t: ${buildID}\nBuild Number \t\t: ${buildNumber}\nBuild Tag\t\t: ${buildTag}""",
                            from   : "aditya@jenkins.com",
                            subject: "Failure in Build Jenkins: ${jobName} #${buildNumber}",
                            to     : "${emailto}"
                    ]
            )
        }
    }
}