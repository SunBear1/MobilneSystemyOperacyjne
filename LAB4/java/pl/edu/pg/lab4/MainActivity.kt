package pl.edu.pg.lab4

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var serviceConnection: ServiceConnection
    private lateinit var serviceConnectionAidl: ServiceConnection
    private lateinit var incomingMessenger: Messenger

    private inner class IncomingHandler(looper: Looper) : Handler(looper) {
        override fun handleMessage(msg: Message) {
            //Pierwszy wariant ustawianie wiadomości z serwisu
            val replay: TextView = findViewById(R.id.textViewReply)
            replay.text = msg.data["msg"].toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Pierwszy wariant
        waitForMessages()
        sendUserMessages()

        //Drugi wariant
        sendUserMessagesAIDL()
    }

    override fun onBackPressed() {
        //funkcja bezpieczeństwa zrywania połączenia z servicem
        unbindService(serviceConnection)
        unbindService(serviceConnectionAidl)
        super.onBackPressed()
    }

    private fun waitForMessages() {
        //wielki loop do czekania
        incomingMessenger = Messenger(IncomingHandler(this.mainLooper))
    }

    private fun sendUserMessages() {
        var isBound = false
        var myMessenger: Messenger? = null
        serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                isBound = true
                // Utworzenie obiektu Messengera umożliwiającego nadawanie wiadomości.
                myMessenger = Messenger(service)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                myMessenger = null
                isBound = false
            }
        }

        //nie dziala idk
        //var intentService = Intent(this, MainService::class.java)
        //bindService(intentService, serviceConnectionAidl, Context.BIND_AUTO_CREATE)

        val messenger = Intent()
        messenger.setClassName("pl.edu.pg.lab4", "pl.edu.pg.lab4.MainService")
        bindService(messenger, serviceConnection, Context.BIND_AUTO_CREATE)

        val sendButton: Button = findViewById(R.id.sendButton)

        sendButton.setOnClickListener {
            if (isBound) {
                val msg: Message = Message.obtain(null, 555, 0, 0)
                msg.data = Bundle().apply {
                    putString("msg", findViewById<EditText>(R.id.plainText).text.toString())
                }
                msg.replyTo = incomingMessenger
                try {
                    myMessenger!!.send(msg)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun sendUserMessagesAIDL() {
        var isBound = false
        var mService: IMyAidlInterface? = null
        serviceConnectionAidl = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                isBound = true
                // Utworzenie obiektu Messengera umożliwiającego nadawanie wiadomości.
                mService = IMyAidlInterface.Stub.asInterface(service)
            }

            override fun onServiceDisconnected(name: ComponentName) {
                mService = null
                isBound = false
            }
        }

        val serviceClass = Intent()
        serviceClass.setClassName("pl.edu.pg.lab4", "pl.edu.pg.lab4.AidlService")
        bindService(serviceClass, serviceConnectionAidl, Context.BIND_AUTO_CREATE)

        val sendButton: Button = findViewById(R.id.send2Button)
        val editText = findViewById<EditText>(R.id.plainText)
        val replayAIDL: TextView = findViewById(R.id.textViewReply2)

        sendButton.setOnClickListener {
            if (isBound) {

                try {
                    mService?.message = editText.text.toString()
                    replayAIDL.text = mService?.message
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }

}