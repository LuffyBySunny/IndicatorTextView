package com.example.droodsunny.customtextview;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button mButton;
    private SideText mSideText;

    private TextView letterText;

    private PopupWindow mPopupWindow;
    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    //做具体的处理
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Toast.makeText(this,"you clicked add",Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  mButton=findViewById(R.id.button);*/

       /* mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent("com.hh");
               sendBroadcast(intent);
            }
        });*/


//      通知
        Intent intent=new Intent(this,Main2Activity.class);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager manager= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification=new Notification.Builder(this).setSmallIcon(R.drawable.ic_launcher_background).
                setContentIntent(pendingIntent).
                setContentText("曼曼给你来电话了").setContentTitle("未接来电").setWhen(System.currentTimeMillis()).
                build();

      notification.flags=Notification.FLAG_AUTO_CANCEL;
      manager.notify(1,notification);

      /*popupWindow*/
       @SuppressLint("InflateParams") final View popupView= getLayoutInflater().inflate(R.layout.popupwindow_layout,null);
       letterText=popupView.findViewById(R.id.letter);
       final Handler handler=new Handler();


       mPopupWindow=new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
       mPopupWindow.setFocusable(false);
       mPopupWindow.setOutsideTouchable(false);

       mSideText=findViewById(R.id.side);
       mSideText.setClickListener(new SideText.SlideTextOnClickListener() {
           @Override
           public void onItemClickListener(char mchar) {
               mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
               letterText.setText(String.valueOf(mchar));
               //在发送消息之前，先清除之前的消息，防止对现在的行为进行干扰
               handler.removeCallbacksAndMessages(null);
               handler.postDelayed(new Runnable() {
                   @Override
                   public void run() {
                       if(mPopupWindow!=null) {
                           mPopupWindow.dismiss();
                       }
                   }
               },500);
               /* if(mPopupWindow.isShowing()){
                    letterText.setText(String.valueOf(mchar));
                    //在发送消息之前，先清除之前的消息，防止对现在的行为进行干扰
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mPopupWindow!=null) {
                                mPopupWindow.dismiss();
                            }
                        }
                    },500);
                }else {
                    mPopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER,0,0);
                    handler.removeCallbacksAndMessages(null);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(mPopupWindow!=null){
                                mPopupWindow.dismiss();
                            }
                        }
                    },500);
                }*/
           }
       });
        DisplayMetrics dm=new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
        Log.d("screen",""+dm.widthPixels);
        Log.d("screen",""+dm.heightPixels);

     //   MyReceiver receiver=new MyReceiver();


      //  myText.setText("曼曼我爱你");

    }

    /*@Override
    protected void onStart() {
        super.onStart();
        myText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Log.d("screen", "" + myText.getWidth());
            }
        });
    }*/



}
