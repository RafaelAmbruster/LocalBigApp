package com.app.localbig.view.tagview.drawers;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import com.app.localbig.view.tagview.TagView;
import com.app.localbig.view.tagview.Tags;
import com.app.localbig.view.tagview.Utils;

public class ReversedModernSharpTagDrawer extends ReversedSharpTagDrawer {
    @Override
    public void drawTag(Rect bounds, Canvas canvas, TagView.TagViewData data) {
        RectF formattedBounds = Utils.toRectF(bounds);
        RectF rect = new RectF(formattedBounds);
        rect.left += data.tagRightPadding * Tags.SHARP_TAG_MULTIPLIER;
        float halfOfRectHeight = (rect.bottom - rect.top) / 2;
        canvas.drawRect(rect, data.backgroundPaint);
        float xPos = formattedBounds.right - data.tagRightPadding;
        float yPos = (formattedBounds.bottom - formattedBounds.top) / 2;
        canvas.drawCircle(xPos, yPos, data.tagCircleRadius, data.circlePaint);
        Path trianglePath = createTrianglePath(data, rect, halfOfRectHeight);
        canvas.drawPath(trianglePath, data.trianglePaint);
    }
}
