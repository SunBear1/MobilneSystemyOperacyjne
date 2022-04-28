package pl.edu.pg.lab4

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AidlService : Service() {
    val newTXT = "AIDL got you: "

    override fun onBind(intent: Intent): IBinder {
        return object : IMyAidlInterface.Stub() {
            var msg = newTXT
            override fun getMessage() = newTXT + msg
            override fun setMessage(msg: String?) {
                this.msg = msg ?: "Empty <3"
            }
        }
    }
}