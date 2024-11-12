package ro.pub.cs.systems.eim.lab05.startedservice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class StartedService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d(Constants.TAG, "onCreate() method was invoked")
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(Constants.TAG, "onBind() method was invoked")
        return null
    }

    override fun onUnbind(intent: Intent): Boolean {
        Log.d(Constants.TAG, "onUnbind() method was invoked")
        return true
    }

    override fun onRebind(intent: Intent) {
        Log.d(Constants.TAG, "onRebind() method was invoked")
    }

    override fun onDestroy() {
        Log.d(Constants.TAG, "onDestroy() method was invoked")
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(Constants.TAG, "onStartCommand() method was invoked")
        // TODO: exercise 5 - implement and start the ProcessingThread
        thread {
            while (true) {
                Log.d(Constants.TAG, "Thread.run() was invoked, PID: " + android.os.Process.myPid() + " TID: " + android.os.Process.myTid());

                Intent intent = new Intent()
                intent.setAction(Constants.ACTION_STRING)
                intent.putExtra(Constants.DATA, Constants.STRING_DATA)
                sendBroadcast(intent)

                Thread.sleep(Constants.SLEEP_TIME)

                intent.setAction(Constants.ACTION_INTEGER)
                intent.putExtra(Constants.DATA, Constants.INTEGER_DATA)
                sendBroadcast(intent)

                Thread.sleep(Constants.SLEEP_TIME)

                intent.setAction(Constants.ACTION_ARRAY_LIST)
                intent.putExtra(Constants.DATA, Constants.ARRAY_LIST_DATA)
                sendBroadcast(intent)

                Thread.sleep(Constants.SLEEP_TIME)
            }
        }
        return START_REDELIVER_INTENT
    }
}
