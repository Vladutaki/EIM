package ro.pub.cs.systems.eim.lab10.siplinphone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import org.linphone.core.*

class MainActivity : AppCompatActivity() {
    private lateinit var core: Core
    private val coreListener = object : CoreListenerStub() {
        override fun onAccountRegistrationStateChanged(core: Core, account: Account, state: RegistrationState?, message: String) {
            when (state) {
                RegistrationState.Failed -> {
                    findViewById<Button>(R.id.register).isEnabled = true
                }

                RegistrationState.Cleared -> {
                    findViewById<LinearLayout>(R.id.register_layout).visibility = View.VISIBLE
                    findViewById<RelativeLayout>(R.id.call_layout).visibility = View.GONE
                    findViewById<Button>(R.id.register).isEnabled = true
                }

                RegistrationState.Ok -> {
                    findViewById<LinearLayout>(R.id.register_layout).visibility = View.GONE
                    findViewById<RelativeLayout>(R.id.call_layout).visibility = View.VISIBLE
                    findViewById<Button>(R.id.unregister).isEnabled = true
                    findViewById<EditText>(R.id.remote_address).isEnabled = true
                }
                else -> {}
            }
        }

        override fun onCallStateChanged(core: Core, call: Call, state: Call.State?, message: String) {
            when (state) {
                Call.State.IncomingReceived -> {
                    findViewById<Button>(R.id.hang_up).isEnabled = true
                    findViewById<Button>(R.id.answer).isEnabled = true
                }
                Call.State.Connected -> {
                    findViewById<Button>(R.id.mute_mic).isEnabled = true
                    findViewById<Button>(R.id.toggle_speaker).isEnabled = true
                    Toast.makeText(this@MainActivity, "remote party answered", Toast.LENGTH_LONG).show()
                }
                Call.State.Released -> {
                    findViewById<Button>(R.id.hang_up).isEnabled = false
                    findViewById<Button>(R.id.answer).isEnabled = false
                    findViewById<Button>(R.id.mute_mic).isEnabled = false
                    findViewById<Button>(R.id.toggle_speaker).isEnabled = false
                    findViewById<EditText>(R.id.remote_address).text.clear()
                    findViewById<Button>(R.id.call).isEnabled = true
                }
                else -> {}
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val factory = Factory.instance()
        core = factory.createCore(null, null, this)

        val status = findViewById<TextView>(R.id.registration_status)
        status.text = "linphone lib version: " + core.version

        findViewById<Button>(R.id.register).setOnClickListener {
            login()
            findViewById<LinearLayout>(R.id.register_layout).visibility = View.GONE
            findViewById<RelativeLayout>(R.id.call_layout).visibility = View.VISIBLE
        }

        findViewById<Button>(R.id.unregister).setOnClickListener {
            val account = core.defaultAccount
            if (account != null) {
                val params = account.params
                val clonedParams = params.clone()
                clonedParams.setRegisterEnabled(false)
                account.params = clonedParams
                it.isEnabled = false
            }
        }

        findViewById<Button>(R.id.call).setOnClickListener {
            outgoingCall()
            findViewById<EditText>(R.id.remote_address).isEnabled = false
            findViewById<Button>(R.id.hang_up).isEnabled = true
            it.isEnabled = false
        }

        findViewById<Button>(R.id.hang_up).setOnClickListener {
            findViewById<EditText>(R.id.remote_address).isEnabled = true
            findViewById<Button>(R.id.call).isEnabled = true

            if (core.callsNb != 0) {
                val call = core.currentCall ?: core.calls[0]
                call?.terminate()
            }
        }

        findViewById<Button>(R.id.answer).setOnClickListener {
            core.currentCall?.accept()
        }
    }

    private fun login() {
        val username = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()
        val domain = findViewById<EditText>(R.id.domain).text.toString()
        val transportType = when (findViewById<RadioGroup>(R.id.transport).checkedRadioButtonId) {
            R.id.udp -> TransportType.Udp
            R.id.tcp -> TransportType.Tcp
            else -> TransportType.Tls
        }
        val authInfo = Factory.instance().createAuthInfo(username, null, password, null, null, domain, null)

        val params = core.createAccountParams()
        val identity = Factory.instance().createAddress("sip:$username@$domain")
        params.identityAddress = identity

        val address = Factory.instance().createAddress("sip:$domain")
        address?.transport = transportType
        params.serverAddress = address
        params.setRegisterEnabled(true)
        val account = core.createAccount(params)

        core.addAuthInfo(authInfo)
        core.addAccount(account)

        core.defaultAccount = account
        core.addListener(coreListener)
        core.start()

        if (packageManager.checkPermission(Manifest.permission.RECORD_AUDIO, packageName) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO), 0)
            return
        }
    }

    private fun outgoingCall() {
        val remoteSipUri = findViewById<EditText>(R.id.remote_address).text.toString()
        val remoteAddress = Factory.instance().createAddress("sip:$remoteSipUri")
        remoteAddress ?: return
        val params = core.createCallParams(null)
        params ?: return
        params.mediaEncryption = MediaEncryption.None
        // initiate call, but status will be in coreListener:onCallStateChanged
        core.inviteAddressWithParams(remoteAddress, params)
    }
}