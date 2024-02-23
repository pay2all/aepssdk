package com.pay2all.aeps

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView


class CustomeProgressBar constructor(val context: Context){


     var alertDialog:AlertDialog?=null
    lateinit var iv_rotate:ImageView

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v2 = inflater.inflate(R.layout.custom_alert_dialog_for_progressbar, null)

        iv_rotate=v2.findViewById<ImageView>(R.id.iv_rotate)

        val builder2 = AlertDialog.Builder(context)
        builder2.setCancelable(false)

        builder2.setView(v2)
        alertDialog = builder2.create()
        if (alertDialog?.getWindow() != null) {
            alertDialog?.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }

    fun mShowDialog()
    {
        val animRotate: Animation = AnimationUtils.loadAnimation(context, R.anim.rotate_animation)
        iv_rotate.startAnimation(animRotate)
        if (alertDialog!=null)
        {
            alertDialog!!.show()
        }
    }


    fun mDismissDialog()
    {
        if (alertDialog!=null)
        {
            if (alertDialog!!.isShowing)
            {
                alertDialog!!.dismiss()
            }
        }
    }

}