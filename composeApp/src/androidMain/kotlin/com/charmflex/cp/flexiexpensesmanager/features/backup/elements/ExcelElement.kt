package com.charmflex.cp.flexiexpensesmanager.features.backup.elements

import org.apache.poi.xssf.usermodel.XSSFWorkbook

internal interface ExcelElement {
    val xssfWorkbook: XSSFWorkbook
}