package com.app.localbig.view.tagview.drawers;

import android.graphics.Canvas;
import android.graphics.Rect;
import com.app.localbig.view.tagview.TagView;

public interface TagDrawer {
    // add java docs for this method
    void drawTag(Rect bounds, Canvas canvas, TagView.TagViewData data);
}
