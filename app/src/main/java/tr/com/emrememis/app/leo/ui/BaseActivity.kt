package tr.com.emrememis.app.leo.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_base.*
import tr.com.emrememis.app.leo.R

class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        Thread.UncaughtExceptionHandler { _, _ ->   }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        NavigationUI.setupWithNavController(toolbar, navController)
    }
}