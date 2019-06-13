package com.example.joe.cityumobile.View.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.joe.cityumobile.Manager.MyApplication;
import com.example.joe.cityumobile.Manager.MyUserManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.Utils;
import com.example.joe.cityumobile.View.Activity.MyPostActivity;
import com.example.joe.cityumobile.View.Activity.SettingActivity;
import com.example.joe.cityumobile.View.Dialog.AvatarSelectorDialog;
import com.example.joe.cityumobile.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {

    private FragmentUserBinding layout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater,R.layout.fragment_user,container,false);
        return layout.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findAndBindView();
        updateAvatar();
    }

    private void findAndBindView(){

        layout.changeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelectDialog();
            }
        });
        layout.myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.currentActivity(), MyPostActivity.class);
                startActivity(intent);
            }
        });

        layout.settingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyApplication.currentActivity(), SettingActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateAvatar(){
        layout.avatar.setImageResource(Utils.getAvatarResId(MyUserManager.currentUser.getAvatar()));
    }

    private void loadNetImage(){
        //ImageLoaderFactory.getLoader().loadImage(netImg,"https://www.baidu.com/img/bd_logo1.png",R.drawable.bg,null);
    }

    private void openImageSelectDialog(){
        AvatarSelectorDialog dialog = new AvatarSelectorDialog.Builder(getContext()).create();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateAvatar();
            }
        });
        dialog.show();
    }

}
