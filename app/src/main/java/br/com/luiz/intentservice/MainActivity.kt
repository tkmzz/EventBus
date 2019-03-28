package br.com.luiz.intentservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.ThreadMode
import org.greenrobot.eventbus.Subscribe





class MainActivity : AppCompatActivity() {

    val receiver = ResponseReceiver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MyIntentService::class.java)
        intent.putExtra(MyIntentService.PARAM_ENTRADA, "AGORA É: ")
        startService(intent)

        rgtReceiver()
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: Status) {
        when(event){
            Status.SUCCESS-> {
                containerLoading.visibility = View.GONE
            }
            Status.ERROR-> {
                containerLoading.visibility = View.GONE
            }
            Status.LOADING-> {
                containerLoading.visibility = View.VISIBLE
            }
        }
    }

    private fun rgtReceiver() {
        val filter = IntentFilter(MyIntentService.ACTION)
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        registerReceiver(receiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    inner class ResponseReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            tvResultado.text = intent?.getStringExtra(MyIntentService.PARAM_SAIDA)

        }
    }
}
