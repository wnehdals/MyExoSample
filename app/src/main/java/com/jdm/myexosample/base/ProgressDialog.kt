package com.jdm.myexosample.base
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.jdm.myexosample.R


class ProgressDialog(context: Context, message: String) {
    private var progressDialog: AlertDialog? = null
    private var progressMessage: TextView
    private var view: View = LayoutInflater.from(context).inflate(R.layout.layout_progressbar, null)

    init {
        var builder = AlertDialog.Builder(context, R.style.Theme_MyExoSample_AlertDialog)
        builder.setCancelable(false)
        builder.setView(view)       //AlertDialog의 화면을 설정

        progressMessage = view.findViewById(R.id.progress_message)
        progressMessage.text = message      //progressMessage 설정
        progressDialog = builder.create()
    }

    fun show() {
        progressDialog?.show()

    }

    fun dismiss() {
        progressDialog?.dismiss()
    }
}