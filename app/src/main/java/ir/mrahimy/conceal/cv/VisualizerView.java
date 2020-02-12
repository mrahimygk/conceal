package ir.mrahimy.conceal.cv;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Random;

public class VisualizerView extends View {
    private static final int MAX_AMPLITUDE = 32767;

    private float[] amplitudes;
    private float[] vectors;
    private int insertIdx = 0;
    private Paint pointPaint;
    private Paint linePaint;
    private int width;
    private int height;
    private int mPoints = 66;
    private int mRadius;
    private int mPointRadius;
    protected Paint mPaint;
    private Paint mGPaint;
    private float[] mSrcY;
    private Random random = new Random();
    int[] thresholds = new int[mPoints];

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        linePaint = new Paint();
        linePaint.setColor(Color.GREEN);
        linePaint.setStrokeWidth(1);
        pointPaint = new Paint();
        pointPaint.setColor(Color.BLUE);
        pointPaint.setStrokeWidth(1);

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
        this.width = w;
        height = h;
        amplitudes = new float[this.width * 2]; // xy for each point across the width
        vectors = new float[this.width * 4]; // xxyy for each line across the width
        mRadius = Math.min(w, h) / 8;
        mPointRadius = Math.abs((int) (2 * mRadius * Math.sin(Math.PI / mPoints / 3)));
    }

    /**
     * modifies draw arrays. cycles back to zero when amplitude samples reach max screen size
     */
    public void addAmplitude(int amplitude) {
        invalidate();
        float scaledHeight = ((float) amplitude / MAX_AMPLITUDE) * (height - 1);
        int ampIdx = insertIdx * 2;
        amplitudes[ampIdx++] = insertIdx;   // x
        amplitudes[ampIdx] = scaledHeight;  // y
        int vectorIdx = insertIdx * 4;
        vectors[vectorIdx++] = insertIdx;   // x0
        vectors[vectorIdx++] = 0;           // y0
        vectors[vectorIdx++] = insertIdx;   // x1
        vectors[vectorIdx] = scaledHeight;  // y1
        // insert index must be shorter than screen width
        insertIdx = ++insertIdx >= width ? 0 : insertIdx;

        float amp = amplitude / 10f;
        mSrcY = new float[mPoints];
        for (int i = 1; i <= mPoints; i++) {
            mSrcY[i - 1] = amp / (random.nextInt(thresholds[i - 1]) + 1);
        }
        Log.d("TAG", "" + mSrcY[0]);
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
            canvas.rotate(-90, getWidth() / 2, getHeight() / 2);
            canvas.rotate(-i, getWidth() / 2, getHeight() / 2);
            float cx = (float) (getWidth() / 2 + mRadius);
            float cy = (float) (getHeight() / 2);
            if (value > 100) mGPaint.setColor(Color.RED);
            else mGPaint.setColor(Color.GREEN);
            canvas.drawCircle(cx + value, cy, mPointRadius, mGPaint);
            canvas.drawRect(cx, cy - mPointRadius, cx + value,
                    cy + mPointRadius, mPaint);
            canvas.restore();
        }
    }
}