package com.example.mywebdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.mywebdemo.history.historyActivity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {


    public ArrayList<String> urlList = new ArrayList<String>();
    public ArrayList<String> nameList = new ArrayList<String>();
    public ArrayList<String> flagList = new ArrayList<String>();
    public String  currenturl="";
    public String  currenttitle="";
    private WebView webView;
    private String url="";
    //注册浏览器
    private void initWebView() {
        if(webView==null) {
            webView = (WebView) findViewById(R.id.mywebview);
            Log.d("webView", "initWebView:新建web了 ");
        }
        WebSettings webSettings = webView.getSettings();


        webView.setVerticalScrollBarEnabled(false);
        //窗口设置为手机大小
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        //设置缓存
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        //设置JS支持
        //webSettings.setJavaScriptEnabled(true);
        //设置支持缩放变焦
        webSettings.setBuiltInZoomControls(false);
        //设置是否支持缩放
        webSettings.setSupportZoom(false);
        //设置是否允许JS打开新窗口
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);


        // 修复一些机型webview无法点击
//        webView.requestFocus(View.FOCUS_DOWN);
//
//        webView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                // TODO Auto-generated method stub
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                    case MotionEvent.ACTION_UP:
//                        if (!v.hasFocus()) {
//                            v.requestFocus();
//                        }
//                        break;
//                }
//                return false;
//            }
//
//        });

        webView.setWebViewClient(new WebViewClient(){
            boolean if_load;
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                return false;
//            }



            //页面完成即加入历史记录
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                currenturl=view.copyBackForwardList().getCurrentItem().getUrl();
                currenttitle=view.copyBackForwardList().getCurrentItem().getTitle();
                if(if_load && !currenttitle.equals(" ")) {
                    nameList.add(currenttitle);
                    urlList.add(currenturl);
                    nameList=removeDuplicate(nameList);
                    urlList=removeDuplicate(urlList);
                    Log.d("array", urlList.toString());
                    if_load=false;
                }
            }

            //页面开始
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon){
                super.onPageStarted(view, url, favicon);

                if_load=true;
            }

        });
        LoadUrl(url);
    }
    //去重
    public static ArrayList removeDuplicate(ArrayList list){
        ArrayList tempList = new ArrayList(list.size());
        for(int i=0;i<list.size();i++){
            if(!tempList.contains(list.get(i)))
                tempList.add(list.get(i));
        }
        return tempList;
    }

    private void LoadUrl(String url) {
        Log.d("tag","运行了一次");
        webView.loadUrl(url);
    }
    //判断http
    public static boolean isHttpUrl(String urls) {
        boolean isurl = false;
        String regex = "(((https|http)?://)?([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }
    //判断www
    public static boolean isUrl(String urls) {
        boolean isurl = false;
        String regex = "(([a-z0-9]+[.])|(www.))"
                + "\\w+[.|\\/]([a-z0-9]{0,})?[[.]([a-z0-9]{0,})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z0-9]{0,}+|/?)";//设置正则表达式

        Pattern pat = Pattern.compile(regex.trim());//对比
        Matcher mat = pat.matcher(urls.trim());
        isurl = mat.matches();//判断是否匹配
        if (isurl) {
            isurl = true;
        }
        return isurl;
    }


    private Button btn_back;
    private Button btn_newwindow;
    private Button btn_forward;
    private Button btn_menu;
    private Button btn;
    private Button btn_more;
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        //这里用到了MenuInflater类，使用该类的inflate方法来读取xml文件并且建立菜单
        MenuInflater inflater = getMenuInflater();
        //设置menu界面为res/menu/menu.xml
        inflater.inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = this.getIntent().getExtras();
        if(bundle != null && bundle.containsKey("url")) {
            url = bundle.getString("url");
            nameList=bundle.getStringArrayList("title");
            urlList=bundle.getStringArrayList("list");
            Log.d("array5", nameList.toString());
            initWebView();
        }
        //按钮注册
        btn = (Button)findViewById(R.id.button);
        btn_back = (Button) findViewById(R.id.btn_back);
        btn_newwindow = (Button) findViewById(R.id.btn_exit);
        btn_forward = (Button) findViewById(R.id.btn_forward);
        btn_menu = (Button) findViewById(R.id.btn_menu);
        btn_more=(Button) findViewById(R.id.btn_more) ;


        btn_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //当前这个v就是 button2控件
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.history:
                                Intent intent = new Intent(MainActivity.this, historyActivity.class);
                                Bundle bundle=new Bundle();
                                //传递name参数为tinyphp
                                bundle.putStringArrayList("history",urlList);
                                bundle.putStringArrayList("title",nameList);
                                bundle.putString("currenturl",currenturl);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                break;
                            case R.id.flag:
                                Log.d("TAG2", "书签");
                                break;
                            case R.id.add_flag:
                                flagList.add(currenturl);
                                flagList=removeDuplicate(flagList);
                                Log.d("array3", flagList.toString());
                                break;
                        }
                        return false;
                    }
                });

                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.menu, popupMenu.getMenu());
                popupMenu.show();
            }
        });

        //按钮添加监听事件
        btn_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }

            }
        });
        btn_newwindow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(getApplicationContext(), "多窗口",
                        Toast.LENGTH_SHORT).show();
            }
        });
        btn_forward.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });
        btn_menu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                LoadUrl("https://m.baidu.com/");
            }
        });
        final EditText myEditText = (EditText) findViewById(R.id.edit_text);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //url判断并且改成想要的格式
                String str = myEditText.getText().toString();
                if (str.equals("")){
                    str="https://m.baidu.com/?cip=110.64.72.187&baiduid=8155C2BBA5E753A5E061F6569491FCEB?index=&ssid=0&bd_page_type=1&from=0&logid=10052674951125970529&pu=sz%401321_480&t_noscript=jump";
                }else {
                    if(isUrl(str)){
                        str="http://"+str;
                    }
                    if(!isHttpUrl(str)){
//                         str = "http://www.baidu.com/baidu?tn=02049043_69_pg&le=utf-8&word=" + myEditText.getText().toString();
                        str = "http://m.baidu.com/s?baiduid=8155C2BBA5E753A5E061F6569491FCEB&tn=baidulocal&le=utf-8&word=" + myEditText.getText().toString()+"&pu=sz%401321_480&t_noscript=jump";
                    }
                }
                url=str;
                initWebView();
            }
        });
    }

}
