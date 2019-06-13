package com.example.joe.cityumobile.View.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.joe.cityumobile.DataModel.BmobModel.Post;
import com.example.joe.cityumobile.Manager.ApplyManager;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.KeyBoardUtils;

/**
 * 新聊天填写备注窗口
 */
public class EditNoteDialog extends Dialog {

    public EditNoteDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public static class Builder{

        private View mLayout;
        private EditNoteDialog mDialog;

        private Button sendBtn;
        private Button cancelBtn;
        private EditText noteInput;

        private Post post;

        public Builder(Context context){
            mDialog = new EditNoteDialog(context, R.style.Theme_AppCompat_Dialog);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mLayout = inflater.inflate(R.layout.dialog_edit_note,null,false);
            mDialog.addContentView(mLayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));

            findAndBindView();
        }

        private void findAndBindView(){
            noteInput = mLayout.findViewById(R.id.noteInput);
            sendBtn = mLayout.findViewById(R.id.confirmSend);
            cancelBtn = mLayout.findViewById(R.id.cancelSend);

            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ApplyManager.getInstance().sendApply(post.getPublisher(),post.getObjectId(),noteInput.getText().toString());
                    KeyBoardUtils.hideSoftKeyboard(mLayout.getWindowToken());
                    mDialog.dismiss();
                }
            });

            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDialog.dismiss();
                }
            });
        }

        public void setPost(Post postData){
            post = postData;
        }

        public EditNoteDialog create(){
            mDialog.setContentView(mLayout);
            mDialog.setCancelable(true);
            mDialog.setCanceledOnTouchOutside(false);
            return mDialog;
        }
    }

}
