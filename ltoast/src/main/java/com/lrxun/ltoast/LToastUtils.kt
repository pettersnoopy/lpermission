package com.lrxun.ltoast

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.PorterDuff
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

/**
 * Created by
 *
 * @author cl
 * @date 2020-04-22
 * from Qidianyun company
 */
object LToastUtils {
    fun tintIcon(drawable: Drawable, tintColor: Int): Drawable {
        drawable.setColorFilter(tintColor, PorterDuff.Mode.SRC_IN)
        return drawable
    }

    fun defaultDrawableFrame(context: Context, tintColor: Int): Drawable {
        val drawable = getDrawable(context, R.drawable.ltoast_bg)
        return tintIcon(drawable!!, tintColor)
    }

    fun setBackground(view: View, drawable: Drawable?) {
        view.background = drawable
    }

    fun getDrawable(context: Context, id: Int): Drawable? {
        return AppCompatResources.getDrawable(context, id)
    }

    fun getColor(context: Context, @ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }
}