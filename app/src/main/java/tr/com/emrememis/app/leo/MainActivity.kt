package tr.com.emrememis.app.leo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import tr.com.emrememis.app.leo.ui.BaseActivity

class MainActivity : AppCompatActivity() {

    companion object {
        const val time = 1000L
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        * we don't need make anything on this activity currently, so going to base activity after a second
        * */

        runOnHandler {
            val intent = Intent(this, BaseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

    private fun runOnHandler(action: () -> Unit) {
        Handler().postDelayed(Runnable(action),
            time
        )
    }

    override fun onBackPressed() {
        finishAffinity()
    }
}