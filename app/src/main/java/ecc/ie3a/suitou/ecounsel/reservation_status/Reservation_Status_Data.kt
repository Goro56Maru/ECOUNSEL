package ecc.ie3a.suitou.ecounsel.reservation_status

import java.util.Date

data class Reservation_Status_Data(
    var ID :String,
    var CounselorID : String,
    var TimeStamp:Date,
    var consaltation : String,
    var remarks : String,
)