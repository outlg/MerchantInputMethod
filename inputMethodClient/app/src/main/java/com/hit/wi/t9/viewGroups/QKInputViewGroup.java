package com.hit.wi.t9.viewGroups;

import android.content.Context;
import android.graphics.Typeface;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputConnection;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hit.wi.jni.Kernel;
import com.hit.wi.util.DisplayUtil;
import com.hit.wi.util.InputMode;
import com.hit.wi.util.ViewsUtil;
import com.hit.wi.util.StringUtil;
import com.hit.wi.t9.R;
import com.hit.wi.t9.SoftKeyboard;
import com.hit.wi.t9.datastruct.InputAction;
import com.hit.wi.t9.functions.PredictManager;
import com.hit.wi.t9.values.Global;
import com.hit.wi.t9.view.KeyContainerLinearLayout;
import com.hit.wi.t9.view.QuickButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by purebluesong on 2016/2/26.
 */
public class QKInputViewGroup extends NonScrollViewGroup {


    private final float TEXTSIZE_RATE_DELETE = (float) 0.8;
    private final float TEXTSIZE_RATE_MAIN = (float) 1.0;
    private final float TEXTSIZE_RATE_PRE = (float) 0.4;
    private final int KEY_A_INDEX = 10;
    private final int KEY_E_INDEX = 2;
    private final int KEY_I_INDEX = 7;
    private final int KEY_O_INDEX = 8;
    private final int KEY_U_INDEX = 6;
    private final int KEY_H_INDEX = 15;
    private final int KEY_Z_INDEX = 19;
    private final int KEY_M_INDEX = 25;

    private final int SMILE_FUNC_NUM = 4;
    /**
     * 英文显示时的动画资源
     */
    int[] enShow = {
            R.anim.en_key_q_switch_in,
            R.anim.en_key_w_switch_in,
            R.anim.en_key_e_switch_in,
            R.anim.en_key_r_switch_in,
            R.anim.en_key_t_switch_in,
            R.anim.en_key_y_switch_in,
            R.anim.en_key_u_switch_in,
            R.anim.en_key_i_switch_in,
            R.anim.en_key_o_switch_in,
            R.anim.en_key_p_switch_in,
            R.anim.en_key_a_switch_in,
            R.anim.en_key_s_switch_in,
            R.anim.en_key_d_switch_in,
            R.anim.en_key_f_switch_in,
            R.anim.en_key_g_switch_in,
            R.anim.en_key_h_switch_in,
            R.anim.en_key_j_switch_in,
            R.anim.en_key_k_switch_in,
            R.anim.en_key_l_switch_in,
            R.anim.en_key_z_switch_in,
            R.anim.en_key_x_switch_in,
            R.anim.en_key_c_switch_in,
            R.anim.en_key_v_switch_in,
            R.anim.en_key_b_switch_in,
            R.anim.en_key_n_switch_in,
            R.anim.en_key_m_switch_in,
    };

    /**
     * 英文隐藏时的动画资源
     */
    int[] enHide = {
            R.anim.en_key_q_switch_out,
            R.anim.en_key_w_switch_out,
            R.anim.en_key_e_switch_out,
            R.anim.en_key_r_switch_out,
            R.anim.en_key_t_switch_out,
            R.anim.en_key_y_switch_out,
            R.anim.en_key_u_switch_out,
            R.anim.en_key_i_switch_out,
            R.anim.en_key_o_switch_out,
            R.anim.en_key_p_switch_out,
            R.anim.en_key_a_switch_out,
            R.anim.en_key_s_switch_out,
            R.anim.en_key_d_switch_out,
            R.anim.en_key_f_switch_out,
            R.anim.en_key_g_switch_out,
            R.anim.en_key_h_switch_out,
            R.anim.en_key_j_switch_out,
            R.anim.en_key_k_switch_out,
            R.anim.en_key_l_switch_out,
            R.anim.en_key_z_switch_out,
            R.anim.en_key_x_switch_out,
            R.anim.en_key_c_switch_out,
            R.anim.en_key_v_switch_out,
            R.anim.en_key_b_switch_out,
            R.anim.en_key_n_switch_out,
            R.anim.en_key_m_switch_out,
    };

    private String[] enKeyText;
    private int SMILE_KEYS_NUM = 4;
    private int[] linear_keys_num = {10, 9, 7};
    private String[] shiftText;
    private String smileText;
    private boolean mShiftOn = false;

    private LinearLayout[] linears = new LinearLayout[3];
    private LinearLayout.LayoutParams[] linearsParams = new LinearLayout.LayoutParams[3];
    private PredictManager predictManager = new PredictManager();
    private QuickButton shiftButton;
    private QuickButton smileButton;
    private QuickButton deleteButton;
    public List<LinearLayout> buttonList;
    private String[] mAllSmileText;


    @Override
    public void create(Context context) {
        super.create(context);
        viewGroupWrapper.setOrientation(LinearLayout.VERTICAL);
        enKeyText = res.getStringArray(R.array.EN_KEY_TEXT);
        shiftText = res.getStringArray(R.array.EN_SHIFT_AND_SYM_KEY_TEXT);
        smileText = res.getString(R.string.smile_key_text);
        mAllSmileText = res.getStringArray(R.array.ALL_SMILE_VIEW_TEXT);
        predictManager.init(context);
        buttonList = new ArrayList<>();

        int count = 0;
        for (int i = 0; i < 3; i++) {
            linears[i] = i != 2 ? new KeyContainerLinearLayout(context) : new LinearLayout(context);
            linearsParams[i] = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);
            linears[i].setOrientation(LinearLayout.HORIZONTAL);
            linears[i].setGravity(Gravity.CENTER);
            for (int j = 0; j < linear_keys_num[i]; j++) {
                LinearLayout button = addButton(enKeyText[count], predictManager.qkPredict[count]);
                button.setOnTouchListener(qkInputOnTouchListener);
                buttonList.add(button);
                linears[i].addView(button);
                count++;
            }
            viewGroupWrapper.addView(linears[i], linearsParams[i]);
        }
        linears[0].setOnTouchListener(cursorLocateOnTouchListener);

        addShiftButton();
        addSmileButton();
        addDeleteButton();
    }

    private void addShiftButton() {
        shiftButton = super.addButton(shiftText[0],
                skinInfoManager.skinData.textcolor_shift,
                skinInfoManager.skinData.backcolor_shift);
        shiftButton.setOnTouchListener(mShiftKeyOnTouchListener);
        shiftButton.itsLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        linears[2].addView(shiftButton, 0);
    }

    private void addSmileButton() {
        smileButton = super.addButton(smileText,
                skinInfoManager.skinData.textcolor_shift,
                skinInfoManager.skinData.backcolor_shift);
        smileButton.setOnTouchListener(mSmileKeyOnTouchListener);
        smileButton.itsLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        linears[2].addView(smileButton, 1);
    }

    private void addDeleteButton() {
        deleteButton = super.addButton(res.getString(R.string.delete_text),
                skinInfoManager.skinData.textcolor_delete,
                skinInfoManager.skinData.backcolor_delete
        );
        deleteButton.setOnTouchListener(mDeleteOnTouchListener);
        deleteButton.itsLayoutParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        linears[2].addView(deleteButton);
    }

    public void reloadPredictText(int keyboard) {
        predictManager.refresh(context);
        switch (keyboard) {
            case Global.KEYBOARD_QK:
                int i = 0;
                for (LinearLayout button : buttonList) {
                    ((TextView) button.findViewById(R.id.predict_text))
                            .setText(InputMode.halfToFull(predictManager.qkPredict[i++]));
                }
                break;
            default:
                for (LinearLayout button : buttonList) {
                    ((TextView) button.findViewById(R.id.predict_text)).setText("");
                }
                break;
        }
    }

    private void toolfunc_refreshQKKeyboardPredict(String text, LinearLayout button, String predictText) {
        text = text.length() < 1 ? Global.currentKeyboard == Global.KEYBOARD_EN ? "" : predictText : text;
        ((TextView) button.findViewById(R.id.predict_text)).setText(InputMode.halfToFull(text));
    }

    public void refreshQKKeyboardPredict() {
        toolfunc_refreshQKKeyboardPredict(Kernel.getPredictA(),
                buttonList.get(KEY_A_INDEX), predictManager.qkPredict[KEY_A_INDEX]);

        toolfunc_refreshQKKeyboardPredict(Kernel.getPredictE(),
                buttonList.get(KEY_E_INDEX), predictManager.qkPredict[KEY_E_INDEX]);

        toolfunc_refreshQKKeyboardPredict(Kernel.getPredictI(),
                buttonList.get(KEY_I_INDEX), predictManager.qkPredict[KEY_I_INDEX]);

        toolfunc_refreshQKKeyboardPredict(Kernel.getPredictO(),
                buttonList.get(KEY_O_INDEX), predictManager.qkPredict[KEY_O_INDEX]);

        toolfunc_refreshQKKeyboardPredict(Kernel.getPredictU(),
                buttonList.get(KEY_U_INDEX), predictManager.qkPredict[KEY_U_INDEX]);

        toolfunc_refreshQKKeyboardPredict(Kernel.getPredictH(),
                buttonList.get(KEY_H_INDEX), predictManager.qkPredict[KEY_H_INDEX]);
    }

    public void setSize(int width, int height, int horGap) {
        int keyWidth = (width - 2 * horGap) / linear_keys_num[0];
        int keyHeight = height / 3;
        for (int j = 0; j < 3; ++j) {
            linearsParams[j].height = keyHeight;
        }
        int padding = horGap / 2;
        for (LinearLayout button : buttonList) {
            button.getLayoutParams().width = keyWidth;
            button.setPadding(padding, padding, padding, padding);
            ((TextView) button.findViewById(R.id.main_text)).getPaint()
                    .setTextSize(DisplayUtil.px2sp(context, Math.min(keyWidth, keyHeight * 7 / 9) * TEXTSIZE_RATE_MAIN));
            ((TextView) button.findViewById(R.id.predict_text)).getPaint()
                    .setTextSize(DisplayUtil.px2sp(context, Math.min(keyWidth, keyHeight * 7 / 9) * TEXTSIZE_RATE_PRE));
            ((LinearLayout) button.getParent()).updateViewLayout(button, button.getLayoutParams());
        }
        int widthKeyWidth = keyWidth * 3 / 2 - padding;

        shiftButton.itsLayoutParams.width = widthKeyWidth;
        ((LinearLayout.LayoutParams) shiftButton.itsLayoutParams).setMargins(padding, padding, padding, padding);
        shiftButton.getPaint().setTextSize(DisplayUtil.px2sp(context, Math.min(keyWidth * 3 / 2, keyHeight) * TEXTSIZE_RATE_DELETE));
        linears[2].updateViewLayout(shiftButton, shiftButton.itsLayoutParams);

        smileButton.itsLayoutParams.width = widthKeyWidth;
        ((LinearLayout.LayoutParams) smileButton.itsLayoutParams).setMargins(padding, padding, padding, padding);
        smileButton.getPaint().setTextSize(DisplayUtil.px2sp(context, Math.min(keyWidth * 3 / 2, keyHeight) * TEXTSIZE_RATE_DELETE));
        linears[2].updateViewLayout(smileButton, smileButton.itsLayoutParams);

        deleteButton.itsLayoutParams.width = widthKeyWidth;
        ((LinearLayout.LayoutParams) deleteButton.itsLayoutParams).setMargins(padding, padding, padding, padding);
        deleteButton.getPaint().setTextSize(DisplayUtil.px2sp(context, Math.min(keyWidth * 3 / 2, keyHeight) * TEXTSIZE_RATE_DELETE));
        linears[2].updateViewLayout(deleteButton, deleteButton.itsLayoutParams);
    }

    public void setVisibility(int visibility) {
        for (LinearLayout button : buttonList) {
            button.clearAnimation();
            button.setVisibility(visibility);
        }
        deleteButton.clearAnimation();
        deleteButton.setVisibility(visibility);
    }

    public boolean isShown() {
        boolean shown = false;
        for (LinearLayout layout : buttonList) {
            shown |= layout.isShown();
        }
        return shown;
    }

    private int lastState = -1;

    public void refreshState() {
        if (lastState == Global.currentKeyboard) return;
        if (Global.currentKeyboard == Global.KEYBOARD_QK) {
            if (!isShown()) setVisibility(View.VISIBLE);
            smileButton.clearAnimation();
            smileButton.setVisibility(View.VISIBLE);
            shiftButton.clearAnimation();//不然后面的View。Gone会不起作用，因为前面用过动画了，所以得清空一下
            shiftButton.setVisibility(View.GONE);
            for (LinearLayout button : buttonList) {
                button.setOnTouchListener(qkInputOnTouchListener);
            }
        } else if (Global.currentKeyboard == Global.KEYBOARD_EN) {
            if (!isShown()) setVisibility(View.VISIBLE);
            shiftButton.clearAnimation();
            shiftButton.setVisibility(View.VISIBLE);
            smileButton.clearAnimation();//不然后面的View。Gone会不起作用，因为前面用过动画了，所以得清空一下
            smileButton.setVisibility(View.GONE);
            for (LinearLayout button : buttonList) {
                button.setOnTouchListener(enInputOnTouchListener);
            }
        } else {
            setVisibility(View.GONE);
        }
        lastState = Global.currentKeyboard;
    }

    private void tool_updateSkin(TextView v, int textColor, int backgroundColor) {
        v.setTextColor(textColor);
        ViewsUtil.setBackgroundWithGradientDrawable(v, backgroundColor);

        v.getBackground().setAlpha(Global.getCurrentAlpha());
        v.setShadowLayer(Global.shadowRadius, 0, 0, skinInfoManager.skinData.shadow);
    }

    public void updateSkin() {
        for (LinearLayout button : buttonList) {
            TextView main_text = ((TextView) button.findViewById(R.id.main_text));
            TextView predict_text = ((TextView) button.findViewById(R.id.predict_text));

            main_text.setTextColor(skinInfoManager.skinData.textcolor_26keys);
            main_text.setShadowLayer(Global.shadowRadius, 0, 0, skinInfoManager.skinData.shadow);
            ViewsUtil.setBackgroundWithGradientDrawable(main_text, skinInfoManager.skinData.backcolor_26keys);
            //main_text.setBackgroundColor(skinInfoManager.skinData.backcolor_26keys);
            main_text.getBackground().setAlpha(Global.getCurrentAlpha());
            predict_text.setTextColor(skinInfoManager.skinData.textcolor_26keys);
            predict_text.setShadowLayer(Global.shadowRadius, 0, 0, skinInfoManager.skinData.shadow);
            //predict_text.setBackgroundColor(skinInfoManager.skinData.backcolor_26keys);
            ViewsUtil.setBackgroundWithGradientDrawable(predict_text, skinInfoManager.skinData.backcolor_26keys);
            predict_text.getBackground().setAlpha(Global.getCurrentAlpha()); //设置透明度
        }

        tool_updateSkin(shiftButton,
                skinInfoManager.skinData.textcolor_shift,
                skinInfoManager.skinData.backcolor_shift
        );
        tool_updateSkin(smileButton,
                skinInfoManager.skinData.textcolor_shift,
                skinInfoManager.skinData.backcolor_shift
        );
        tool_updateSkin(deleteButton,
                skinInfoManager.skinData.textcolor_26keys,
                skinInfoManager.skinData.backcolor_delete
        );
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean show) {
        shiftButton.clearAnimation();
        smileButton.clearAnimation();
        deleteButton.clearAnimation();

        shiftButton.setVisibility(View.GONE);
        smileButton.setVisibility(View.GONE);
        deleteButton.setVisibility(View.GONE);
        int i = 0;
        for (LinearLayout button : buttonList) {
            if (show) {
                Animation anim = AnimationUtils.loadAnimation(context, enHide[i++]);
                anim.setAnimationListener(getMyAnimationListener(button));
                button.startAnimation(anim);
            } else {
                button.setVisibility(View.GONE);
            }
        }
    }

    public void show(boolean show) {
        int i = 0;
//        clearAnimation();
        for (LinearLayout button : buttonList) {
            button.setVisibility(View.VISIBLE);
            if (show) {
                button.startAnimation(AnimationUtils.loadAnimation(context, enShow[i++]));
            }
        }
        shiftButton.clearAnimation();
        smileButton.clearAnimation();
        if (Global.currentKeyboard == Global.KEYBOARD_EN) {
            shiftButton.setVisibility(View.VISIBLE);
            smileButton.setVisibility(View.GONE);
            if (show)
                shiftButton.startAnimation(AnimationUtils.loadAnimation(context, enShow[KEY_Z_INDEX]));
        } else if (Global.currentKeyboard == Global.KEYBOARD_QK) {
            smileButton.setVisibility(View.VISIBLE);
            shiftButton.setVisibility(View.GONE);
            if (show)
                smileButton.startAnimation(AnimationUtils.loadAnimation(context, enShow[KEY_Z_INDEX]));
        }
        deleteButton.clearAnimation();
        deleteButton.setVisibility(View.VISIBLE);
        if (show)
            deleteButton.startAnimation(AnimationUtils.loadAnimation(context, enShow[KEY_M_INDEX]));
    }

    public void show() {
        show(true);
    }

    public void startAnimation(Animation anim) {
        for (LinearLayout button : buttonList) {
            button.startAnimation(anim);
        }
        if (Global.currentKeyboard == Global.KEYBOARD_EN) {
            shiftButton.startAnimation(anim);
        } else if (Global.currentKeyboard == Global.KEYBOARD_QK) {
            smileButton.startAnimation(anim);
        }
        deleteButton.startAnimation(anim);
    }

    public LinearLayout addButton(String text, String predict_text) {
        LinearLayout button = (LinearLayout) getEnKeyInflaterView(text, predict_text);
        button.setVisibility(View.GONE);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        button.setLayoutParams(params);

        return button;
    }

    private View getEnKeyInflaterView(String text, String predict) {
        LayoutInflater enKeyInflater = LayoutInflater.from(softKeyboard);
        View button = enKeyInflater.inflate(R.layout.en_key, null);
        TextView main_text = (TextView) button.findViewById(R.id.main_text);
        TextView predict_text = (TextView) button.findViewById(R.id.predict_text);
        main_text.setText(InputMode.halfToFull(text));
        main_text.setTextColor(skinInfoManager.skinData.textcolor_26keys);
        predict_text.setText(InputMode.halfToFull(predict));
        predict_text.setTextColor(skinInfoManager.skinData.textcolor_26keys);
        if (Global.shadowSwitch) {
            main_text.setShadowLayer(Global.shadowRadius, 0, 0, skinInfoManager.skinData.shadow);
            predict_text.setShadowLayer(Global.shadowRadius, 0, 0, skinInfoManager.skinData.shadow);
        }
        return button;
    }

    @Override
    public void setBackgroundAlpha(int alpha) {
        for (LinearLayout button : buttonList) {
            if (button.getBackground() != null) button.getBackground().setAlpha(0);
            (button.findViewById(R.id.main_text)).getBackground().setAlpha(alpha);
            (button.findViewById(R.id.predict_text)).getBackground().setAlpha(alpha);
        }
        shiftButton.getBackground().setAlpha(alpha);
        smileButton.getBackground().setAlpha(alpha);
        deleteButton.getBackground().setAlpha(alpha);
    }

    @Override
    public void clearAnimation() {
        for (LinearLayout button : buttonList) {
            button.clearAnimation();
        }
        deleteButton.clearAnimation();
        shiftButton.clearAnimation();
        smileButton.clearAnimation();
    }

    public void setTypeface(Typeface typeface) {
        shiftButton.setTypeface(typeface);
        deleteButton.setTypeface(typeface);
        smileButton.setTypeface(typeface);
    }

    private void onTouchEffect(View view, int action, int backgroundColor) {
        softKeyboard.transparencyHandle.handleAlpha(action);
        softKeyboard.keyboardTouchEffect.onTouchEffectWithAnim(
                view, action,
                skinInfoManager.skinData.backcolor_touchdown,
                backgroundColor
        );
    }

    private void onTouchEffectSpecial(View view, int action, int backgroundColor) {
        softKeyboard.transparencyHandle.handleAlpha(action);
        softKeyboard.keyboardTouchEffect.onTouchEffectWithAnimForQK(view, action,
                skinInfoManager.skinData.backcolor_touchdown,
                backgroundColor
        );
    }

    private void showSmile() {
        softKeyboard.quickSymbolViewGroup.setVisibility(View.GONE);
        softKeyboard.prefixViewGroup.setText(StringUtil.convertStringstoList(mAllSmileText));
        softKeyboard.prefixViewGroup.setBackgroundAlpha(Global.getCurrentAlpha());
        softKeyboard.prefixViewGroup.updateSkin();
        softKeyboard.prefixViewGroup.setVisibility(View.VISIBLE);
    }

    private void hideSmile() {
        softKeyboard.prefixViewGroup.setBackgroundColor(skinInfoManager.skinData.backcolor_prefix);
        softKeyboard.prefixViewGroup.setText(StringUtil.convertStringstoList(softKeyboard.mFuncKeyboardText));
        softKeyboard.prefixViewGroup.setVisibility(View.GONE);
        softKeyboard.quickSymbolViewGroup.setVisibility(View.VISIBLE);
        if (Kernel.getWordsNumber() > 0) {
            softKeyboard.quickSymbolViewGroup.setVisibility(View.GONE);
            softKeyboard.secondLayerLayout.setVisibility(View.GONE);
            softKeyboard.candidatesViewGroup.setVisibility(View.VISIBLE);
        }
    }

    //of course it should be in stack
    private String alphabetUpCase = "QWERTYUIOPASDFGHJKLZXCVBNM";
    private String alphabet = "qwertyuiopasdfghjklzxcvbnm";

    private boolean keyboardOnTouchEvent(View view, MotionEvent motionEvent) {
        View upLightView;
        onTouchEffectSpecial(view, motionEvent.getAction(), skinInfoManager.skinData.backcolor_26keys);
        if (view.getBackground() != null) view.getBackground().setAlpha(0);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                upLightView = softKeyboard.lightViewManager.mLightView[0];
                if (upLightView.isShown()) {
                    upLightView.setVisibility(View.GONE);
                    upLightView.clearAnimation();
                    upLightView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.light_bot_off));
                }
                if (motionEvent.getY() < 0) {
                    String commitText = (String) ((TextView) view.findViewById(R.id.predict_text)).getText();
                    if (commitText.length() > 0) {
                        commitText = InputMode.fullToHalf(commitText);
                        if (StringUtil.isAllLetter(commitText)) {
                            commitText = commitText.toLowerCase();
                            Global.redoText_single.clear();
                            softKeyboard.sendMsgToQKKernel(InputMode.fullToHalf(commitText));
                        } else {
                            softKeyboard.chooseWord(0);
                            softKeyboard.commitText(commitText);
                        }
                    }
                } else if (motionEvent.getX() > 0 && motionEvent.getX() < view.getMeasuredWidth()) {
                    int index = buttonList.indexOf(view);
                    Global.redoText_single.clear();
                    softKeyboard.sendMsgToQKKernel(alphabet.substring(index, index + 1));
                }
                break;
            case MotionEvent.ACTION_MOVE:
                upLightView = softKeyboard.lightViewManager.mLightView[0];
                if (motionEvent.getY() >= view.getY() && upLightView.isShown()) {
                    upLightView.setVisibility(View.GONE);
                    upLightView.clearAnimation();
                    upLightView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.light_top_off));
                }
                boolean mLightShow = false;
                for (View lightview : softKeyboard.lightViewManager.mLightView) {
                    mLightShow |= lightview.isShown();
                }
                if (motionEvent.getY() < view.getY() && !mLightShow) {
                    String commitText = ((TextView) view.findViewById(R.id.predict_text)).getText().toString();
                    if (commitText.length() < 1) break;
                    ((TextView) upLightView).setText(InputMode.halfToFull(commitText));
                    upLightView.setVisibility(View.VISIBLE);
                    upLightView.clearAnimation();
                    upLightView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.light_top));
                }
                break;
        }
        return true;
    }

    private View.OnTouchListener qkInputOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return keyboardOnTouchEvent(view, motionEvent);
        }
    };

    private boolean enKeyboardOnTouchEvent(View view, MotionEvent motionEvent) {
        onTouchEffectSpecial(view, motionEvent.getAction(), skinInfoManager.skinData.backcolor_26keys);
        if (view.getBackground() != null) view.getBackground().setAlpha(0);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                int index = buttonList.indexOf(view);
                String commitText = mShiftOn ? alphabetUpCase.substring(index, index + 1) : alphabet.substring(index, index + 1);
                if (Global.isInView(view, motionEvent)) {
                    softKeyboard.commitText(commitText);
                }
                break;
        }
        return true;
    }

    private View.OnTouchListener enInputOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return enKeyboardOnTouchEvent(view, motionEvent);
        }
    };

    private boolean shiftOnTouchEvent(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            mShiftOn = !mShiftOn;
            ((Button) v).setText(mShiftOn ? shiftText[1] : shiftText[0]);
        }
        softKeyboard.transparencyHandle.handleAlpha(event.getAction());
        onTouchEffect(v, event.getAction(), skinInfoManager.skinData.backcolor_shift);
        return true;
    }

    /**
     * 功能：监听英文键盘Shift键的touch事件
     * 调用时机：touch英文键盘的Shift键
     */
    private View.OnTouchListener mShiftKeyOnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return shiftOnTouchEvent(v, event);
        }
    };

    private int[] performActions = {
            android.R.id.selectAll,
            android.R.id.copy,
            android.R.id.cut,
            android.R.id.paste
    };

    private boolean smileOnTouchEvent(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showSmile();
                break;
            case MotionEvent.ACTION_MOVE:
                PreFixViewGroup prefixViewGroup = softKeyboard.prefixViewGroup;
                float X = event.getX();
                float Y = event.getY();
                float perWidth = softKeyboard.keyboardWidth / SMILE_KEYS_NUM;
                if (Y <= 0) {
                    prefixViewGroup.setBackgroundColor(skinInfoManager.skinData.backcolor_prefix);
                    if (0 < X && X < perWidth * SMILE_FUNC_NUM) {
                        prefixViewGroup.setBackgroundColorByIndex(skinInfoManager.skinData.backcolor_touchdown, (int) (X / perWidth));
                    }
                } else {
                    prefixViewGroup.setBackgroundColor(skinInfoManager.skinData.backcolor_prefix);
                }
                prefixViewGroup.setBackgroundAlpha(Global.getCurrentAlpha());
                break;
            case MotionEvent.ACTION_UP:
                hideSmile();
                float x = event.getX();
                float y = event.getY();
                float perwidth = softKeyboard.keyboardWidth / SMILE_KEYS_NUM;
                InputConnection ic = softKeyboard.getCurrentInputConnection();
                if (y <= 0 && ic != null) {
                    x = Math.max(0, Math.min(SMILE_FUNC_NUM * perwidth - 1, x));
                    int select = ((int) (x / perwidth));
                    ic.performContextMenuAction(performActions[select]);
                    if (select == 2) {
                        softKeyboard.editPinyin("", false);
                    }
                }
                break;
        }
        softKeyboard.transparencyHandle.handleAlpha(event.getAction());
        onTouchEffect(v, event.getAction(), skinInfoManager.skinData.backcolor_smile);
        return false;
    }

    private View.OnTouchListener mSmileKeyOnTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event) {
            return smileOnTouchEvent(v, event);
        }
    };

    private boolean deleteOnTouchEvent(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            softKeyboard.mHandler.sendEmptyMessageDelayed(softKeyboard.MSG_REPEAT, SoftKeyboard.REPEAT_START_DELAY);
        } else if (event.getAction() == MotionEvent.ACTION_MOVE && Global.slideDeleteSwitch) {
            softKeyboard.lightViewManager.HideLightView(event.getX(), event.getY(), v.getWidth(), v.getHeight());
            if (Global.LEFT == ViewsUtil.computeDirection(event.getX(), event.getY(), v.getWidth(), v.getHeight())) {
                softKeyboard.lightViewManager.ShowLightView(event.getX(), event.getY(), v.getWidth(), v.getHeight(), "清空");
                softKeyboard.mHandler.removeMessages(softKeyboard.MSG_REPEAT);
            } else if (Global.UP == ViewsUtil.computeDirection(event.getX(), event.getY(), v.getWidth(), v.getHeight())) {
                softKeyboard.lightViewManager.ShowLightView(event.getX(), event.getY(), v.getWidth(), v.getHeight(), "恢复");
                softKeyboard.mHandler.removeMessages(softKeyboard.MSG_REPEAT);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            softKeyboard.mHandler.removeMessages(softKeyboard.MSG_REPEAT);
            softKeyboard.lightViewManager.lightViewAnimate(v, event);
            if (event.getX() < 0 && Global.slideDeleteSwitch) {
                softKeyboard.deleteAll();
            } else if (event.getY() < 0) {
                if (Global.redoTextForDeleteAll != "") {
                    softKeyboard.commitText(Global.redoTextForDeleteAll);
                    Global.redoTextForDeleteAll = "";
                }
                if (!Global.redoTextForDeleteAll_preedit.equals("")) {
                    softKeyboard.sendMsgToQKKernel(Global.redoTextForDeleteAll_preedit);
                    Global.redoTextForDeleteAll_preedit = "";
                } else {
                    if (Global.redoText_single.size() > 0) {
                        InputAction ia = Global.redoText_single.pop();
                        if (ia.Type == InputAction.TEXT_TO_KERNEL) {
                            softKeyboard.sendMsgToQKKernel(ia.text.toString());
                        } else {
                            softKeyboard.commitText(ia.text.toString());
                        }
                    }
                }
            } else {
                softKeyboard.deleteLast();
            }
        }
        softKeyboard.transparencyHandle.handleAlpha(event.getAction());
        onTouchEffect(v, event.getAction(), skinInfoManager.skinData.backcolor_delete);
        return false;
    }


    private View.OnTouchListener mDeleteOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return deleteOnTouchEvent(v, event);
        }
    };


    private boolean cursorLocateOnTouchEvent(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float rate = Math.max(0, Math.min(1f, event.getX() / v.getMeasuredWidth()));
            softKeyboard.pinyinProc.setCursorByRate(softKeyboard.getCurrentInputConnection(), rate);
        }
        return true;
    }

    private View.OnTouchListener cursorLocateOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return cursorLocateOnTouchEvent(v, event);
        }
    };

}
