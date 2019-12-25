package yang.com.servicedemo3;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

//  新建一个继承Service类MyService，在MyService里实例化一个继承Binder的内部类MyBinder，并在onBind回调方法里面返回这个MyBinder对象
public class MyService extends Service {
    private static final String TAG = "YOUNG-MyService";

//  OnChangeListener接口的引用
    private OnChangeListener onChangeListener = null;

//  继承Binder的内部类
    public class  MyBinder extends Binder{
        MyService getService(){
            return MyService.this;
        }
    }

//    1、Activity调用bindservice绑定服务
//    2、绑定成功，onBind()会被调用, 返回IBinder对象.
//    (绑定服务，必须实现onBind()回调方法，该方法返回的IBinder对象定义了客户端用来与服务进行交互的编程接口)
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        Log.d(TAG, "onBind");
        return new MyBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    public void m1(){
        Log.d(TAG, "bind successful");
    }

    // Activity通过Binder对象来调用Service的方法将消息传给Service
    public void m2(String text){
        Log.d(TAG, "from activity msg: "+text);

        m3("------------service  params");
    }

    //触发回调
    public void m3(String text){
        if (null != onChangeListener){
            onChangeListener.onChange(text);
        }
    }

    // 提供一个注册回调的方法
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.onChangeListener = onChangeListener;
    }

    //自定义一个回调接口
    public interface OnChangeListener{
        void onChange(String text);
    }
}
