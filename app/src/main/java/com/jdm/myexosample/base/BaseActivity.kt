package com.jdm.myexosample.base

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.jdm.myexosample.R

abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {
    @get:LayoutRes
    abstract val layoutId: Int
    private lateinit var _binding: T
    val binding: T
        get() = _binding
    private var progressDialog: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, layoutId)
        initState()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }

    open fun initView() = Unit
    abstract fun subscribe()
    abstract fun initEvent()
    open fun initState() {
        initView()
        initEvent()
        subscribe()
    }

    fun showProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = ProgressDialog(this, getString(R.string.loading))
        progressDialog?.show()
    }

    fun dismissProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    fun showSimpleDialog(
        context: Context,
        title: String = getString(R.string.noti),
        message: String = getString(R.string.unkown_error),
        buttonText: String = getString(R.string.ok),
        buttonListener: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, p1: Int) {
                dialog?.dismiss()
            }

        },
        cancelable: Boolean = true
    ) {
        BaseDialog.makeSimpleDialog(
            context = context,
            title = title,
            message = message,
            positiveButtonText = buttonText,
            positiveButtonOnClickListener = buttonListener,
            cancelable = cancelable
            )
    }
}