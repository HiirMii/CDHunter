package com.example.android.cdhunter.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WishlistWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WishlistWidgetFactory(this, intent);
    }
}
