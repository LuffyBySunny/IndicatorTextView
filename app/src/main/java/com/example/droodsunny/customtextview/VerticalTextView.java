package com.example.droodsunny.customtextview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class VerticalTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint paint;
    private Rect rect;
    //对字体分组
    private List<char[]> list;

    //每列最大的字体个数
    private int maxHeight=0;

    //设置字体
    public void setText(String text) {
        this.text = text;
    }

    //需要绘制的字体
    private String text;

    //列数
    private int columns;

    private float textSize;
    //默认从右边开始

    private Paint.Align align= Paint.Align.LEFT;
    private int textWidth;
    private int textHeight;

    public VerticalTextView(Context context) {
        super(context);
    }

    public VerticalTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array=context.getTheme().obtainStyledAttributes(attrs,R.styleable.VerticalTextView,0,0);

        textSize=array.getDimension(R.styleable.VerticalTextView_VtextSize,60);

        array.recycle();
        list=new ArrayList<>();
        paint=new Paint();
        rect=new Rect();
        //设置字体大小
        paint.setTextSize(textSize);
        paint.getTextBounds("正",0,1,rect);
        textWidth=rect.width();
        textHeight=rect.height();
        Log.d("width",rect.width()+"");
        Log.d("height",rect.height()+"");

    }

    public VerticalTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measuredWidth;
        int measuredHeight;
        int widthModel=MeasureSpec.getMode(widthMeasureSpec);
        int heightModel=MeasureSpec.getMode(heightMeasureSpec);

        //默认宽高
        int width=MeasureSpec.getSize(widthMeasureSpec);
        int height=MeasureSpec.getSize(heightMeasureSpec);

        Log.d("MeasureSpec",height+"");
        //在这测量具体的高度和宽度
        //文字个数
        int length=text.length();


        getColumns(text,length,height);

        //如果是wrap_content
        if (widthModel==MeasureSpec.AT_MOST){
            measuredWidth=columns*textWidth;
        }else {
            measuredWidth=width;
        }
        if(heightModel==MeasureSpec.AT_MOST){
            measuredHeight=maxHeight;
        }else {
            measuredHeight=height;
        }
      setMeasuredDimension(measuredWidth,measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for(int i=0;i<list.size();i++){
            Log.d("texts", String.valueOf(list.get(i)));
            char[] chars=list.get(i);
            int size=chars.length;
            for(int j=0;j<size;j++){
                Log.d("chars", String.valueOf(chars[j]));
                if (align== Paint.Align.LEFT) {
                    canvas.drawText(chars, j, 1, i * textWidth, (j+1)*textHeight, paint);
                }else {
                    canvas.drawText(chars,j,1,(columns-i-1)*textWidth,(j+1)*textHeight,paint);
                }
            }
        }

    }

    //得到具体的列数和最大的高度
    private void getColumns(String text,int length,int MaxHeight){
        list.clear();
        columns=0;
        //字符串转换为字符数组
        char[] chars=text.toCharArray();
        Log.d("chars",chars.length+" "+length);
        //本行的高度
        int myHeight=0;
        //保存上一个位置
        int j=0;
        for(int i=0;i<length;i++){
          //每一次换行更新一下最大高度，列数，保存每一列的数据,清空本行的高度
         if(MaxHeight-myHeight<textHeight&&i!=length-1){
             columns++;
             maxHeight=maxHeight>myHeight?maxHeight:myHeight;
             myHeight=0;
             list.add(subChars(chars,j,i-1));
             j=i;
             i--;
         }else if(chars[i]=='\n'){
                columns++;
                maxHeight=maxHeight>myHeight?maxHeight:myHeight;
                myHeight=0;
                list.add(subChars(chars,j,i));
                j=i+1;
            }else if(i==length-1){
             columns++;
             list.add(subChars(chars,j,i));
             //保存本列的高度
         }else {
             myHeight+=textHeight;
         }

        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
    }
    private char[] subChars(char[] chars,int i,int j){
        int length=j-i+1;
        char[] chars1=new char[length];
        for(int k=0;k<length;k++,i++){
            chars1[k]=chars[i];
        }
        return chars1;
    }
}
