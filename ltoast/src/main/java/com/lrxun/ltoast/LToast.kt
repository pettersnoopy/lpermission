package com.lrxun.ltoast

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.*

/**
 * Created by
 *
 * @author cl
 * @date 2020-04-22
 * from Qidianyun company
 */
object LToast {
    private val LOADED_TOAST_TYPEFACE: Typeface =
        Typeface.create("sans-serif-condensed", Typeface.NORMAL)
    private var currentTypeface: Typeface = LOADED_TOAST_TYPEFACE
    private var textSize = 16 // in SP
    private var tintIcon = true
    private var allowQueue = true
    private var lastToast: Toast? = null
    @CheckResult
    fun normal(context: Context, @StringRes message: Int): Toast {
        return normal(context, context.getString(message), Toast.LENGTH_SHORT, null, false)
    }

    @CheckResult
    fun normal(context: Context, message: CharSequence): Toast {
        return normal(context, message, Toast.LENGTH_SHORT, null, false)
    }

    @CheckResult
    fun normal(context: Context, @StringRes message: Int, icon: Drawable?): Toast {
        return normal(context, context.getString(message), Toast.LENGTH_SHORT, icon, true)
    }

    @CheckResult
    fun normal(context: Context, message: CharSequence, icon: Drawable?): Toast {
        return normal(context, message, Toast.LENGTH_SHORT, icon, true)
    }

    @CheckResult
    fun normal(context: Context, @StringRes message: Int, duration: Int): Toast {
        return normal(context, context.getString(message), duration, null, false)
    }

    @CheckResult
    fun normal(context: Context, message: CharSequence, duration: Int): Toast {
        return normal(context, message, duration, null, false)
    }

    @CheckResult
    fun normal(
        context: Context, @StringRes message: Int, duration: Int,
        icon: Drawable?
    ): Toast {
        return normal(context, context.getString(message), duration, icon, true)
    }

    @CheckResult
    fun normal(
        context: Context, message: CharSequence, duration: Int,
        icon: Drawable?
    ): Toast {
        return normal(context, message, duration, icon, true)
    }

    @CheckResult
    fun normal(
        context: Context, @StringRes message: Int, duration: Int,
        icon: Drawable?, withIcon: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            false,
            icon,
            LToastUtils.getColor(context, R.color.ltoast_normal_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun normal(
        context: Context, message: CharSequence, duration: Int,
        icon: Drawable?, withIcon: Boolean
    ): Toast {
        return custom(
            context,
            message,
            false,
            icon,
            LToastUtils.getColor(context, R.color.ltoast_normal_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun richText(context: Context, message: CharSequence): Toast {
        return custom(
            context,
            message,
            true,
            null,
            LToastUtils.getColor(context, R.color.ltoast_normal_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            Toast.LENGTH_SHORT,
            withIcon = false,
            shouldTint = false
        )
    }

    @CheckResult
    fun warning(context: Context, @StringRes message: Int): Toast {
        return warning(context, context.getString(message), Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun warning(context: Context, message: CharSequence): Toast {
        return warning(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun warning(context: Context, @StringRes message: Int, duration: Int): Toast {
        return warning(context, context.getString(message), duration, true)
    }

    @CheckResult
    fun warning(context: Context, message: CharSequence, duration: Int): Toast {
        return warning(context, message, duration, true)
    }

    @CheckResult
    fun warning(
        context: Context,
        @StringRes message: Int,
        duration: Int,
        withIcon: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            false,
            LToastUtils.getDrawable(
                context,
                R.drawable.ltoast_ic_error_outline_white_24dp
            ),
            LToastUtils.getColor(context, R.color.ltoast_warning_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun warning(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast {
        return custom(
            context,
            message,
            false,
            LToastUtils.getDrawable(
                context,
                R.drawable.ltoast_ic_error_outline_white_24dp
            ),
            LToastUtils.getColor(context, R.color.ltoast_warning_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun info(context: Context, @StringRes message: Int): Toast {
        return info(context, context.getString(message), Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun info(context: Context, message: CharSequence): Toast {
        return info(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun info(context: Context, @StringRes message: Int, duration: Int): Toast {
        return info(context, context.getString(message), duration, true)
    }

    @CheckResult
    fun info(context: Context, message: CharSequence, duration: Int): Toast {
        return info(context, message, duration, true)
    }

    @CheckResult
    fun info(context: Context, @StringRes message: Int, duration: Int, withIcon: Boolean): Toast {
        return custom(
            context,
            context.getString(message),
            false,
            LToastUtils.getDrawable(
                context,
                R.drawable.ltoast_ic_info_outline_white_24dp
            ),
            LToastUtils.getColor(context, R.color.ltoast_info_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun info(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast {
        return custom(
            context,
            message,
            false,
            LToastUtils.getDrawable(
                context,
                R.drawable.ltoast_ic_info_outline_white_24dp
            ),
            LToastUtils.getColor(context, R.color.ltoast_info_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun success(context: Context, @StringRes message: Int): Toast {
        return success(context, context.getString(message), Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun success(context: Context, message: CharSequence): Toast {
        return success(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun success(context: Context, @StringRes message: Int, duration: Int): Toast {
        return success(context, context.getString(message), duration, true)
    }

    @CheckResult
    fun success(context: Context, message: CharSequence, duration: Int): Toast {
        return success(context, message, duration, true)
    }

    @CheckResult
    fun success(
        context: Context,
        @StringRes message: Int,
        duration: Int,
        withIcon: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            false,
            LToastUtils.getDrawable(context, R.drawable.ltoast_ic_check_white_24dp),
            LToastUtils.getColor(context, R.color.ltoast_success_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun success(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast {
        return custom(
            context,
            message,
            false,
            LToastUtils.getDrawable(context, R.drawable.ltoast_ic_check_white_24dp),
            LToastUtils.getColor(context, R.color.ltoast_success_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun error(context: Context, @StringRes message: Int): Toast {
        return error(context, context.getString(message), Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun error(context: Context, message: CharSequence): Toast {
        return error(context, message, Toast.LENGTH_SHORT, true)
    }

    @CheckResult
    fun error(context: Context, @StringRes message: Int, duration: Int): Toast {
        return error(context, context.getString(message), duration, true)
    }

    @CheckResult
    fun error(context: Context, message: CharSequence, duration: Int): Toast {
        return error(context, message, duration, true)
    }

    @CheckResult
    fun error(context: Context, @StringRes message: Int, duration: Int, withIcon: Boolean): Toast {
        return custom(
            context,
            context.getString(message),
            false,
            LToastUtils.getDrawable(context, R.drawable.ltoast_ic_clear_white_24dp),
            LToastUtils.getColor(context, R.color.ltoast_error_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun error(context: Context, message: CharSequence, duration: Int, withIcon: Boolean): Toast {
        return custom(
            context,
            message,
            false,
            LToastUtils.getDrawable(context, R.drawable.ltoast_ic_clear_white_24dp),
            LToastUtils.getColor(context, R.color.ltoast_error_color),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            true
        )
    }

    @CheckResult
    fun custom(
        context: Context, @StringRes message: Int, isRichText: Boolean, icon: Drawable?,
        duration: Int, withIcon: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            isRichText,
            icon,
            -1,
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            false
        )
    }

    @CheckResult
    fun custom(
        context: Context, message: CharSequence, isRichText: Boolean, icon: Drawable?,
        duration: Int, withIcon: Boolean
    ): Toast {
        return custom(
            context,
            message,
            isRichText,
            icon,
            -1,
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            false
        )
    }

    @CheckResult
    fun custom(
        context: Context, @StringRes message: Int, isRichText: Boolean, @DrawableRes iconRes: Int,
        @ColorRes tintColorRes: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            isRichText,
            LToastUtils.getDrawable(context, iconRes),
            LToastUtils.getColor(context, tintColorRes),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            shouldTint
        )
    }

    @CheckResult
    fun custom(
        context: Context, message: CharSequence, isRichText: Boolean, @DrawableRes iconRes: Int,
        @ColorRes tintColorRes: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        return custom(
            context,
            message,
            isRichText,
            LToastUtils.getDrawable(context, iconRes),
            LToastUtils.getColor(context, tintColorRes),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            shouldTint
        )
    }

    @CheckResult
    fun custom(
        context: Context, @StringRes message: Int, isRichText: Boolean, icon: Drawable?,
        @ColorRes tintColorRes: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            isRichText,
            icon,
            LToastUtils.getColor(context, tintColorRes),
            LToastUtils.getColor(context, R.color.ltoast_default_text_Color),
            duration,
            withIcon,
            shouldTint
        )
    }

    @CheckResult
    fun custom(
        context: Context, @StringRes message: Int, icon: Drawable?,
        @ColorRes tintColorRes: Int, @ColorRes textColorRes: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        return custom(
            context,
            context.getString(message),
            false,
            icon,
            LToastUtils.getColor(context, tintColorRes),
            LToastUtils.getColor(context, textColorRes),
            duration,
            withIcon,
            shouldTint
        )
    }

    @SuppressLint("ShowToast", "InflateParams")
    @CheckResult
    fun custom(
        context: Context, message: CharSequence, isRichText: Boolean, icon: Drawable?,
        @ColorInt tintColor: Int, @ColorInt textColor: Int, duration: Int,
        withIcon: Boolean, shouldTint: Boolean
    ): Toast {
        val currentToast: Toast = Toast.makeText(context, "", duration)
        val toastLayout: View =
            (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                .inflate(R.layout.ltoast_layout, null)
        val toastIcon = toastLayout.findViewById<ImageView>(R.id.toast_icon)
        val toastTextView: TextView = toastLayout.findViewById(R.id.toast_text)
        val drawableFrame = if (shouldTint) LToastUtils.defaultDrawableFrame(
            context,
            tintColor
        ) else LToastUtils.getDrawable(context, R.drawable.ltoast_bg)
        LToastUtils.setBackground(toastLayout, drawableFrame)
        if (withIcon) {
            requireNotNull(icon) { "Avoid passing 'icon' as null if 'withIcon' is set to true" }
            LToastUtils.setBackground(
                toastIcon,
                if (tintIcon) LToastUtils.tintIcon(icon, textColor) else icon
            )
        } else {
            toastIcon.visibility = View.GONE
        }
        if (isRichText) {
            // TODO support richText
        } else {
            toastTextView.text = message
        }
        toastTextView.setTextColor(textColor)
        toastTextView.typeface = currentTypeface
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize.toFloat())
        currentToast.view = toastLayout
        if (!allowQueue) {
            if (lastToast != null) lastToast!!.cancel()
            lastToast = currentToast
        }
        return currentToast
    }

    class Config private constructor() {
        private var typeface: Typeface = currentTypeface
        private var textSize = LToast.textSize
        private var tintIcon = LToast.tintIcon
        private var allowQueue = true
        @CheckResult
        fun setToastTypeface(typeface: Typeface): Config {
            this.typeface = typeface
            return this
        }

        @CheckResult
        fun setTextSize(sizeInSp: Int): Config {
            this.textSize = sizeInSp
            return this
        }

        @CheckResult
        fun tintIcon(tintIcon: Boolean): Config {
            this.tintIcon = tintIcon
            return this
        }

        @CheckResult
        fun allowQueue(allowQueue: Boolean): Config {
            this.allowQueue = allowQueue
            return this
        }

        fun apply() {
            currentTypeface = typeface
            LToast.textSize = textSize
            LToast.tintIcon = tintIcon
            LToast.allowQueue = allowQueue
        }

        companion object {
            @get:CheckResult
            val instance: Config
                get() = Config()

            fun reset() {
                currentTypeface = LOADED_TOAST_TYPEFACE
                textSize = 16
                tintIcon = true
                allowQueue = true
            }
        }
    }
}