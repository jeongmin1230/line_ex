package org.techtown.line_ex;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity {
    private MyPaintView myView;
    int count = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myView = new MyPaintView(this);

        ((LinearLayout) findViewById(R.id.paintLayout)).addView(myView);
        ((RadioGroup)findViewById(R.id.radioGroup)).setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) { // 라디오버튼 RED, GREEN, BLUE 선택 할 때마다 switch 로 이벤트
                        switch (checkedId) {
                            case R.id.btnRed:
                                myView.mPaint.setColor(Color.RED);
                                break;
                            case R.id.btnGreen:
                                myView.mPaint.setColor(Color.GREEN);
                                break;
                            case R.id.btnBlue:
                                myView.mPaint.setColor(Color.BLUE);
                                break;
                        }
                    }
                });

        Button btnTh = findViewById(R.id.btnTh);
        btnTh.setOnClickListener((new View.OnClickListener() { // btnTh 버튼을 누르면 굵기 변경
            @Override
            public void onClick(View view) {
                if(count%2==1){ // 만약 count 가 홀수이면 btnTh 버튼을 Thin 텍스트로 바꾸고 굵기를 10으로 설정, count 증가
                    btnTh.setText("Thin");
                    myView.mPaint.setStrokeWidth(10);
                    count++;
                } else { // 짝수이면 btnTh 버튼을 Thick 으로 바꾸고 굵기 20으로 설정, count 증가 = 버튼 누를 때마다 홀수 짝수로 변경됨
                    btnTh.setText("Thick");
                    myView.mPaint.setStrokeWidth(20);
                    count++;
                }
            }
        }));

        ((Button)findViewById(R.id.btnClear)).setOnClickListener(new View.OnClickListener() { // clear 버튼 누를 때 화면에 표시된 모든 것들을 지움
            @Override
            public void onClick(View view) {
                myView.mBitmap.eraseColor(Color.TRANSPARENT);
                myView.invalidate(); // invalidate 는 무효화하다 라는 뜻, 내장 메서드?
            }
        });
    }

    private static class MyPaintView extends View {
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mPaint;
        public MyPaintView(Context context) {
            super(context);
            mPath = new Path();
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
            mPaint.setAntiAlias(true);
            mPaint.setStrokeWidth(10);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawBitmap(mBitmap, 0, 0, null); //지금까지 그려진 내용
            canvas.drawPath(mPath, mPaint); //현재 그리고 있는 내용
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) { // 화면을 터치할 때의 이벤트
            int x = (int)event.getX();
            int y = (int)event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mPath.reset();
                    mPath.moveTo(x, y);
                    break;
                case MotionEvent.ACTION_MOVE:
                    mPath.lineTo(x, y);
                    break;
                case MotionEvent.ACTION_UP:
                    mPath.lineTo(x, y);
                    mCanvas.drawPath(mPath, mPaint); //mBitmap 에 기록
                    mPath.reset();
                    break;
            }
            this.invalidate();
            return true;
        }
    }
}