import java.util.regex.Pattern

def inputEmail() {
    input(
            message: 'Hey, you haven\'t set email before.. please set first',
            ok: 'Insert',
            parameters: [
                    string(defaultValue: 'example@email.com',
                            description: '''<p style="color:red;">*Required</p><h5>Insert <b style="color:blue">Email Address</b> to Send Notification Email</h5>''',
                            name: 'inputEmailTo',
                            trim: true)
            ]
    )
}

static def Boolean emailPatterns(email) {
    def regex = "^[\\w!#\$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$"
    Pattern pattern = Pattern.compile(regex)
    return pattern.matcher(email).matches()
}

def abortBuild(params) {
    currentBuild.result = 'ABORTED'
    error("Paramaters not Accepted for: ${params}")
}

static def String dateTime() {
    return new Date().format('dd/MM/yyyy HH:mm:ss')
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
                def timeStamp = dateTime()
                def emailAddress = null
            }

            steps {
                bat "java -version"
                bat "gradle -v"
                echo "Initialize Stage Running at ${timeStamp}"

                script {
                    if (emailto == null || emailto == "" || emailto == "example@email.com") {
                        echo "Seems Like you Haven\'t Set Email Yet, Requesting New Input.."
                        emailAddress = inputEmail()
                    } else {
                        if (emailPatterns(emailto)) {
                            emailAddress = emailto
                        } else {
                            echo "Seems Like you Set Email Invalid Email, Requesting New Input.."
                            emailAddress = inputEmail()
                        }
                    }

                    //Checking again if email valid or not
                    if (emailAddress == null || emailAddress == "" || emailAddress == "example@email.com" || !emailPatterns(emailAddress)) {
                        abortBuild(emailAddress)
                    }
                }
            }
        }

        stage('Build-Stage') {
            environment {
                def timeStamp = dateTime()
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
                def timeStamp = dateTime()
            }

            steps {
                echo "Unit Test Running at ${timeStamp}"
                bat "gradle clean build check test jar"
            }
        }
    }

    post {
        always {
            echo "Builds are ${currentBuild.currentResult} at ${dateTime()}"
        }

        success {
            mail([
                    body   : """Test Successfully Build at this:\n${buildURL}\n\nBuild Number\t\t: ${buildNumber}\nBuild Tag\t\t: ${buildTag}""",
                    from   : "aditya@jenkins.com",
                    subject: "Success in Build Jenkins:\n${jobName} #${buildNumber}",
                    to     : "${emailAddress}"
            ])
            script {
                if (params.JUnit) {
                    echo "Generating JUnit Reports"
                    junit testResults: "**/build/test-results/test/TEST-*.xml"
                }

                if (params.Checkstyle) {
                    echo "Generating Checkstyle Reports"
                    //checkstyle pattern: "**/build/reports/checkstyle/*.xml"
                    recordIssues(
                            tools: [
                                    checkStyle(pattern: '**/build/reports/checkstyle/*.xml')
                            ]
                    )
                }
            }

            publishHTML target: [
                    allowMissing         : false,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : "${jobName}/build/reports/tests/test/",
                    reportFiles          : 'index.html',
                    reportName           : 'JUnit-Reports'

            ]
        }

        failure {
            mail(
                    [
                            body   : """Test Failed Occurs\nCheck Console Output at below to see Detail\n${buildURL}\n\nBuild ID\t\t: ${buildID}\nBuild Number \t\t: ${buildNumber}\nBuild Tag\t\t: ${buildTag}""",
                            from   : "aditya@jenkins.com",
                            subject: "Failure in Build Jenkins: ${jobName} #${buildNumber}",
                            to     : "${emailAddress}"
                    ]
            )
        }
    }
}