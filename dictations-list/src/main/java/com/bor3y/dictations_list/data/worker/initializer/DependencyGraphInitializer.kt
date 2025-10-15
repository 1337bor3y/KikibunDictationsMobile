package com.bor3y.dictations_list.data.worker.initializer

import android.content.Context
import androidx.startup.Initializer
import com.bor3y.dictations_list.data.di.InitializerEntryPoint

class DependencyGraphInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        InitializerEntryPoint.resolve(context)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}