package id.emergence.wher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // setting up nav component
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_container) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.nav_main)

        lifecycleScope.launch {
            if (isLoggedIn()) {
                graph.setStartDestination(R.id.homeFragment)
            } else {
                graph.setStartDestination(R.id.splashFragment)
            }
            // bind navGraph to fragment
            val navController = navHostFragment.navController
            navController.setGraph(graph, intent.extras)
        }
    }

    private fun isLoggedIn() = false
}
