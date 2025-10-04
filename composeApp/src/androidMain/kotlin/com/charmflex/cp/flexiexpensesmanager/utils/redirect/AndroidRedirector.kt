package com.charmflex.cp.flexiexpensesmanager.utils.redirect

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import com.charmflex.cp.flexiexpensesmanager.core.app.AppConfigProvider
import com.charmflex.cp.flexiexpensesmanager.di.ActivityProvider
import com.charmflex.cp.flexiexpensesmanager.features.utils.redirect.RedirectPath
import com.charmflex.cp.flexiexpensesmanager.features.utils.redirect.Redirector


internal class AndroidRedirector(
    private val appConfigProvider: AppConfigProvider,
    private val activityProvider: ActivityProvider
) : Redirector {
    override fun redirectTo(redirectPath: RedirectPath) {
        val packageName = appConfigProvider.packageName()
        activityProvider.currentActivity?.let {
            try {
                it.startActivity(
                    Intent(
                        Intent.ACTION_VIEW, "market://details?id=$packageName".toUri()
                    )
                )
            } catch (exception: ActivityNotFoundException) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    "https://play.google.com/store/apps/details?id=$packageName".toUri()
                )
                it.startActivity(intent)
            }
        } ?: run {
            // Log error
        }
    }
}