package com.shkolla.mungesa.utils

import org.apache.poi.hssf.usermodel.HSSFDateUtil
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.text.SimpleDateFormat

class ExcelFileReader(private val filePath: IFilePath) {

    fun validatePath(path: String): Boolean {
        if (path == "") {
            // Invalid path
            filePath.onInvalidPath()
            return false
        }

        if (!path.endsWith(".xls") && !path.endsWith(".xlsx")) {
            // Invalid file
            filePath.onInvalidFile()
            return false
        }

        if (!File(path).exists()) {
            // File does not exist
            filePath.onFileMissing()
            return false
        }

        return true
    }

    fun validateFile(file: File): Boolean {
        if (!file.exists()) {
            // File missing
            filePath.onFileMissing()
            return false
        }

        val workBook = when {
            file.path.endsWith(".xls") -> {
                HSSFWorkbook(FileInputStream(file))
            }
            file.path.endsWith(".xlsx") -> {
                XSSFWorkbook(FileInputStream(file))
            }
            else ->  null
        }

        if (workBook != null && workBook.numberOfSheets < 2){
            filePath.onWrongNumberOfSheets()
            return false
        }

            return true
    }


    companion object {

        private fun getWorkBook(path: String): Workbook? {
            var workBook: Workbook? = null

            try {
                val excelFile = FileInputStream(File(path))
                workBook = when {
                    path.endsWith(".xls") -> {
                        HSSFWorkbook(excelFile)
                    }
                    path.endsWith(".xlsx") -> {
                        XSSFWorkbook(excelFile)
                    }

                    else -> null
                }

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }


            return workBook
        }

        fun readWorkSheet(pathFile: String, read: (workBook: Workbook) -> Unit) {

            val workBook = getWorkBook(pathFile)
            if (workBook != null) {
                read(workBook)
            }
        }

        fun getCellAsString(
            row: Row,
            c: Int,
            formulaEvaluator: FormulaEvaluator
        ): String? {
            var value = ""
            try {
                val cell: Cell = row.getCell(c)
                val cellValue: CellValue = formulaEvaluator.evaluate(cell)
                when (cellValue.cellType) {
                    Cell.CELL_TYPE_BOOLEAN -> value = "" + cellValue.booleanValue
                    Cell.CELL_TYPE_NUMERIC -> {
                        val numericValue: Int = cellValue.numberValue.toInt()
                        value = if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            val date: Double = cellValue.numberValue
                            val formatter = SimpleDateFormat("MM/dd/yy")
                            formatter.format(HSSFDateUtil.getJavaDate(date))
                        } else {
                            "0$numericValue"
                        }
                    }
                    Cell.CELL_TYPE_STRING -> value = "" + cellValue.stringValue
                    else -> {
                    }
                }
            } catch (e: NullPointerException) {
                e.printStackTrace()
            }
            return value
        }
    }

}

interface IFileChooser {
    fun onFileSelected(path: String, action: () -> Unit)
}

interface IFilePath {
    fun onInvalidPath()

    fun onInvalidFile()

    fun onFileMissing()

    fun onWrongNumberOfSheets()
}