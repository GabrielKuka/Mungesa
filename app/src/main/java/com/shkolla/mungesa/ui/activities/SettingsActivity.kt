package com.shkolla.mungesa.ui.activities

import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.meet.quicktoast.Quicktoast
import com.obsez.android.lib.filechooser.ChooserDialog
import com.shkolla.mungesa.R
import com.shkolla.mungesa.ui.dialogs.TextDialog
import com.shkolla.mungesa.utils.ExcelFileReader
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
                                Quicktoast(requireActivity()).sinfo("File u zgjodh me sukses!")
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
            val fileReader = ExcelFileReader(this)

            // Validate path
            if (!fileReader.validatePath(path)) return

            // Validate file
            if (!fileReader.validateFile(File(path))) return

            action()
        }

        override fun onWrongNumberOfSheets() {
            val fm = requireActivity().supportFragmentManager

            TextDialog(resources.getString(R.string.invalid_no_of_sheets)).show(fm, "")
        }

        override fun onInvalidPath() {
            val fm = requireActivity().supportFragmentManager

            TextDialog(resources.getString(R.string.invalid_path)).show(fm, "")
        }

        override fun onInvalidFile() {
            val fm = requireActivity().supportFragmentManager

            TextDialog(resources.getString(R.string.invalid_file)).show(fm, "")
        }

        override fun onFileMissing() {
            val fm = requireActivity().supportFragmentManager

            TextDialog(resources.getString(R.string.no_file)).show(fm, "")
        }
    }


}