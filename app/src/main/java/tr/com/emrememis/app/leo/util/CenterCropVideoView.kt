package tr.com.emrememis.app.leo.util

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

/*
* create a custom video view for center crop
* */
class CenterCropVideoView constructor(context: Context, attrs: AttributeSet?): VideoView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}