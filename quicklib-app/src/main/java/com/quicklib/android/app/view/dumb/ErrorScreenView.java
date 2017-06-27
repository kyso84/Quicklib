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
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.quicklib.android.app.R;
import com.quicklib.android.core.helper.StringHelper;


public class ErrorScreenView extends ErrorBannerView {


    protected static final String KEY_TITLE = "title";
    protected static final String KEY_FOOTER = "footer";

    protected TextView title;
    protected TextView footer;

    public ErrorScreenView(Context context) {
        super(context);
    }

    public ErrorScreenView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ErrorScreenView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Nullable
    @Override
    public void onCreate(@Nullable AttributeSet attributeSet, Bundle bundle) {
        super.onCreate(attributeSet, bundle);
        if (attributeSet != null) {
            TypedArray a = getContext().obtainStyledAttributes(attributeSet, R.styleable.ErrorScreenView, 0, 0);
            if (a != null) {

                String localTitle = a.getString(R.styleable.ErrorScreenView_title);
                if (!TextUtils.isEmpty(localTitle)) {
                    bundle.putString(KEY_TITLE, localTitle);
                }

                String localFooter = a.getString(R.styleable.ErrorScreenView_footer);
                if (!TextUtils.isEmpty(localFooter)) {
                    bundle.putString(KEY_FOOTER, localFooter);
                }
            }
            a.recycle();
        }
    }

    @NonNull
    @Override
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, boolean b) {
        View view = layoutInflater.inflate(R.layout.view_error_screen, this, b);
        title = findViewById(R.id.view_error_title);
        footer = findViewById(R.id.view_error_footer);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        if (saveInstanceState != null) {

        } else if (getArguments() != null) {
            Builder builder = new Builder();

            String localTitle = getArguments().getString(KEY_TITLE, "");
            if (!TextUtils.isEmpty(localTitle)) {
                builder.withTitle(localTitle);
            }

            String localFooter = getArguments().getString(KEY_FOOTER, "");
            if (!TextUtils.isEmpty(localFooter)) {
                builder.withFooter(localFooter);
            }
        }
    }

    public void showUnknownError() {
        Builder builder = new Builder();
        builder.withImage(getContext(), R.drawable.ic_error_unknown);
        builder.withImageSize(getContext(), R.dimen.image_large);
        builder.withTitle(getContext(), R.string.error_unknown);
        builder.withMessage(getContext(), R.string.error_we_apologize);
        format(builder);
    }

    public void showNetworkError(@NonNull View.OnClickListener buttonListener) {
        Builder builder = new Builder();
        builder.withImage(getContext(), R.drawable.ic_error_network);
        builder.withImageSize(getContext(), R.dimen.image_large);
        builder.withTitle(getContext(), R.string.error_no_network);
        builder.withButton(getContext(), R.string.try_again, buttonListener);
        format(builder);
    }

    public void showEmptyList() {
        Builder builder = new Builder();
        builder.withImage(getContext(), R.drawable.ic_error_empty_list);
        builder.withImageSize(getContext(), R.dimen.image_large);
        builder.withMessage(getContext(), R.string.error_empty_list);
        format(builder);
    }


    public void format(Builder builder) {
        super.format(builder);

        // Add title
        if (!TextUtils.isEmpty(builder.title)) {
            title.setText(StringHelper.fromHtml(builder.title));
            title.setVisibility(VISIBLE);
        } else {
            title.setVisibility(GONE);
        }


        // Add footer (usually a disclaimer)
        if (!TextUtils.isEmpty(builder.footer)) {
            footer.setText(StringHelper.fromHtml(builder.footer));
            footer.setVisibility(VISIBLE);
        } else {
            footer.setVisibility(GONE);
        }
    }


    public static class Builder extends ErrorBannerView.Builder {
        private String title = null;
        private String footer = null;


        public Builder withTitle(@NonNull Context context, @StringRes int titleId) {
            return withTitle(context.getString(titleId));
        }

        public Builder withTitle(@NonNull String titleText) {
            this.title = titleText;
            return this;
        }

        public Builder withFooter(@NonNull Context context, @StringRes int footerId) {
            return withFooter(context.getString(footerId));
        }

        public Builder withFooter(@NonNull String footerText) {
            this.footer = footerText;
            return this;
        }

        public ErrorScreenView build(Context context) {
            ErrorScreenView errorView = new ErrorScreenView(context);
            errorView.format(this);
            return errorView;
        }
    }

}
