package com.kosmasfn.calculatorscan.view.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    protected open lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getInflatedLayout(layoutInflater))
        setUp(savedInstanceState)
    }

    private fun getInflatedLayout(inflater: LayoutInflater): View {
        binding = setBinding(inflater)
        return binding.root
    }

    abstract fun setBinding(layoutInflater: LayoutInflater): VB
    abstract fun setUp(savedInstanceState: Bundle?)

}