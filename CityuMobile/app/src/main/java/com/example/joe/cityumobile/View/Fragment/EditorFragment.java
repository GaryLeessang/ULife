package com.example.joe.cityumobile.View.Fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.SeekBar;

import com.example.joe.cityumobile.Core.Base.BaseFragment;
import com.example.joe.cityumobile.Core.Listener.MyTouchListener;
import com.example.joe.cityumobile.Core.Listener.PostPublishListener;
import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.DataModel.BmobModel.User;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.View.Activity.MainActivity;
import com.example.joe.cityumobile.View.Dialog.PostConfirmDialog;
import com.example.joe.cityumobile.databinding.FragmentEditorBinding;

import cn.bmob.v3.BmobUser;

public class EditorFragment extends BaseFragment implements MyTouchListener {

    private FragmentEditorBinding layout;
    private int currentSelectPostType = -1;

    StringBuilder stringBuilder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater,R.layout.fragment_editor,container,false);
        return layout.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        findAndBindView();
    }

    private void findAndBindView(){
        ((MainActivity)getActivity()).addTouchListener(this);

        stringBuilder = new StringBuilder();
        layout.postTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()<=20){
                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(s.length());
                    stringBuilder.append("/20");
                    layout.titleLimit.setText(stringBuilder.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layout.postEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() <= 200){
                    stringBuilder.delete(0,stringBuilder.length());
                    stringBuilder.append(s.length());
                    stringBuilder.append("/200");
                    layout.contentLimit.setText(stringBuilder.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        layout.sizeSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0:
                        layout.expressSize.setText("Small");
                        break;
                    case 1:
                        layout.expressSize.setText("Middle");
                        break;
                    case 2:
                        layout.expressSize.setText("Big");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        layout.prioritySlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress){
                    case 0:
                        layout.expressPriority.setText("Relax");
                        break;
                    case 1:
                        layout.expressPriority.setText("Normal");
                        break;
                    case 2:
                        layout.expressPriority.setText("Urgent");
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        layout.commisionSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                layout.expressCommision.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        layout.publishPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishNewPost();
            }
        });

        layout.postType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.selectHelp:
                        currentSelectPostType = 1;
                        layout.publishPost.setVisibility(View.VISIBLE);
                        layout.reminder.setVisibility(View.GONE);
                        break;
                    case R.id.selectProvide:
                        currentSelectPostType = 2;
                        layout.publishPost.setVisibility(View.VISIBLE);
                        layout.reminder.setVisibility(View.GONE);
                        break;
                    default:
                        currentSelectPostType = -1;
                        layout.publishPost.setVisibility(View.GONE);
                        layout.reminder.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        resetEditor();
    }

    /**
     * 发布新的帖子
     */
    private void publishNewPost(){
        Post post = new Post();
        post.setPublisher(BmobUser.getCurrentUser(User.class));
        if (layout.postTitle.getText().toString().trim().isEmpty()){
            switch (currentSelectPostType){
                case 1:
                    post.setTitle("Default title [help]");
                    break;
                case 2:
                    post.setTitle("Default title [service]");
                    break;
            }
        }else{
            post.setTitle(layout.postTitle.getText().toString());
        }
        post.setContent(layout.postEditText.getText().toString());
        post.setPostType(currentSelectPostType);

        post.setSize(layout.expressSize.getText().toString());
        post.setPriority(layout.expressPriority.getText().toString());
        post.setCommission(layout.commisionSlider.getProgress());

        PostConfirmDialog.Builder dialogBuilder = new PostConfirmDialog.Builder(getContext());
        dialogBuilder.setPostData(post);
        dialogBuilder.setListener(new PostPublishListener() {
            @Override
            public void done(boolean isSuccess) {
                if (isSuccess){
                    resetEditor();
                }
            }
        });
        dialogBuilder.create().show();
    }

    /**
     * 重置编辑器
     */
    public void resetEditor(){
        currentSelectPostType = -1;
        layout.reminder.setVisibility(View.VISIBLE);
        layout.publishPost.setVisibility(View.GONE);
        layout.postType.clearCheck();
        layout.postEditText.setText("");
        layout.postTitle.setText("");
        layout.sizeSlider.setProgress(0);
        layout.prioritySlider.setProgress(1);
        layout.commisionSlider.setProgress(10);
    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        if (isClickedView(layout.postTitle,event) || isClickedView(layout.postEditText,event)){
            return;
        }else{
            hideSoftKeyboard(layout.getRoot().getWindowToken());
        }
    }

    /**
     * 设置监听器
     */
    @Override
    public void setListener() {

    }
}
