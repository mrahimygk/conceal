package ir.mrahimy.conceal.util.cv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Random;

public class VisualizerView extends View {
   // private static final int MAX_AMPLITUDE = 32767;

    private final int mPoints = 66;
    private int mRadius;
    private int mPointRadius;
    protected final Paint mPaint;
    private final Paint mGPaint;
    private float[] mSrcY;
    private final Random random = new Random();
    int[] thresholds = new int[mPoints];

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#2196F3"));
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.FILL);

        mGPaint = new Paint();
        mGPaint.setAntiAlias(true);

        mSrcY = new float[mPoints];
        for (int i = 0; i < mPoints; i++) {
            thresholds[i] = random.nextInt(66) + 1;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mRadius = Math.min(w, h) / 8;
        mPointRadius = Math.abs((int) (2 * mRadius * Math.sin(Math.PI / mPoints / 3)));
    }

    /**
     * modifies draw arrays. cycles back to zero when amplitude samples reach max screen size
     */
    public void addAmplitude(int amplitude) {
        invalidate();
        float amp = amplitude / 10f;
        mSrcY = new float[mPoints];
        for (int i = 1; i <= mPoints; i++) {
            mSrcY[i - 1] = amp / (random.nextInt(thresholds[i - 1]) + 1);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        for (int i = 0; i < 360; i = i + 360 / mPoints) {
            float cx = (float) (getWidth() / 2 + Math.cos(i * Math.PI / 180) * mRadius);
            float cy = (float) (getHeight() / 2 - Math.sin(i * Math.PI / 180) * mRadius);
            canvas.drawCircle(cx, cy, mPointRadius, mPaint);
        }

        for (int i = 0; i < 360; i = i + 360 / mPoints) {
            float value = mSrcY[i * mPoints / 360];
            if (value == 0) continue;
            if (value > 222) value = 222;
            canvas.save();
            float width = getWidth() / (float) 2;
            float height = getHeight()/ (float) 2;
            canvas.rotate(-90, width, height);
            canvas.rotate(-i, width, height);
            float cx = (float) (width + mRadius);
            if (value > 100) mGPaint.setColor(Color.RED);
            else mGPaint.setColor(Color.GREEN);
            canvas.drawCircle(cx + value, width, mPointRadius, mGPaint);
            canvas.drawRect(cx, width - mPointRadius, cx + value,
                    width + mPointRadius, mPaint);
            canvas.restore();
        }
    }
}