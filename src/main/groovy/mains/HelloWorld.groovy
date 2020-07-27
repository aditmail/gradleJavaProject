package mains

import classes.Calculator
import classes.Person

class HelloWorld {

    public static void main(String[] args) {
        test()
        javaStyle()
        groovyStyle()
        calculator()

        //readFile()
        //writeFile()
    }

    static def test() {
        //Test-Test
        println "Hellow World Groovy"
        int age = 40
        String adit = "mail"
        def testing = "Adit Testing"

        println("${age.getClass()} + $testing")
        println("$age : $adit")
        //Test-Test
    }

    static def javaStyle() {
        //Example
        Person person = new Person() //Java Style Method

        //Java Style Method
        person.setFirstName("Aditya")
        person.setLastName("Smail")
        person.setAge(12)
        println("${person.getFullName()} is ${person.getAge()} old, and ${person.defineAge()}")
        println("---------------------------------------------")
    }

    static def groovyStyle() {
        //Groovy Style Method
        //def personGroov = new classes.Person() //Groovy Style Method
        def personGroov = [
                new Person(firstName: "Mail", lastName: "Adit", age: 40),
                new Person(firstName: "Subasa", lastName: "Ozora", age: 22)
        ]
        for (Person p : personGroov) {
            //println p.getFullName().dropRight(1)

            if (p.getFullName().contains("a")) {
                println p.getFullName().replace("a", "")
            }
        }

        def firstName = "Homer"

        // Apa ini??
        def map = [:]
        map."Simpson-${firstName}" = "Homer Simpson"
        assert map."Simpson-${firstName}" == "Homer Simpson"
        println map

        tryCatch(personGroov)
        closureExample(personGroov)
    }

    static def tryCatch(List<Person> person) {
        println("-----------------------------------------")
        //TryCatch
        try {
            person.get(0).getFullName().toLong()
        } catch (NumberFormatException e) {
            println "Exception $e.message"
            assert e instanceof NumberFormatException
        }
    }

    static def closureExample(List<Person> person) {
        println("\n----------------- Closure -----------------")
        //-- This printing all Data from classes.Person --
        //Closure personToString = { println person.toString() }
        //personToString()

        //-- This printing selected data you want --
        Closure personToString = { Person list ->
            //println list.toString()
            println list.getFullName()
        }

        //This.. is Equals
        person.each {
            personToString(it)
        }
        //To This
        /*for (classes.Person p : person) {
            personToString(p)
        }*/

        //Looping with Knowing Which Index
        person.eachWithIndex { Person entry, int i ->
            println "Data ke #$i : ${entry.getFirstName()}"
        }

        //Filtering element
        //Boolean to Check if true or not
        println person.find {
            //if (it.firstName == 'Subasa') println("De Name ${it.lastName}")
            it.firstName == 'Subasa'
        } == person.get(0)

        println person.collect {
            it.age <= 35
        } == [false, true]

        println person.sort {
            it.age
        } == [person[0], person[1]]

        //Closure are can be set as parameter to
        handlePerson(personToString, person)
        println("----------------- Closure -----------------\n")
    }

    static def handlePerson(Closure closure, List<Person> person) {
        if (person == null || person.size() == 0) {
            throw new RuntimeException("classes.Person Cannot be Null")
        }
        closure(person.get(1))
    }

    static def calculator() {
        println("-----------------------------------------")
        def calculator = new Calculator()
        println("Value of adding: 5 + 10 is ${calculator.adding(5, 10)}")
        println("Value of substract: 5 - 10 is ${calculator.subtract(5, 10)}")
        println("Value of multiply: 5 x 10 is ${calculator.multiply(5, 10)}")
        println("Value of divide: 5 : 10 is ${calculator.divide(5, 10)}")

        //assert calculator.divide(5, 0)

        //Cannot Divide 0
        try {
            println("Value of divide: 5 : 0 is ${calculator.divide(5, 0)}")
        } catch (RuntimeException e) {
            //assert e.getMessage() == "Cannot divide by Zero"
            println e.message.toString()
        }

        //For Value Checking...
        //assert calculator.adding(5, 11) == 15
    }

    static def readFile() {
        println("-----------------------------------------")
        List<Person> list = new ArrayList<>()
        def readFile = new File("/data.txt")
        def data = readFile.getText("UTF-8")

        data.eachLine { line, no ->
            if (line.contains(",")) {
                def person = new Person()
                def parse = line.split(",")
                parse.eachWithIndex { value, i ->
                    if (i == 0) person.setFirstName(value)
                    else if (i == 1) person.setLastName(value)
                    else if (i == 2) person.setAge(value.toInteger())
                }

                list.add(person)
            } else {
                //throw new RuntimeException("Data Should Contain ','")
            }
        }

        println("--------------------------------------------")
        println "Total Data: ${list.size()}"
        list.eachWithIndex { Person entry, int i ->
            println "Data classes.Person -${i + 1} : ${entry.getFullName()}"
        }
    }

    static def writeFile() {
        def textFile = new File("src/text/data.txt")

        if (!textFile.exists()) {
            textFile.createNewFile()
            textFile.withWriter('UTF-8') { writer ->
                writer.writeLine("Naruto,Uzumaki,35")
                writer.writeLine("Sasuke,Uchiha,33")
                writer.writeLine("Kakashi,Hatake,53")
            }
        } else {
            textFile.append("\nLuffy,Monkey,19")
            textFile.append("\nZoro,Marimo,18")
            textFile.append("\nSanji,Baka,33")
        }
    }

    static def exerciseData() {
        println("-----------------------------------------")
        List<Integer> numberCaptured = readNumbers()
        println numberCaptured

        println "Maks Number: ${numberCaptured.max()}"
        println "Total Number: ${numberCaptured.sum()}"
    }


    private static def List<Integer> readNumbers() {
        /*URI uri = getClass().getClassLoader().getResource("resources").toURI()
        def paths

        if ("jar" == uri.getScheme()) {
            FileSystem fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap(), null)
            paths = fileSystem.getPath()
            println paths
        }else{
            paths = Paths.get(uri)
            println paths
        }*/

        File resDir = new File(/resources/)
        List<Integer> allNumbers = []

        resDir.eachFile { file -> //For Each Files in Resources Directory
            def path = file.getAbsolutePath()
            if (!path.contains("META-INF")) {
                file.eachLine { line -> //For Each Lines in Each File
                    if (line.isNumber()) { //If Line is a Number
                        allNumbers.add(line.toInteger()) //Add to here
                        //allNumbers << line.toInteger()
                    }
                }
            } else {
                println "This path: ($path) Contains Denied Folder"
            }
        }

        allNumbers
    }
}
