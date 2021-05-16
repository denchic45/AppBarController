package com.denchic45.appbarcontroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.NotNull;

public class ToolbarBehavior extends AppBarLayout.Behavior {

    private boolean scrollable = false;
    private int count;
    private View scrollableView;

    public ToolbarBehavior(View scrollableView) {
        this.scrollableView = scrollableView;
    }

    public ToolbarBehavior(Context context, AttributeSet attrs, View scrollableView) {
        super(context, attrs);
        this.scrollableView = scrollableView;
    }

    @Override
    public boolean onInterceptTouchEvent(@NotNull CoordinatorLayout parent, @NotNull AppBarLayout child, @NotNull MotionEvent ev) {
        return scrollable && super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onStartNestedScroll(@NotNull CoordinatorLayout parent, AppBarLayout child, View directTargetChild, View target, int nestedScrollAxes, int type) {
        updatedScrollable(scrollableView);
        return scrollable && super.onStartNestedScroll(parent, child, scrollableView, target, nestedScrollAxes, type);
    }

    @Override
    public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout child, View target, float velocityX, float velocityY, boolean consumed) {
        return scrollable && super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
    }

    public void listenScrolling(View scrollableView) {
        this.scrollableView = scrollableView;
    }

    private void updatedScrollable(View directTargetChild) {
        if (directTargetChild instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) directTargetChild;
            RecyclerView.Adapter<?> adapter = recyclerView.getAdapter();
            if (adapter != null) {
                if (adapter.getItemCount() != count) {
                    scrollable = false;
                    count = adapter.getItemCount();
                    if (count == 0) {
                        scrollable = false;
                        return;
                    }
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    if (layoutManager != null) {
                        int lastVisibleItem = 0;
                        int firstVisibleItem = 0;
                        if (layoutManager instanceof LinearLayoutManager) {
                            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                            lastVisibleItem = Math.abs(linearLayoutManager.findLastCompletelyVisibleItemPosition());
                            firstVisibleItem = Math.abs(linearLayoutManager.findFirstCompletelyVisibleItemPosition());
                        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                            int[] lastItems = staggeredGridLayoutManager.findLastCompletelyVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
                            int[] firstItems = staggeredGridLayoutManager.findFirstCompletelyVisibleItemPositions(new int[staggeredGridLayoutManager.getSpanCount()]);
                            lastVisibleItem = Math.abs(lastItems[lastItems.length - 1]);
                            firstVisibleItem = Math.abs(firstItems[firstItems.length -1]);
                        }
                        scrollable = lastVisibleItem < count - 1 || firstVisibleItem > 0;
                    } else if (adapter.getItemCount() == 0) {
                        scrollable = false;
                    }
                }
            }
        } else scrollable = true;
    }

    public boolean isScrollable() {
        return scrollable;
    }
}
