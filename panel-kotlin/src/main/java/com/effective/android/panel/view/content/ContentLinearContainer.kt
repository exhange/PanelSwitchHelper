package com.effective.android.panel.view.content

import android.annotation.TargetApi
import android.content.Context
import android.support.annotation.IdRes
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import com.effective.android.panel.R

/**
 * --------------------
 * | PanelSwitchLayout  |
 * |  ----------------  |
 * | |                | |
 * | |ContentContainer| |
 * | |                | |
 * |  ----------------  |
 * |  ----------------  |
 * | | PanelContainer | |
 * |  ----------------  |
 * --------------------
 * Created by yummyLau on 2020/05/07
 * Email: yummyl.lau@gmail.com
 * blog: yummylau.com
 */
class ContentLinearContainer : LinearLayout, IContentContainer {
    @IdRes
    var editTextId = 0

    @IdRes
    var autoResetId = 0
    var autoResetByOnTouch: Boolean = true
    private lateinit var contentContainer: ContentContainerImpl

    @JvmOverloads
    constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        initView(attrs, defStyleAttr, 0)
    }

    @TargetApi(21)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ContentLinearContainer, defStyleAttr, 0)
        editTextId = typedArray.getResourceId(R.styleable.ContentLinearContainer_linear_edit_view, -1)
        autoResetId = typedArray.getResourceId(R.styleable.ContentLinearContainer_linear_auto_reset_area, -1)
        autoResetByOnTouch = typedArray.getBoolean(R.styleable.ContentLinearContainer_linear_auto_reset, autoResetByOnTouch)
        typedArray.recycle()
        orientation = VERTICAL
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        contentContainer = ContentContainerImpl(this, autoResetByOnTouch, editTextId, autoResetId)
    }

    override fun layoutContainer(l: Int, t: Int, r: Int, b: Int) {
        contentContainer.layoutContainer(l, t, r, b)
    }

    override fun findTriggerView(id: Int): View? {
        return contentContainer.findTriggerView(id)
    }

    override fun changeContainerHeight(targetHeight: Int) {
        contentContainer.changeContainerHeight(targetHeight)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val sonResume = super.dispatchTouchEvent(ev)
        getResetActionImpl().hookDispatchTouchEvent(ev, sonResume)
        return sonResume
    }


    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val onTouch = super.onTouchEvent(event)
        getResetActionImpl().hookOnTouchEvent(event)
        return onTouch
    }

    override fun getInputActionImpl(): IInputAction = contentContainer.getInputActionImpl()

    override fun getResetActionImpl(): IResetAction = contentContainer.getResetActionImpl()
}