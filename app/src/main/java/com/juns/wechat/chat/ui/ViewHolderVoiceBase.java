package com.juns.wechat.chat.ui;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.same.city.love.R;
import com.juns.wechat.chat.bean.VoiceMsg;
import com.juns.wechat.chat.xmpp.util.MsgCode;
import com.juns.wechat.util.AudioManager;

import java.io.File;

/**
 * Created by xiajun on 2017/1/20.
 */

public class ViewHolderVoiceBase extends BaseMsgViewHolder {
    ImageView ivVoice;
    TextView tvVoicePadding;
    TextView tvVoiceLength;
    protected VoiceMsg voiceMsg;

    ViewHolderVoiceBase(View view) {
        super(view);
        ivVoice = (ImageView) view.findViewById(R.id.iv_voice);
        tvVoicePadding = (TextView) view.findViewById(R.id.tv_voice_padding);
        tvVoiceLength = (TextView) view.findViewById(R.id.tv_length);
    }

    @Override
    protected void updateView() {
        //先得到bean，在进行其他操作
        voiceMsg = (VoiceMsg) messageBean.getMsgObj();
        super.updateView();
        tvVoicePadding.setText(getPaddingByVoiceLength(voiceMsg.seconds));
        tvVoiceLength.setText(voiceMsg.seconds + "''");
    }

    private String getPaddingByVoiceLength(int voiceLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= voiceLength; i++) {
            if (i % 3 == 0) {
                sb.append("   ");
            }
        }
        return sb.toString();
    }

    @Override
    protected void onClickLayoutContainer() {
        super.onClickLayoutContainer();
        File file = new File(AudioManager.RECORD_PATH, voiceMsg.fileName);
        if (!file.exists()) { //由于文件都是唯一生成的，如果本地存在，说明是本地发出去，本地接收的
            MsgCode msgCode = new MsgCode();
            msgCode.decode(voiceMsg.encodeStr, file.getPath());
        }
        ChatMediaPlayer chatMediaPlayer = ChatMediaPlayer.getInstance();
        if (chatMediaPlayer.isRunning()) {
            chatMediaPlayer.stopVoice();
        }
        chatMediaPlayer.setVoiceView(ivVoice);
        chatMediaPlayer.setVoiceDir(!isLeftLayout());

        chatMediaPlayer.playVoice(AudioManager.RECORD_PATH + "/" + voiceMsg.fileName);

    }
}
