package yang.com.servicedemo3;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.nfc.Tag;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "YOUNG-MainActivity";

    private Button btnBindService;
    private Button btnCallServiceM2;
    private Button btnRegisterCallback;
    private Button btnUnbindService;
    private MyService service;

//    标记服务是否绑定
    private boolean isBind = false;

    //    3、IBinder对象传递到ServiceConnection的回调接口onServiceConnected中，
//    4、在activity里就得到了binder
    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            service = ((MyService.MyBinder) iBinder).getService();
            service.m1();
            isBind = true;
            Log.d(TAG, "-------onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBind = false;
            service = null;
            Log.d(TAG, "-------onServiceDisconnected");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
//        bindService();
    }

    private void initViews(){
        btnBindService = f(R.id.btnBindService);
        btnBindService.setOnClickListener(this);
        btnCallServiceM2 = f(R.id.btnCallServiceM2);
        btnCallServiceM2.setOnClickListener(this);
        btnRegisterCallback = f(R.id.btnRegisterCallback);
        btnRegisterCallback.setOnClickListener(this);
        btnUnbindService = f(R.id.btnUnbindService);
        btnUnbindService.setOnClickListener(this);
    }

    private <T> T f(int resId){
        return (T)findViewById(resId);
    }

    private void bindService(){
        Intent bindIntent = new Intent(this,MyService.class);
        bindService(bindIntent,conn,BIND_AUTO_CREATE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnBindService:
                bindService();
                break;
            case R.id.btnCallServiceM2:
                if (service !=null){
                    service.m2("------------activity params");
                }else{
                    Log.d(TAG, "binder  null");
                }
                break;
            case R.id.btnRegisterCallback:
                //注册自定义回调
                if (null != service){
                    service.setOnChangeListener(new MyService.OnChangeListener() {
                        @Override
                        public void onChange(String text) {
                            Log.d(TAG, "from service msg: "+text);
                        }
                    });
                }
                break;
            case R.id.btnUnbindService:
                if (isBind == true){
                    unbindService(conn);
                    isBind = false;
                    service = null;
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        try{
            unbindService(conn);
            service =null;
        }catch (Exception e){
            Log.d(TAG, " exception");   //避免忘记解绑，但只能解绑一次，通用做法try，catch
        }
        super.onDestroy();
    }
}

