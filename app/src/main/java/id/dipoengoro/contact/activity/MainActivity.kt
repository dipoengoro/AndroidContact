package id.dipoengoro.contact.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import id.dipoengoro.contact.R
import id.dipoengoro.contact.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        navController = (supportFragmentManager.findFragmentById(R.id.fragmentContainerView)
                as NavHostFragment).navController
        NavigationUI.setupActionBarWithNavController(
            this@MainActivity,
            navController,
            AppBarConfiguration(navController.graph)
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            navController.navigateUp()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}