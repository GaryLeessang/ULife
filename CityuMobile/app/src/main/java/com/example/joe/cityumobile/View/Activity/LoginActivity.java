package com.example.joe.cityumobile.View.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.joe.cityumobile.Core.Base.BaseActivity;
import com.example.joe.cityumobile.DataAccess.LocalDataAccess;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.databinding.ActivityLoginBinding;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

//登录界面
public class LoginActivity extends BaseActivity {

    ActivityLoginBinding layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        layout = DataBindingUtil.setContentView(this,R.layout.activity_login);

        setImmersiveStatusBar();
        MyApplication.instance.checkAllPermission();
        setListener();
    }

    /**
     * 设置监听器
     */
    @Override
    public void setListener() {
        layout.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Login();
                if (BmobUser.isLogin()){
                    LocalDataAccess.getInstance().initial(getApplicationContext(),BmobUser.getCurrentUser(User.class).getUsername());

                    MyApplication.instance.isUserLogin = true;
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    MyUserManager.setCurrentUser(BmobUser.getCurrentUser(User.class));
                    goToMainActivity();
                }else{
                    selectLoginUser();
                }
            }
        });

        layout.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.currentActivity(),RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    //跳转到主界面
    void goToMainActivity(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }


    //用户登录(暂时禁用)
    void Login(){
        User user = new User();
        user.setUsername(layout.username.getText().toString());
        user.setPassword(layout.password.getText().toString());
        user.login(new SaveListener<User>() {
            public void done(User user,BmobException e){
                if (e == null){
                    //MyApplication.dataAccess.initial(getApplicationContext(),user.getUsername());
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    MyUserManager.setCurrentUser(user);
                    goToMainActivity();

                }else{
                    Log.e("My",e.toString());
                    MyUserManager.currentUser = null;
                    Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    //--------------------测试专用--------------------

    void selectLoginUser(){
        final CharSequence[] items={"Joe","Kami", "Gary"};
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Select developer account");
        final User user = new User();

        builder.setItems(items,new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int i) {
                if (items[i].equals("Joe")){
                    user.setUsername("55366688");
                    user.setPassword("123abc");
                }else if(items[i].equals("Kami")){
                    user.setUsername("55469991");
                    user.setPassword("123456");
                }else if (items[i].equals("Gary")){
                    user.setUsername("55419023");
                    user.setPassword("123456789");
                }

                try{
                    user.login(new SaveListener<User>() {
                        public void done(User user,BmobException e){
                            if (e == null){
                                //MyApplication.dataAccess.initial(getApplicationContext(),user.getUsername());

                                //初始化数据库
                                LocalDataAccess.getInstance().initial(getApplicationContext(),user.getUsername());

                                MyApplication.instance.isUserLogin = true;
                                Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                                MyUserManager.setCurrentUser(user);
                                goToMainActivity();

                            }else{
                                Log.e("My","login error:"+e.toString());
                                MyApplication.instance.isUserLogin = false;
                                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
                                MyUserManager.setCurrentUser(null);
                            }
                        }
                    });
                }catch (Exception e){
                    Log.e("My","login:"+e.toString());
                }

            }
        });

        builder.show();
    }

    //----------------------------------------------



    //注册新用户
    void SignUp(){

        User newUser = new User();
        newUser.setUsername(layout.username.getText().toString());
        newUser.setPassword(layout.password.getText().toString());

        newUser.signUp(new SaveListener<User>() {

            @Override
            public void done(User user, BmobException e) {
                if (e == null){
                    Toast.makeText(MyApplication.currentActivity(), "注册成功", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MyApplication.currentActivity(), "注册失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}
