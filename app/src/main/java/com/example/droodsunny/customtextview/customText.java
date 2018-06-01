package com.example.droodsunny.customtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by DroodSunny on 2018/3/6.
 */

public class customText extends View {
    /**
     * 需要绘制的文字
     */
    private ArrayList<String> textList;
    private String mText;
    /**
     * 文本的颜色
     */
    private int mTextColor;
    /**
     * 文本的大小
     */
    private float mTextSize;
    /**
     * 绘制时控制文本绘制的范围
     */
    private Rect mBound;
    private Paint mPaint;

    public customText(Context context) {
        this(context, null);
    }
    public customText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public customText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        textList=new ArrayList<>();


        //获取自定义属性的值

        TypedArray array=context.getTheme().obtainStyledAttributes(attrs,R.styleable.customText,    defStyleAttr,0);

        mText = array.getString(R.styleable.customText_mText);
        mTextColor=array.getColor(R.styleable.customText_mTextColor, Color.BLACK);
        mTextSize=array.getDimension(R.styleable.customText_mTextSize,100);


        //注意回收
        array.recycle();
        //初始化画笔
        mPaint = new Paint();
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        //获得绘制文本的宽和高
        mBound = new Rect();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);
    }
    //API21
//    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//        init();
//    }

    public void setText(String text){
        this.mText=text;
    }
    public void setTextColor(int color){
        this.mTextColor=color;
    }
    public void setTextSize(int size){
        this.mTextSize=size;
    }
    public float getTextSize(){
        return mTextSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //绘制文字
        //getWidth()获得view的宽度
        //mBound.with()获得字体的总宽度
        //从下往上画
        //绘制多行
        for(int i=0;i<textList.size();i++){
            canvas.drawText(textList.get(i), getPaddingStart(), (getPaddingTop()+mBound.height())*(i+1), mPaint);
        }
    }
    boolean isOneLines = true;
    int spLineNum;//最大行数


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        Log.d("onMeasure","我被调用了");
        float fontScale=getResources().getDisplayMetrics().scaledDensity;
        int widthMode=MeasureSpec.getMode(widthMeasureSpec);//获取宽的模式
        int heightMode=MeasureSpec.getMode(heightMeasureSpec);//获取高的模式
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);//获取宽的尺寸
        int heightSize=MeasureSpec.getSize(heightMeasureSpec);//获取高的尺寸
        Log.d("widthSize",widthSize+"");
        //获取字体的宽度
        float textWidth=mBound.width();
        //mText的长度
        //判断是否已经对文本分过段
        if(textList.size()==0){
            int padding=getPaddingLeft()+getPaddingRight();//内边距宽度
            int specWidth=widthSize-padding;//能够显示文本的最大宽度

            if(textWidth<specWidth){
                spLineNum=1;
                textList.add(mText);

            }else{
                isOneLines=false;
               /* spLineNum=((int)(textWidth/specWidth))+1;*/
               spLineNum= (int) (textWidth/specWidth)+1;
                Log.d("lineStr",mTextSize/fontScale+0.5f+"");
                Log.d("lineStr",specWidth+"");
                Log.d("lineStr",spLineNum+"");
                //每一行文字的个数
                int lineLength= (int) (specWidth/mTextSize);
                String lineStr;
                for(int i=0;i<spLineNum;i++){
                    if(mText.length()<lineLength){
                        lineStr=mText.substring(0,mText.length());
                    }else if((mText.length()-i*lineLength)<lineLength){
                        lineStr=mText.substring(i*lineLength,mText.length());
                    }else {
                        lineStr=mText.substring(i*lineLength,(i+1)*lineLength);
                    }
                    textList.add(lineStr);
                }
            }
        }
        int width;
        int height;
        //支持自己的match_parent和wrap_content
        //处理view的宽
        //如果属性是match_parent或者是具体的值，则直接赋值
        if(widthMode==MeasureSpec.EXACTLY){
            width=widthSize;
        }else{
            //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
            width=(int)(textWidth+getPaddingLeft()+getPaddingRight());
        }
        //处理view的高度
        if(heightMode==MeasureSpec.EXACTLY){
            height=heightSize;
        }else{
            //获取字体的高度
            float textHeight=mBound.height();
            //如果是一行
            if(isOneLines) {
                //控件的宽度就是文本的宽度加上两边的内边距。内边距就是padding值，在构造方法执行完就被赋值
                height = (int) (textHeight + getPaddingTop() + getPaddingBottom());
            }else {
                height=(int)(spLineNum*textHeight+getPaddingTop()*spLineNum+getPaddingBottom());
            }
        }
        setMeasuredDimension(width,height);
    }
}