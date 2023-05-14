package com.example.wallet_mate

class IncomeActivity {

    var id:String=""
    var amount:String=""
    var title:String=""
    var category:String=""
    var date:String=""
    var note:String=""
    var timestamp:Long=0
    var uid:String=""

    constructor()

    constructor(id: String, amount: String, title: String, category: String, date: String, note: String, timestamp: Long, uid: String
    ) {
        this.id = id
        this.amount = amount
        this.title = title
        this.category = category
        this.date = date
        this.note = note
        this.timestamp = timestamp
        this.uid = uid
    }

}