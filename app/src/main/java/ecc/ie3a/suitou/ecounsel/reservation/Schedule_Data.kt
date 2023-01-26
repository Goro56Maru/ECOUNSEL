package ecc.ie3a.suitou.ecounsel.reservation

import java.sql.Time

data class Schedule_Data(
    var Time: String,
    var Possible: Boolean,
    var Select_Counselor: String,
)
