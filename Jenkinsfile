def moduleScript = [:] //Global Variable for Define Groovy Scripts

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

        //Define Variable for Utils Script Class
        def utilsScript = "$moduleScript.utils"
    }

    stages {
        stage('Initialize-Stage') {
            environment {
                def emailAddress = null
            }

            steps {
                script {
                    utilsScript = load("${env.WORKSPACE}/src/main/groovy/pipeline/utils.groovy")
                    echo "Initialize Stage Running at ${utilsScript.dateTime()}"

                    if (utilsScript.validateEmail(emailto)) {
                        echo "Seems Like you Haven\'t Set Email Yet, Requesting New Input.."
                        emailAddress = utilsScript.inputEmail()
                    } else {
                        if (utilsScript.emailPatterns(emailto)) {
                            emailAddress = emailto
                        } else {
                            echo "Seems Like you Set Email Invalid Email, Requesting New Input.."
                            emailAddress = inputEmail()
                        }
                    }

                    //Checking again if email valid or not
                    if (utilsScript.validateEmail(emailAddress)) {
                        utilsScript.abortBuild(emailAddress)
                    }
                }

                bat "java -version"
                bat "gradle -v"
            }
        }

        stage('Build-Stage') {
            steps {
                script {
                    echo "Clone Repository from Github at ${utilsScript.dateTime()}"
                }

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
                script {
                    echo "Unit Test Running at ${utilsScript.dateTime()}"
                }
                bat "gradle clean build check test jar"
            }
        }
    }

    post {
        /*always {
            script {
                echo "Builds are ${currentBuild.currentResult} at ${utilsScript.dateTime()}"
            }
        }*/

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

            /*publishHTML target: [
                    allowMissing         : false,
                    alwaysLinkToLastBuild: false,
                    keepAll              : true,
                    reportDir            : "coverage",
                    reportFiles          : 'index.html',
                    reportName           : 'JUnit-Reports'
            ]*/
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