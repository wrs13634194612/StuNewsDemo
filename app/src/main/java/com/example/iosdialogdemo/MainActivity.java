package com.example.iosdialogdemo;



        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.util.Log;

        import com.google.gson.Gson;
        import com.youth.banner.Banner;
        import com.youth.banner.BannerConfig;
        import com.youth.banner.Transformer;

        import java.io.IOException;
        import java.io.InputStream;
        import java.util.ArrayList;
        import java.util.List;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;
        import androidx.recyclerview.widget.LinearLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    //界面控件
    private Banner mbanner;
    //轮播图的数据
    private MyImageLoader myImageLoader;
    private ArrayList<Integer> photos;


    /*新闻列表*/
    private List<StuBean.SchReadyIngListBean> stuList;



    private RecyclerView rvLanguage2 ;
    private String jsonString;
    private LanguageRecycleAdapter2 languageRecycleAdapter2;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            if (message.what == 100) {
                jsonString = (String) message.obj;
                Log.e("INFO", "##### Sub: " + ": " + jsonString);
                Gson gson = new Gson();
                //解析字符串
                StuBean mStuBean = gson.fromJson(jsonString, StuBean.class);
                if (stuList != null) {
                    stuList.clear();
                }
                stuList.addAll(mStuBean.getSch_ready_ing_list());
                Log.e("INFO", "##### Sub: " + ": " + stuList);
                languageRecycleAdapter2.setData(stuList);
                languageRecycleAdapter2.notifyDataSetChanged();

            }
            return false;
        }
    });





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();
    }

    //界面初始化
    private void initView() {
        mbanner = (Banner) findViewById(R.id.banner);
        //设置轮播的样式
        mbanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);  //CIRCLE_INDICATOR
        //设置图片加载器
        mbanner.setImageLoader(myImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mbanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //设置轮播间隔时间
        mbanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mbanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mbanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mbanner.setImages(photos)
                //开始调用的方法，启动轮播图。
                .start();


        rvLanguage2 = findViewById(R.id.rvLanguage2);
        stuList = new ArrayList<>();

        rvLanguage2.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        languageRecycleAdapter2 = new LanguageRecycleAdapter2(MainActivity.this, stuList);
        rvLanguage2.setAdapter(languageRecycleAdapter2);

        loadJsonFromAssests();

    }

    //数据初始化
    private void initData() {
        myImageLoader = new MyImageLoader();
        //将轮播的图片放在photos 那里
        photos = new ArrayList<Integer>();
        photos.add(R.mipmap.ic_launcher);
        photos.add(R.mipmap.ic_launcher);
        photos.add(R.mipmap.ic_launcher);
        photos.add(R.mipmap.ic_launcher);
        photos.add(R.mipmap.ic_launcher);
        photos.add(R.mipmap.ic_launcher);

    }


    public void loadJsonFromAssests() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("stu.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        Log.e("TAG", "loadJsonFromAssests:" + json.toString());
        Message msg = new Message();
        msg.what = 100;
        msg.obj = json.toString();
        mHandler.sendMessage(msg);
    }



}