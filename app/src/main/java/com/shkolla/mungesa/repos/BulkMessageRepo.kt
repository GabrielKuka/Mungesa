package com.shkolla.mungesa.repos

import android.content.Context
import androidx.preference.PreferenceManager
import com.shkolla.mungesa.models.BulkMessage
import com.shkolla.mungesa.ui.activities.SettingsActivity
import com.shkolla.mungesa.utils.ExcelFileReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BulkMessageRepo() {
    companion object {
        val bulkMessages: MutableList<BulkMessage> = mutableListOf()
        var bulkSmsPath = ""
    }

    fun initBulkMessages(c: Context) {

        // 1. Get the path stored in the device
        bulkSmsPath = PreferenceManager.getDefaultSharedPreferences(c).getString(
            SettingsActivity.EXCEL_FILE_KEY, ""
        ).toString()

        // 2. Clear the list if not empty
        bulkMessages.clear()

        // 3. Retrieve Excel Data
        readExcelData()
    }

    private fun readExcelData() {
        ExcelFileReader.readWorkSheet(bulkSmsPath) { workBook ->
            val sheet = workBook.getSheetAt(AppData.BULK_SMS_WORKSHEET)
            val rowCount = sheet.physicalNumberOfRows
            val formulaEvaluator = workBook.creationHelper.createFormulaEvaluator()

            CoroutineScope(Dispatchers.Default).launch {
                for (rowPos in 1 until rowCount) {
                    val row = sheet.getRow(rowPos)

                    val studentFullName = ExcelFileReader.getCellAsString(row, 0, formulaEvaluator)
                    val message = ExcelFileReader.getCellAsString(row, 1, formulaEvaluator)
                    val phoneNumber = ExcelFileReader.getCellAsString(row, 2, formulaEvaluator)

                    bulkMessages.add(BulkMessage(studentFullName!!, message!!, phoneNumber!!))

                }
            }
        }
    }


}