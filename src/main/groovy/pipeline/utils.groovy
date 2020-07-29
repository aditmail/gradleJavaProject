package pipeline

import java.util.regex.Pattern

/** Get TimeStamp **/
static def String dateTime() {
    return new Date().format('dd/MM/yyyy HH:mm:ss')
}

/** Get Email Address Validation **/
static def Boolean emailPatterns(email) {
    def regex = "^[\\w!#\$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#\$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}\$"
    Pattern pattern = Pattern.compile(regex)

    return pattern.matcher(email).matches()
}

static def Boolean validateEmail(emailAddress) {
    return (emailAddress == null || emailAddress == "" || emailAddress == "example@email.com")
}

/**
 * --------------------------------------- Jenkins Pipeline Function ---------------------------------------------------------
 * **/

/** Set Email Address **/
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

/** Set Abort Build **/
def abortBuild(params) {
    currentBuild.result = 'ABORTED'
    error("Parameters not Accepted for: ${params}")
}

return this