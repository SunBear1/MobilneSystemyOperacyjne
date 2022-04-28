package pl.edu.pg.lab4

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log


class MainService : Service() {
    private val SERVICETAG = "MSOLAB4"

    private lateinit var mServiceLooper: Looper
    private lateinit var mServiceHandler: IncomingHandler
    private lateinit var mMessenger: Messenger


    private inner class IncomingHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            // Tutaj umieść obsługę przesłanej wiadomości.
            Log.d(SERVICETAG, msg.data["msg"].toString())

            msg.replyTo.send(Message.obtain(null, 555).apply {
                data = Bundle().apply {
                    putString("msg", "Witaj ${msg.data["msg"]}")
                }
            })
        }
    }

    override fun onCreate() {
        // Utworzenie dedykowanego wątku o niskim priorytecie.
        val thread = HandlerThread(
            "ServiceStartArguments",
            Process.THREAD_PRIORITY_BACKGROUND
        )
        thread.start()
        mServiceLooper = thread.looper
        mServiceHandler = IncomingHandler(mServiceLooper)
        mMessenger = Messenger(mServiceHandler)
    }


    override fun onBind(intent: Intent?): IBinder {
        return mMessenger.binder
    }

    override fun onDestroy() {
        Log.d(SERVICETAG, "Shutdown")
    }
}