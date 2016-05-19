package com.tlabs.android.evanova.mvp;

public interface ActivityView {

	void setBackground(final String url);

	void setTitle(CharSequence s);

	void setTitle(int sRes);

	void setTitleDescription(CharSequence s);

	void setTitleDescription(int sRes);

	void setTitleIcon(final int iconRes);

	void setTitleIcon(final String url);

	void setLoading(final boolean loading);

    void showMessage(CharSequence s);

    void showMessage(int sRes);

    void showError(CharSequence s);

    void showError(int sRes);

}
