package com.charmflex.cp.flexiexpensesmanager.core.di
import com.charmflex.cp.flexiexpensesmanager.core.storage.AndroidFileStorage
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.DocumentManager
import com.charmflex.cp.flexiexpensesmanager.core.storage.FileStorage
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AndroidAssetReader
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AndroidDocumentManager
import com.charmflex.cp.flexiexpensesmanager.core.utils.file.AssetReader
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


val androidMainModule = module {
    singleOf(::AndroidFileStorage) { bind<FileStorage>() }
    singleOf(::AndroidAssetReader) { bind<AssetReader>() }
    singleOf(::AndroidDocumentManager) { bind<DocumentManager>() }
}