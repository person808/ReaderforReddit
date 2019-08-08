package com.kainalu.readerforreddit.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Checkable
import android.widget.LinearLayout
import com.kainalu.readerforreddit.R
import com.kainalu.readerforreddit.user.LoggedOutUser
import com.kainalu.readerforreddit.user.UserData
import kotlinx.android.synthetic.main.account_switcher_item.view.*

class AccountSwitcherItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
): LinearLayout(context, attrs, defStyle, defStyleRes), Checkable {

    private var checked = false

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.account_switcher_item, this, true)
        isClickable = true
        isFocusable = true
        val height = context.resources.getDimensionPixelOffset(R.dimen.bottom_sheet_item_height)
        layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, height)
        orientation = HORIZONTAL
        addRipple()
    }

    override fun isChecked(): Boolean {
        return checked
    }

    override fun toggle() {
        checked = !checked
        updateCheckVisibility()
    }

    override fun setChecked(checked: Boolean) {
        this.checked = checked
        updateCheckVisibility()
    }

    private fun updateCheckVisibility() {
        checkImageView.visibility = if (checked) View.VISIBLE else View.GONE
    }

    fun setUserData(userData: UserData) {
        render(userData)
    }

    private fun render(userData: UserData) {
        if (userData is LoggedOutUser) {
            usernameTextView.text = context.getString(R.string.anonymous_user)
        } else {
            usernameTextView.text = userData.username
        }
    }
}