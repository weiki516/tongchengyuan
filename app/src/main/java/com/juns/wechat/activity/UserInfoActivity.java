package com.juns.wechat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.bean.FriendBean;
import com.juns.wechat.bean.UserBean;
import com.juns.wechat.chat.ui.ChatActivity;
import com.juns.wechat.database.dao.FriendDao;
import com.juns.wechat.database.dao.UserDao;
import com.juns.wechat.exception.UserNotFoundException;
import com.juns.wechat.helper.CommonViewHelper;
import com.juns.wechat.manager.AccountManager;
import com.juns.wechat.net.request.HttpActionImpl;
import com.style.net.core.NetDataBeanCallback;
import com.style.base.BaseToolbarActivity;
import com.style.constant.Skip;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 用户资料
 */

public class UserInfoActivity extends BaseToolbarActivity implements OnClickListener {
    @Bind(R.id.ivAvatar)
    ImageView ivAvatar;
    @Bind(R.id.tvNickName)
    TextView tvNickName;
    @Bind(R.id.ivSex)
    ImageView ivSex;
    @Bind(R.id.tvUserName)
    TextView tvUserName;
    @Bind(R.id.btnSendMsg)
    Button btnSendMsg;

    private int userId;

    private UserBean curUser = AccountManager.getInstance().getUser();
    private FriendBean friendBean;
    private UserBean userBean;
    private String subType = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_user_info;
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.avatar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_text_only:
                //goonNext();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void initData() {
        setToolbarTitle("详细资料");
        userId = getIntent().getIntExtra(Skip.KEY_USER_ID, 0);
        if (userId == curUser.getUserId()) {  //查看自己的信息
            userBean = curUser;
            setData();
            return;
        }

        friendBean = FriendDao.getInstance().findByOwnerAndContactName(curUser.getUserId(), userId);
        if (friendBean != null) {  //不是好友关系
            subType = friendBean.getSubType();
        }
        userBean = UserDao.getInstance().findByUserId(userId);
        if (userBean != null) {
            setData();
            return;
        }
        HttpActionImpl.getInstance().queryUserData(TAG, userId, new NetDataBeanCallback<UserBean>(UserBean.class) {
            @Override
            protected void onCodeSuccess(UserBean data) {
                if (data != null) {
                    userBean = data;
                    UserDao.getInstance().replace(data);
                    setData();
                }
            }

            @Override
            protected void onCodeFailure(String msg) {
                showToast("获取用户信息失败");
            }
        });
    }

    private void setData() {
        CommonViewHelper.setUserViewInfo(userBean, ivAvatar, tvNickName, ivSex, tvUserName, true);

        if (userId == curUser.getUserId()) {
            btnSendMsg.setText("个人信息");
        } else {
            if (subType == null) {
                btnSendMsg.setText("加为好友");
            } else {
                btnSendMsg.setText("发送消息");
            }
        }
    }

    @OnClick(R.id.btnSendMsg)
    public void onClick(View v) {
        if (userId == curUser.getUserId()) {
            startActivity(new Intent(this, PersonInfoShowActivity.class));
            return;
        }
        if (subType == null) {
            Intent intent = new Intent(UserInfoActivity.this, AddFriendFinalActivity.class);
            intent.putExtra(Skip.KEY_USER_NAME, userBean.getUserName());
            startActivity(intent);
        } else {
            Intent intent = new Intent(UserInfoActivity.this, ChatActivity.class);
            intent.putExtra(Skip.KEY_USER_ID, userId);
            startActivity(intent);
        }
    }

}
