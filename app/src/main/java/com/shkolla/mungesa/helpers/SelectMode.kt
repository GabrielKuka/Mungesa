package com.shkolla.mungesa.helpers


import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_homepage.*

object SelectMode {


    fun isEnabled() : Boolean {
        return AppData.selectMode
    }

    fun enable(a: AppCompatActivity) {
        try{
            AppData.selectMode = true
            a.student_recycler_view.adapter?.notifyDataSetChanged()
            AppData.displayMessage(a, "Select mode enabled")
        }catch(e: Exception){
            AppData.displayMessage(a, "Error disabling select mode")
        }

    }

    fun disable(a: AppCompatActivity) {
        try {
            AppData.selectMode = false
            a.student_recycler_view.adapter?.notifyDataSetChanged()
            AppData.displayMessage(a, "Select mode disabled")
        } catch (e: Exception) {
            AppData.displayMessage(a, "Error disabling select mode")
        }

    }

}