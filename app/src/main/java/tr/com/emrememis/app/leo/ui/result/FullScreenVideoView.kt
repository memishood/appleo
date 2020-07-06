package tr.com.emrememis.app.leo.ui.result

import android.content.Context
import android.util.AttributeSet
import android.widget.VideoView

class FullScreenVideoView constructor(context: Context, attrs: AttributeSet?): VideoView(context, attrs) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec)
    }
}