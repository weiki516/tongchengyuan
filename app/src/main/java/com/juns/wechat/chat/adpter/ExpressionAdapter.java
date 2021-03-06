package com.juns.wechat.chat.adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.same.city.love.R;
import com.style.base.BaseRecyclerViewAdapter;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ExpressionAdapter extends BaseRecyclerViewAdapter<String> {

    public ExpressionAdapter(Context mContext, List<String> list) {
        super(mContext, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.row_expression, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final int pos = position;
        final ViewHolder holder = (ViewHolder) viewHolder;
        String filename = getData(position);
        int resId = mContext.getResources().getIdentifier(filename, "drawable", mContext.getPackageName());

        holder.ivExpression.setImageResource(resId);
        super.setOnItemClickListener(viewHolder, pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_expression)
        ImageView ivExpression;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);

        }
    }
}
