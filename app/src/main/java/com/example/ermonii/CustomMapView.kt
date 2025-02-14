package com.example.ermonii

import android.content.Context
import android.util.AttributeSet
import org.osmdroid.views.MapView

class CustomMapView : MapView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    var isInteracting = false

    override fun onTouchEvent(event: android.view.MotionEvent): Boolean {
        when (event.action) {
            android.view.MotionEvent.ACTION_DOWN -> isInteracting = true
            android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> isInteracting = false
        }
        return super.onTouchEvent(event)
    }
}