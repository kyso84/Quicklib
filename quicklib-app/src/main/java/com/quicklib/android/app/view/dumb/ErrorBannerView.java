package com.quicklib.android.app.view.dumb;
/*
 * Copyright 2017 (C) Yellow Pages
 *
 * Created by: the French Connection (bdescha1)
 * Created on: 19 juin 2017
 *
 * Project: yp-messaging
 * Package: com.ypg.android.common.view.dumb
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.quicklib.android.core.helper.ContextHelper;
import com.quicklib.android.core.helper.StringHelper;
import com.quicklib.android.core.view.CustomFrameLayout;
import com.quicklib.android.app.R;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class ErrorBannerView extends CustomFrameLayout {

    protected static final String KEY_IMAGE = "image";
    protected static final String KEY_IMAGE_SIZE = "imageSize";
    protected static final String KEY_MESSAGE = "message";

    protected ImageView image;
    protected TextView message;
    protected Button button;

    public ErrorBannerView(Context context) {
        super(context);
    }

    public ErrorBannerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    @Override
    public void onCreate(@Nullable AttributeSet attributeSet, Bundle bundle) {
        if (attributeSet != null) {
            TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.ErrorScreenView, 0, 0);
            if (a != null) {

                int localImage = a.getResourceId(R.styleable.ErrorScreenView_image, 0);
                if (localImage != 0) {
                    bundle.putInt(KEY_IMAGE, localImage);
                }

                int localImageSize = a.getDimensionPixelSize(R.styleable.ErrorScreenView_image, 0);
                if (localImageSize != 0) {
                    bundle.putInt(KEY_IMAGE_SIZE, localImageSize);
                }

                String localMessage = a.getString(R.styleable.ErrorScreenView_message);
                if (!TextUtils.isEmpty(localMessage)) {
                    bundle.putString(KEY_MESSAGE, localMessage);
                }

            }
            a.recycle();
        }
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean b) {
        View view = layoutInflater.inflate(R.layout.view_error_banner, this, b);
        image = findViewById(R.id.view_error_image);
        message = findViewById(R.id.view_error_message);
        button = findViewById(R.id.view_error_button);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        if (saveInstanceState != null) {

        } else if (getArguments() != null) {
            Builder builder = new Builder();

            int localImage = getArguments().getInt(KEY_IMAGE, 0);
            if (localImage > 0) {
                builder.withImage(getContext(), localImage);
            }

            int localImageSize = getArguments().getInt(KEY_IMAGE_SIZE, 0);
            if (localImageSize > 0) {
                builder.withImageSize(getContext(), localImageSize);
            }

            String localMessage = getArguments().getString(KEY_MESSAGE, "");
            if (!TextUtils.isEmpty(localMessage)) {
                builder.withMessage(localMessage);
            }
        }
    }

    public void showUnknownError() {
        Builder builder = new Builder()
                .withImage(getContext(), R.drawable.ic_error_empty_list)
                .withMessage(getContext(), R.string.error_unknown);
        format(builder);
    }

    public void showNetworkError(@NonNull View.OnClickListener buttonListener) {
        Builder builder = new Builder()
                .withImage(getContext(), R.drawable.ic_error_network)
                .withMessage(getContext(), R.string.error_no_network)
                .withButton(getContext(), R.string.try_again, buttonListener);
        format(builder);
    }


    public void format(Builder builder) {
        // Add image
        if (builder.image != null) {
            image.setImageDrawable(builder.image);
            image.setVisibility(VISIBLE);
            image.getLayoutParams().width = builder.imageSize;
            image.getLayoutParams().height = builder.imageSize;
            image.requestLayout();
        } else {
            image.setVisibility(GONE);
        }

        // Add message
        if (!TextUtils.isEmpty(builder.message)) {
            // Is there clickable words ?
            if (builder.messageKeywords != null && builder.messageListener != null) {
                SpannableString spannableString = getClickableWords(builder.message, builder.messageKeywords, builder.messageListener, builder.messageCaseSensitive, builder.messageKeywordColor);
                message.setText(spannableString);
                message.setMovementMethod(LinkMovementMethod.getInstance());
            } else {
                message.setText(StringHelper.fromHtml(builder.message));
            }
            message.setVisibility(VISIBLE);
        } else {
            message.setVisibility(GONE);
        }

        // Add a button
        if (!TextUtils.isEmpty(builder.button) && builder.buttonListener != null) {
            button.setText(StringHelper.fromHtml(builder.button));
            button.setOnClickListener(builder.buttonListener);
            button.setVisibility(VISIBLE);
        } else {
            button.setVisibility(GONE);
        }
    }

    public interface WordClickListener {
        void onClick(String word);
    }

    protected SpannableString getClickableWords(final String text, final List<String> keywords, final WordClickListener wordClickListener, boolean caseSensitive, @ColorInt int keywordColor) {
        SpannableString spannableString = new SpannableString(text);
        for (final String keyword : keywords) {
            boolean contains = caseSensitive ? text.contains(keyword) : text.toLowerCase(Locale.getDefault()).contains(keyword.toLowerCase(Locale.getDefault()));
            if (contains) {
                // Define clickable action
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        wordClickListener.onClick(keyword);
                    }
                };

                // Apply to the right word
                int idxStart = text.toLowerCase(Locale.getDefault()).indexOf(keyword.toLowerCase(Locale.getDefault()));
                int idxEnd = idxStart + keyword.length();
                spannableString.setSpan(clickableSpan, idxStart, idxEnd, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                spannableString.setSpan(new StyleSpan(Typeface.BOLD), idxStart, idxEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                spannableString.setSpan(new ForegroundColorSpan(keywordColor), idxStart, idxEnd, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            }
        }
        return spannableString;
    }


    public static class Builder {
        private Drawable image = null;
        private int imageSize = WRAP_CONTENT;

        private String message = null;

        private List<String> messageKeywords = null;
        private WordClickListener messageListener = null;
        private boolean messageCaseSensitive = false;
        @ColorInt
        private int messageKeywordColor = Color.BLUE;

        private String button = null;
        private OnClickListener buttonListener = null;


        public Builder withImage(@NonNull Context context, @DrawableRes int imageId) {
            return withImage(ContextCompat.getDrawable(context, imageId));
        }

        public Builder withImage(@NonNull Drawable image) {
            this.image = image;
            return this;
        }

        public Builder withImageSize(@NonNull Context context, @DimenRes int imageSize) {
            this.imageSize = ContextHelper.getDimenPx(context, imageSize);
            return this;
        }

        public Builder withMessage(@NonNull Context context, @StringRes int messageId) {
            return withMessage(context.getString(messageId));
        }

        public Builder withMessage(@NonNull String contentText) {
            this.message = contentText;
            return this;
        }

        public Builder withMessage(@NonNull Context context, @StringRes int messageTextId, @NonNull WordClickListener textListener, @NonNull String... keywordList) {
            return withMessage(context.getString(messageTextId), textListener, keywordList);
        }

        public Builder withMessage(@NonNull String messageText, @NonNull WordClickListener textListener, @NonNull String... keywordList) {
            this.message = messageText;
            if (keywordList != null && keywordList.length > 0) {
                this.messageKeywords = Arrays.asList(keywordList);
                this.messageListener = textListener;
            }
            return this;
        }

        public Builder withMessageCaseSensitive(boolean caseSensitive) {
            this.messageCaseSensitive = caseSensitive;
            return this;
        }

        public Builder withMessageKeywordColor(@ColorInt int color) {
            this.messageKeywordColor = color;
            return this;
        }

        public Builder withMessageKeywordColorRes(@NonNull Context context, @ColorRes int colorRes) {
            this.messageKeywordColor = ContextCompat.getColor(context, colorRes);
            return this;
        }

        public Builder withButton(@NonNull Context context, @StringRes int buttonTextId, @NonNull View.OnClickListener buttonListener) {
            return withButton(context.getString(buttonTextId), buttonListener);
        }

        public Builder withButton(@NonNull String buttonText, @NonNull View.OnClickListener buttonListener) {
            this.button = buttonText;
            this.buttonListener = buttonListener;
            return this;
        }

        public ErrorBannerView build(Context context) {
            ErrorBannerView errorView = new ErrorBannerView(context);
            errorView.format(this);
            return errorView;
        }
    }
}
