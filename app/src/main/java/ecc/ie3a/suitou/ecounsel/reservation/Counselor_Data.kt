package ecc.ie3a.suitou.ecounsel.reservation

data class Counselor_Data(
    var CounselorID: String,
    var CounselorName: String,
    var CounselorGroup:String,
    var Testes: String,
    var word: String,
    var work: ArrayList<Map<String,*>>
)
