package com.example.joe.cityumobile.View.Adapter.ViewHolder;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.joe.cityumobile.Core.Base.BaseViewHolder;
import com.example.joe.cityumobile.DataModel.DaoModel.Express;
import com.example.joe.cityumobile.R;
import com.example.joe.cityumobile.Utils.TimeUtils;
import com.example.joe.cityumobile.Utils.Utils;

import java.util.Date;

public class ExpressRecordHolder extends BaseViewHolder {

    private Express data;

    private TextView createDate;
    private TextView content;
    private TextView state;

    public ExpressRecordHolder(Context context, ViewGroup root) {
        super(context, root, R.layout.item_post_history);
    }

    /**
     * 找到并绑定所有UI组件
     */
    @Override
    public void findViews() {
        createDate = itemView.findViewById(R.id.createDate);
        content = itemView.findViewById(R.id.content);
        state = itemView.findViewById(R.id.postState);
    }

    /**
     * 设置数据元
     *
     * @param o
     */
    @Override
    public void setData(Object o) {
        data = (Express) o;
    }

    /**
     * 填充UI组件
     */
    @Override
    public void fillComponents() {
        createDate.setText(TimeUtils.dateToString(data.getCreateTime(),TimeUtils.FORMAT_DATE_TIME_SECOND));
        content.setText(data.getContent());
        if (data.getExpireTime().before(new Date())){
            state.setText("Expire");
            Utils.setViewTint(state,"#7E7E7E");
        }else{
            state.setText("Active");
            Utils.setViewTint(state,"#03A9F4");
        }
    }
}
