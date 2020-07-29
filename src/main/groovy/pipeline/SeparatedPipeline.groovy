package pipeline

print("test Loaded ${getClass().simpleName}")
static def String dateTime() {
    return new Date().format('dd/MM/yyyy HH:mm:ss')
}

return this
