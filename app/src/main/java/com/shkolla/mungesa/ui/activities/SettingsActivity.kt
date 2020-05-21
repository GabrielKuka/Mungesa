package com.shkolla.mungesa.ui.activities

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.obsez.android.lib.filechooser.ChooserDialog
import com.shkolla.mungesa.R
import com.shkolla.mungesa.ui.dialogs.TextDialog
import com.shkolla.mungesa.utils.IFileChooser
import com.shkolla.mungesa.utils.IFilePath
import java.io.File

class SettingsActivity : AppCompatActivity() {

    companion object {
        const val EXCEL_FILE_KEY = "excelFilePath"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        actionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat(), IFileChooser, IFilePath {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            preferenceManager.findPreference<Preference>(EXCEL_FILE_KEY)
                ?.setOnPreferenceClickListener {

                    ChooserDialog(activity)
                        .withStartFile(Environment.getExternalStorageState() + "/")
                        .withChosenListener { path, _ ->

                            onFileSelected(path) {
                                preferenceManager.sharedPreferences.edit()
                                    .putString(EXCEL_FILE_KEY, path).apply()
                            }


                        }
                        .withOnCancelListener {
                            it.cancel()
                        }
                        .build()
                        .show()

                    true
                }
        }

        override fun onFileSelected(path: String, action: () -> Unit) {
            // Validate path first
            if (!isPathValid(path)) return

            action()
        }

        override fun isPathValid(path: String): Boolean {
            if (path == "") {
                val content = resources.getString(R.string.invalid_path)
                TextDialog(content).show(requireActivity().supportFragmentManager, "")
                return false
            }

            if (!path.endsWith(".xls") && !path.endsWith(".xlsx")) {
                val content = resources.getString(R.string.invalid_file)
                TextDialog(content).show(requireActivity().supportFragmentManager, "")
                return false
            }

            if (!File(path).exists()) {
                val content = resources.getString(R.string.no_file)
                TextDialog(content).show(requireActivity().supportFragmentManager, "")
                return false
            }

            return true
        }
    }


}