package com.jdm.myexosample.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.BUTTON_NEGATIVE
import android.content.DialogInterface.BUTTON_POSITIVE
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.jdm.myexosample.R

object BaseDialog {
    fun makeSimpleDialog(
            context: Context,
            title: String? = null,
            message: String,
            positiveButtonText: String = "OK",
            negativeButtonText: String? = null,
            positiveButtonOnClickListener: DialogInterface.OnClickListener,
            negativeButtonOnClickListener: DialogInterface.OnClickListener? = null,
            cancelable: Boolean = true
    ): Dialog {
        val view = LayoutInflater.from(context).inflate(R.layout.dialog_simple, null)
        val textViewTitle = view.findViewById<TextView>(R.id.text_view_title)
        val textViewMessage = view.findViewById<TextView>(R.id.text_view_message)
        val buttonPositive = view.findViewById<Button>(R.id.button_positive)
        val buttonNegative = view.findViewById<Button>(R.id.button_negative)

        val builder = AlertDialog.Builder(context)
                .setView(view)

        val dialog = builder.create()
        dialog.setCancelable(cancelable)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        textViewMessage.text = message

        buttonPositive.text = positiveButtonText
        buttonPositive.setOnClickListener {
            positiveButtonOnClickListener.onClick(dialog, BUTTON_POSITIVE)
        }

        if (negativeButtonText == null)
            buttonNegative.visibility = View.GONE
        else {
            buttonNegative.text = negativeButtonText
            buttonNegative.setOnClickListener {
                positiveButtonOnClickListener.onClick(dialog, BUTTON_NEGATIVE)
            }
        }

        if (title == null)
            textViewTitle.visibility = View.GONE
        else
            textViewTitle.text = title

        return dialog
    }


}