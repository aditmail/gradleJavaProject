package classes

import groovy.transform.Canonical

@Canonical
class Person {
    String firstName
    String lastName
    int age

    String getFullName() {
        "Hellow, $firstName $lastName!"
    }

    String defineAge() {
        if (age <= 20) {
            "He/She is a New Comer"
        } else if (age >= 21 && age <= 40) {
            "He/She is Middle Age"
        } else {
            "He/She is The Mature.."
        }
    }
}
