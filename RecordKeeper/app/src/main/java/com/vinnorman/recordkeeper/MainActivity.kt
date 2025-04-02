package com.vinnorman.recordkeeper

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vinnorman.recordkeeper.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNav.setOnNavigationItemSelectedListener(this)

        supportFragmentManager.commit {
            replace(R.id.frame_content, RunningFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_running -> {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, RunningFragment())
                }
                return true
            }
            R.id.nav_cycling -> {
                supportFragmentManager.commit {
                    replace(R.id.frame_content, CyclingFragment())
                }
                return true
            }
        }
        return false
    }
}
