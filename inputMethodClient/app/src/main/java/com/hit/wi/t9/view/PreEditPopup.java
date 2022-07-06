package com.hit.wi.t9.view;

import android.content.Context;
import android.graphics.Paint;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.hit.wi.jni.Kernel;
import com.hit.wi.util.DisplayUtil;
import com.hit.wi.util.ViewsUtil;
import com.hit.wi.t9.R;
import com.hit.wi.t9.SoftKeyboard;
import com.hit.wi.t9.values.Global;

/**
 * Created by purebluesong on 2016/4/7.
 */
public class PreEditPopup {
    private final float TEXTSIZE_RATE = (float) 2.3;
    private final int TEXTSIZE_RATE_BY_WIDTH = 6;
    private final float TEXTSIZE_RATE_BY_HEIGHT = (float) 0.33;

    private PopupWindow container;
    private EditText editText;
    private SoftKeyboard softKeyboard;

    private Paint toolPaint;
    private int leftMargin = 0;
    private int selectStart;
    private int selectStop;
    private CharSequence mText;

    public void setSoftKeyboard(SoftKeyboard softKeyboard) {
        this.softKeyboard = softKeyboard;
    }

    public void create(Context context) {
        toolPaint = new Paint();
        editText = new EditText(context);
        editText.setPadding(0, 0, 0, 0);
        editText.setGravity(Gravity.LEFT & Gravity.CENTER_VERTICAL);
        editText.setVisibility(View.VISIBLE);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        editText.setBackgroundResource(R.drawable.blank);
        ViewsUtil.setBackgroundWithGradientDrawable(editText, softKeyboard.skinInfoManager.skinData.backcolor_editText);
        //editText.setBackgroundColor(softKeyboard.skinInfoManager.skinData.backcolor_editText);
        editText.getBackground().setAlpha(Global.getCurrentAlpha());
        editText.setOnClickListener(editOnClickListener);
        if (Global.shadowSwitch)
            editText.setShadowLayer(Global.shadowRadius, 0, 0, softKeyboard.skinInfoManager.skinData.shadow);

        container = new PopupWindow(editText, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        container.setFocusable(false);
        container.setTouchable(true);
        container.setClippingEnabled(false);
        container.setBackgroundDrawable(null);
    }

    public void upadteSize(int width, int height, int leftMargin) {
        container.setHeight(height);
        container.setWidth(width);
        this.leftMargin = leftMargin;
    }

    public void updateSkin() {
        editText.setBackgroundResource(R.drawable.blank);
        editText.setBackgroundColor(softKeyboard.skinInfoManager.skinData.backcolor_preEdit);
        editText.setTextColor(softKeyboard.skinInfoManager.skinData.textcolor_preEdit);
        editText.getBackground().setAlpha(Global.getCurrentAlpha());
        editText.setShadowLayer(Global.shadowRadius, 0, 0, softKeyboard.skinInfoManager.skinData.shadow);
    }

    public boolean isShown() {
        return container.isShowing() | editText.isShown();
    }

    public void refreshState() {
        if (editText == null) return;
        String pinyin = Kernel.getWordsShowPinyin();
        if (pinyin.length() > 0) {
            showText(pinyin);
            editText.setSelection(Math.min(selectStart, pinyin.length()), Math.min(selectStop, pinyin.length()));
        } else {
            dismiss();
        }
    }
    public void setBackgroundAlpha(int alpha){

        editText.getBackground().setAlpha(alpha);
    }

    public void showText(CharSequence text) {
        editText.setText(text);
        mText = text;
        float length = toolPaint.measureText((String) text);
        editText.setTextSize(DisplayUtil.px2sp(softKeyboard,
                Math.min(container.getHeight() * TEXTSIZE_RATE_BY_HEIGHT, TEXTSIZE_RATE_BY_WIDTH * container.getWidth() / length) * TEXTSIZE_RATE
        ));
        show();
    }

    public void show() {
        if (!isShown()) {
            editText.setPadding(leftMargin, 0, leftMargin, 0);
            container.showAsDropDown(softKeyboard.keyboardLayout, 0, -container.getHeight() - softKeyboard.keyboardParams.height);
        }
    }

    public void dismiss() {
        mText = "";
        container.dismiss();
    }

    public void setCursor(int cursor) {
        this.selectStart = Math.max(cursor, 0);
        this.selectStop = Math.max(cursor, 0);
    }

    public void setCursor(int start, int stop) {
        this.selectStart = Math.max(start, 0);
        this.selectStop = Math.max(stop, 0);
    }

    public int getTextLength() {
        return mText.length();
    }

//    public void setButtonAlpha(float alpha) {
//        editText.setAlpha(alpha);
//    }

    /**
     * for sync the cursor of inputConnection and edit
     */
    private View.OnClickListener editOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int baseCursor = softKeyboard.pinyinProc.mCandidateStart;
            InputConnection ic = softKeyboard.getCurrentInputConnection();
            ic.setSelection(baseCursor + editText.getSelectionStart(), baseCursor + editText.getSelectionEnd());
        }
    };

}
