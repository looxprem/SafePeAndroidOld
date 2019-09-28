package com.safepayu.wallet.adapterpackage;

import android.view.View;

/**
 * Created by Saksham on 3/6/2018.
 */

public interface ClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
