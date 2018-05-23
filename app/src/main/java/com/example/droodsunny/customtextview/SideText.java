package com.example.droodsunny.customtextview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class SideText extends View {
    private enum STATE {//状态枚举类
        TOUCH, DEFAULT
    }

    private float textSize;//字体大小
    private float textSpace;//字体间隔
    private int defaultColor ;//默认字体颜色
    private int selectedColor;//选中的字体颜色
    private int touchedColor;//触摸后字体颜色

    private int tWidth;//view宽度
    private int backColor;//view背景颜色
    private Paint mPaint;//绘制文字
    private Rect mBound;//View大小
    private RectF oval1;//精确度为float，上半圆
    private RectF oval2;//下半圆

    private int currentPos;//被选中的字母

    private float textHeight;
    private STATE mSTATE=STATE.DEFAULT;
    private SlideTextOnClickListener mTextOnClickListener;

    private List<Character> lettersList;


    public SideText(Context context) {
        this(context,null);
    }

    public SideText(Context context, AttributeSet attrs) {
        this(context, attrs,0);

    }

    public SideText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //初始化字母Array
        Log.d("init","初始化完成");
        initLetters();
        TypedArray attributes=context.obtainStyledAttributes(attrs,R.styleable.SideText);
        //初始化属性
        textSize=attributes.getDimensionPixelSize(R.styleable.SideText_textSize,40);//字体默认大小
        textSpace=attributes.getDimensionPixelSize(R.styleable.SideText_textSpace,10);//
        defaultColor=attributes.getColor(R.styleable.SideText_textDefaultColor, Color.GRAY);
        selectedColor=attributes.getColor(R.styleable.SideText_selectedColor,Color.GREEN);

        touchedColor=attributes.getColor(R.styleable.SideText_textTouchedColor,Color.WHITE); //触摸之后，text的颜色
        tWidth= (int) attributes.getDimension(R.styleable.SideText_tWidth,60);
        backColor=attributes.getColor(R.styleable.SideText_backColor,Color.GRAY);
        attributes.recycle();

        /*绘制文本*/
        mPaint=new Paint();
        mBound=new Rect();
        mPaint.setAntiAlias(true);//开启抗锯齿

        mPaint.setTextSize(textSize);//获取字体大小
        mPaint.getTextBounds(String.valueOf(lettersList.get(0)),0,1,mBound);//测量字母的高度
        textHeight=mBound.height();//获得每个字母的高度

        Log.d("textHeight",textHeight+"");

        oval1=new RectF(0,0,tWidth,tWidth);//上部分半圆
        oval2=new RectF(0,26*textHeight+25*textSpace,tWidth,tWidth+26*textHeight+25*textSpace);//下半圆
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int width;
        int height;
        if(widthMode==MeasureSpec.EXACTLY){
            //如果是match_parent或者是具体值
            width=widthSize;
        }else {
            //wrap_content
            width=tWidth;
        }
        Log.d("widthSize",widthSize+"");
        Log.d("tWidth",tWidth+"");
        if (heightMode==MeasureSpec.EXACTLY){
            //如果是match_parent或者是具体值
            height=heightSize;
        }else {
            height= (int) (26 * textHeight + tWidth + textSpace * 25);
        }

        setMeasuredDimension(width,height);//设置大小
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int viewWidth=getMeasuredWidth();//获得view宽度
        int x;//绘制字母的x坐标
        int y;//绘制字母的y坐标
        switch (mSTATE){
            case DEFAULT://默认状态，透明view+灰色字母
                mPaint.setColor(defaultColor);
                mPaint.setTextSize(textSize);
                y= (int) (tWidth/2+textHeight);
                for(int i=0;i<26;i++){
                   mPaint.getTextBounds(lettersList.get(i).toString(),0,1,mBound);
                   x=(getMeasuredWidth()-mBound.width())/2;//获得绘制的x坐标起点
                    canvas.drawText(lettersList.get(i).toString(),x,y,mPaint);
                    y+=textHeight+textSpace;

                }
                break;
            case TOUCH://点击状态，灰色view+白色字母
                //绘制view背景
                mPaint.setColor(backColor);
                mPaint.setTextSize(textSize);
                canvas.drawArc(oval1, 180, 180, true, mPaint);// 画弧，参数1是RectF，参数2是角度的开始，参数3是多少度，参数4为true时画扇形，为false时画弧线
                canvas.drawArc(oval2,0,180,true,mPaint);
                canvas.drawRect(0,tWidth/2,tWidth,26*textHeight+25*textSpace+tWidth/2,mPaint);
                y= (int) (tWidth/2+textHeight);
               // mPaint.setColor(touchedColor);

                for(int i=0;i<26;i++){
                    //每绘制一个字母之前都要测量一下大小
                   mPaint.getTextBounds(lettersList.get(i).toString(),0,1,mBound);
                    x=(getMeasuredWidth() - mBound.width()) / 2;//获得绘制的x坐标起点
                    if(currentPos==i){
                        //如果是被选中的
                        mPaint.setColor(selectedColor);
                    }else {
                        mPaint.setColor(touchedColor);
                    }
                    canvas.drawText(lettersList.get(i).toString(),x,y,mPaint);
                    Log.d("position",currentPos+"");
                    y+=textHeight+textSpace;
                }
                break;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        super.onTouchEvent(event);
        float y;//记录点击纵坐标
        switch (event.getAction()){
            case MotionEvent.ACTION_UP:
                mSTATE=STATE.DEFAULT;//恢复默认状态
                 invalidate();//刷新view
                break;
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                mSTATE=STATE.TOUCH;
                y=  event.getY();//获取坐标
                if(y<=tWidth/2){//y<0或者是在上半圆，都显示第一个字母
                    currentPos=0;
                }else if(y>=(getMeasuredHeight()-tWidth/2)){//超出范围或者在下半圆
                    currentPos=25;
                }else {
                    y=y-tWidth/2;//减去上半圆的高度
                    currentPos= (int) (y/(textHeight+textSpace));
                }
                invalidate();//刷新view
                //回调
                if(mTextOnClickListener!=null){
                    mTextOnClickListener.onItemClickListener(lettersList.get(currentPos));
                }
                break;
        }


     return true;
    }

    private void initLetters(){

        lettersList=new ArrayList<>();
       for(char c='A';c<='Z';c++){
           lettersList.add(c);
       }
    }

    //设置监听回调函数
    public void setClickListener(SlideTextOnClickListener textOnClickListener){
        mTextOnClickListener=textOnClickListener;
    }
    public interface SlideTextOnClickListener{
        void onItemClickListener(char mchar);
    }


}
