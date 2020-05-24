package com.shkolla.mungesa.repos

import android.content.Context
import androidx.preference.PreferenceManager
import com.shkolla.mungesa.models.Student
import com.shkolla.mungesa.ui.activities.SettingsActivity
import com.shkolla.mungesa.utils.ExcelFileReader

class StudentRepo() {

    companion object {
        var students: MutableList<Student> = mutableListOf()
        var absencePath = ""
    }

    fun initStudents(c: Context) {

        // 1. Get the url stored in the device
        absencePath = PreferenceManager.getDefaultSharedPreferences(c)
            .getString(SettingsActivity.EXCEL_FILE_KEY, "")
            .toString()

        // 2. Clear the list if not empty
        students.clear()

        // 3. Retrieve excel data
        readExcelData()


    }

    private fun readExcelData() {
        ExcelFileReader.readWorkSheet(absencePath) { workBook ->
            val sheet = workBook.getSheetAt(AppData.STUDENT_WORKSHEET)
            val rowCount = sheet.physicalNumberOfRows
            val formulaEvaluator = workBook.creationHelper.createFormulaEvaluator()

            for (rowPos in 1 until rowCount) {

                val row = sheet.getRow(rowPos)

                val firstName = ExcelFileReader.getCellAsString(row, 0, formulaEvaluator)
                val lastName = ExcelFileReader.getCellAsString(row, 1, formulaEvaluator)
                val phoneNumber = ExcelFileReader.getCellAsString(row, 2, formulaEvaluator)

                students.add(Student(firstName!!, lastName!!, phoneNumber!!))
            }

        }
    }


}