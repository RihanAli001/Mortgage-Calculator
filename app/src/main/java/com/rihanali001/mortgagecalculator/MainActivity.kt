package com.rihanali001.mortgagecalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.rihanali001.mortgagecalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding :ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.navView.setNavigationItemSelectedListener { item ->
            binding.navView.setCheckedItem(item.itemId)
            when (item.itemId){
                R.id.emiCalcuFrag -> {
                    replaceFragment(EmiCalculatorFragment())
                    true
                }
                R.id.simpleCalcuFrag -> {
                    replaceFragment(SimpleCalculatorFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(EmiCalculatorFragment())
    }

    private fun replaceFragment(fragment : Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }
}