package com.example.esp.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import java.io.Serializable;

/**
 * 支持自定义各种属性的Dialog
 *
 * Created by Ryan Hu on 2017/9/30.
 */

public class GravityDialog extends AppCompatDialog {

    private GravityDialog(@NonNull Context context, @NonNull BaseBuilder builder) {
        this(context,
                builder.dialogView,
                builder.gravity,
                builder.animation,
                builder.dimAmount,
                builder.layoutWidth,
                builder.layoutHeight,
                builder.cancelable,
                builder.cancelOnTouchOutside);
    }

    public GravityDialog(
            @NonNull Context context,
            @NonNull View dialogView,
            int gravity,
            int animation,
            float dimAmount,
            int layoutWidth,
            int layoutHeight,
            boolean cancelable,
            boolean cancelOnTouchOutside) {

        super(context);

        Window window = getWindow();

        window.requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(dialogView);        //这步要在requestFeature()之后

        WindowManager.LayoutParams lp = window.getAttributes();
        if (layoutWidth != Integer.MIN_VALUE) {
            lp.width = layoutWidth;
        }
        if (layoutHeight != Integer.MIN_VALUE) {
            lp.height = layoutHeight;
        }

        lp.gravity = gravity;

        if (animation > 0) lp.windowAnimations = animation;
        if (dimAmount < 0f) {
            lp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        } else {
            lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            lp.dimAmount = dimAmount;
        }

        window.setAttributes(lp);

        window.setBackgroundDrawable(new ColorDrawable(0x00000000));        //禁用系统默认风格的dialog背景

        setCancelable(cancelable);
        setCanceledOnTouchOutside(cancelOnTouchOutside);
    }

    public static class Builder extends BaseBuilder<Builder> {
    }

    public static class BaseBuilder<T extends BaseBuilder<T>> implements Serializable {

        transient View dialogView;
        int gravity;
        int animation;
        float dimAmount;
        int layoutWidth;
        int layoutHeight;
        boolean cancelable;
        boolean cancelOnTouchOutside;

        public static BaseBuilder newBuilder() {
            return new BaseBuilder();
        }

        public BaseBuilder() {
            gravity = Gravity.CENTER;
            dimAmount = 0.6f;
            layoutWidth = WindowManager.LayoutParams.MATCH_PARENT;
            layoutHeight = WindowManager.LayoutParams.WRAP_CONTENT;
            cancelable = false;
            cancelOnTouchOutside = false;
        }

        /**
         * Dialog的内容View
         *
         * @param dialogView
         * @return
         */
        public T dialogView (View dialogView) {
            this.dialogView = dialogView;
            return (T)this;
        }

        /**
         * Dialog的位置
         *
         * @see Gravity
         * @param gravity
         * @return
         */
        public T gravity (int gravity) {
            this.gravity = gravity;
            return (T)this;
        }

        /**
         * Dialog的进场/出场动画，注意这个不是animation资源，而是一个style，其中包含了android:windowEnterAnimation和android:windowExitAnimation两个属性
         * <pre>
         * &lt;style name="MyDialogAnimation"&gt;
         *     &lt;item name="android:windowEnterAnimation"&gt;@anim/bottom_popup_enter&lt;/item&gt;
         *     &lt;item name="android:windowExitAnimation"&gt;@anim/bottom_popup_exit&lt;/item&gt;
         * &lt;/style&gt;
         * </pre>
         *
         * @param animation 动画的式样资源ID
         * @return
         */
        public T animation (int animation) {
            this.animation = animation;
            return (T)this;
        }

        /**
         * Dialog蒙层的暗度
         *
         * @see WindowManager.LayoutParams#dimAmount
         * @see WindowManager.LayoutParams#FLAG_DIM_BEHIND
         * @param dimAmount
         * @return
         */
        public T dimAmount (float dimAmount) {
            this.dimAmount = dimAmount;
            return (T)this;
        }

        /**
         * Dialog的宽度
         *
         * @see android.view.ViewGroup.LayoutParams#width
         * @param layoutWidth
         * @return
         */
        public T layoutWidth (int layoutWidth) {
            this.layoutWidth = layoutWidth;
            return (T)this;
        }

        /**
         * Dialog的高度
         *
         * @see android.view.ViewGroup.LayoutParams#height
         * @param layoutHeight
         * @return
         */
        public T layoutHeight (int layoutHeight) {
            this.layoutHeight = layoutHeight;
            return (T)this;
        }

        /**
         * Dialog是否可点击后退取消
         *
         * @param cancelable
         * @return
         */
        public T cancelable (boolean cancelable) {
            this.cancelable = cancelable;
            return (T)this;
        }

        /**
         * Dialog是否可点击外部区域取消
         *
         * @param cancelOnTouchOutside
         * @return
         */
        public T cancelOnTouchOutside (boolean cancelOnTouchOutside) {
            this.cancelOnTouchOutside = cancelOnTouchOutside;
            return (T)this;
        }

        public GravityDialog build (@NonNull Context context) {

            if (context == null) {
                throw new IllegalArgumentException("Context is null");
            }
            if (dialogView == null) {
                throw new IllegalArgumentException("Content view is null");
            }

            return new GravityDialog(context, this);
        }
    }
}
