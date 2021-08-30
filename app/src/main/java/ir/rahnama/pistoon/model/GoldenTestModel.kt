package ir.rahnama.pistoon.model

data class GoldenTestModel(val id : Long ,val part : Long, val title : String , val answer : String) {


    constructor(part: Long,title: String,answer: String) :this(0,part, title, answer)

}
