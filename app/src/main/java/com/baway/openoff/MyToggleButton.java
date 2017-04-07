package com.baway.openoff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author 任珏
 * @类的用途
 * @date 2017/4/7 13:15
 */
public class MyToggleButton extends View implements View.OnClickListener {

    private Bitmap switch_background;
    private Bitmap slide_button;
    private Paint paint;
    //滑动按钮的左边界
    private float slide_left;
    //当前开关的状态
    private boolean currentState=false;

    /**
     * 代码里创建对象时用此构造方法
     * @param context
     */
    public MyToggleButton(Context context) {
        super(context);
    }

    /**
     * 布局文件里创建对象由系统自动调用
     * @param context 上下文
     * @param attrs 属性集
     */
    public MyToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化
        initView();
    }

    /**
     * 初始化
     */
    private void initView() {
        //初始化图片
        switch_background = BitmapFactory.decodeResource(getResources(), R.drawable.switch_background);
        slide_button = BitmapFactory.decodeResource(getResources(), R.drawable.slide_button);
        //初始化画笔
        paint = new Paint();
        //添加onClick监听事件
        setOnClickListener(this);
    }

    /**
	 * view 对象显示的屏幕上，有几个重要步骤：
	 * 1、构造方法 创建 对象。
	 * 2、测量view的大小。	onMeasure(int,int);
	 * 3、确定view的位置 ，view自身有一些建议权，决定权在 父view手中。  onLayout();
	 * 4、绘制 view 的内容 。 onDraw(Canvas)
	 */

    /**
     * 测量尺寸时的回调方法
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //设置当前view的大小
        //width:view的宽度
        //height：view的高度
        setMeasuredDimension(switch_background.getWidth(),switch_background.getHeight());
    }

    /**
     * 绘制当前view的内容
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        /**
         * 绘制背景
         * backgroundBitmap 要绘制的图片
         * left 图片的左边界
         * top 图片的上边界
         * paint 绘制图片要用的画笔
         */
        canvas.drawBitmap(switch_background,0,0,paint);
        //绘制可滑动的按钮
        canvas.drawBitmap(slide_button,slide_left,0,paint);
    }
    //判断是否发生了拖动，如果发生了就不响应点击事件
    private boolean isDrag=false;
    @Override
    public void onClick(View v) {
        if (!isDrag){
            //改变状态值
            currentState=!currentState;
            //刷新当前状态
            flushState();
        }
    }

    private void flushState() {
        if (currentState){
            slide_left=switch_background.getWidth()-slide_button.getWidth();
        }else{
            slide_left=0;
        }
        //刷新当前视图，导致onDraw方法执行
        flushView();
    }
    private int firstX;
    private int lastX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                firstX=lastX= (int) event.getX();
                isDrag=false;
                break;
            case MotionEvent.ACTION_MOVE:
                //判断是否发生拖动
                if (Math.abs(event.getX()-firstX)>5){
                    isDrag=true;
                }
                //计算手指在屏幕上移动的距离
                int dis= (int) (event.getX()-lastX);
                //将本次的位置给lastX
                lastX= (int) event.getX();
                //个同居手指移动的距离，改变slide_left的值
                slide_left=slide_left+dis;
                break;
            case MotionEvent.ACTION_UP:
                if (isDrag) {
                    int maxLeft = switch_background.getWidth() - slide_button.getWidth();
                    if (slide_left > maxLeft / 2) {
                        currentState = true;
                    } else {
                        currentState = false;
                    }
                    flushState();
                }
                break;
        }
        flushView();
        return true;
    }

    private void flushView() {
        int maxLeft=switch_background.getWidth()-slide_button.getWidth();
        slide_left=(slide_left>0)?slide_left:0;
        slide_left=(slide_left<maxLeft)?slide_left:maxLeft;
        invalidate();
    }
}
