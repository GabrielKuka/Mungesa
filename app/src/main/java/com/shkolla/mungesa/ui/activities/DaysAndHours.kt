package com.shkolla.mungesa.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.shkolla.mungesa.R
import com.shkolla.mungesa.ui.fragments.DaysFragment
import com.shkolla.mungesa.viewmodels.DaysViewModel

class DaysAndHours : AppCompatActivity() {

    private lateinit var daysViewModel: DaysViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_days_and_hours)

        daysViewModel = ViewModelProviders.of(this).get(DaysViewModel::class.java)

        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.apply {
            replace(R.id.contentFragment, DaysFragment(), "daysFragment")
        }.commit()


    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentByTag("hoursFragment")

        if (currentFragment != null && currentFragment.isVisible) {
            daysViewModel.checkForSelectedHours()
        }
        super.onBackPressed()
    }

}
