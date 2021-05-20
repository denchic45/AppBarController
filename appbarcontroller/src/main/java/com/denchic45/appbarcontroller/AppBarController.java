package com.denchic45.appbarcontroller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.denchic45.RecyclerViewFinishListener;
import com.google.android.material.appbar.AppBarLayout;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public final class AppBarController {

    private static final HashMap<Lifecycle, AppBarController> controllers = new HashMap<>();
    private final AppBarLayout appBarLayout;
    private final AppCompatActivity activity;
    private final Map<Integer, View> views = new HashMap<>();
    private Toolbar toolbar;

    private AppBarController(@NotNull AppCompatActivity activity, @NonNull AppBarLayout appBarLayout) {
        final Lifecycle lifecycle = activity.getLifecycle();
        controllers.put(lifecycle, this);
        this.appBarLayout = appBarLayout;
        this.activity = activity;
        observeLifecycle(lifecycle);
        findToolbar();
    }

    public static @NotNull AppBarController findController(@NotNull LifecycleOwner lifecycleOwner) {
        AppBarController controller = controllers.get(lifecycleOwner.getLifecycle());
        if (controller == null) {
            throw new RuntimeException("Controller does not exist");
        }
        return controller;
    }

    @Contract("_, _ -> new")
    public static @NotNull AppBarController create(AppCompatActivity activity, @NonNull AppBarLayout appBarLayout) {
        return new AppBarController(activity, appBarLayout);
    }

    public void addView(@LayoutRes int layoutId) {
        View view = LayoutInflater.from(activity).inflate(layoutId, appBarLayout);
        int viewId = view.getId();
        views.put(viewId, view);
    }

    public void addView(@NotNull View view, @AppBarLayout.LayoutParams.ScrollFlags int scrollFlags) {
        addView(view);
        setScrollFlags(view, scrollFlags);
    }

    public void addView(View view) {
        appBarLayout.addView(view);
        views.put(view.getId(), view);
    }

    private void setScrollFlags(@NotNull View view, @AppBarLayout.LayoutParams.ScrollFlags int scrollFlags) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        ((AppBarLayout.LayoutParams) params).setScrollFlags(scrollFlags);
        view.setLayoutParams(params);
    }

    public void setExpanded(boolean expand, boolean animate) {
        appBarLayout.setExpanded(expand, animate);
    }

    public void setExpandableIfViewCanScroll(@NonNull View view, LifecycleOwner lifecycleOwner) {

        if (view instanceof RecyclerView) {
            expandAppbarIfNecessary((RecyclerView) view);
            RecyclerView.Adapter<?> adapter = ((RecyclerView) view).getAdapter();
            RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {

                @Override
                public void onChanged() {
                    expandAppbarIfNecessary((RecyclerView) view);
                    super.onChanged();
                }

                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    expandAppbarIfNecessary((RecyclerView) view);
                    super.onItemRangeInserted(positionStart, itemCount);
                }

                @Override
                public void onItemRangeRemoved(int positionStart, int itemCount) {
                    expandAppbarIfNecessary((RecyclerView) view);
                    super.onItemRangeRemoved(positionStart, itemCount);
                }

                @Override
                public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                    expandAppbarIfNecessary((RecyclerView) view);
                    super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                }
            };
            adapter.registerAdapterDataObserver(observer);
            lifecycleOwner.getLifecycle().addObserver(new LifecycleEventObserver() {
                @Override
                public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
                    if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                        adapter.unregisterAdapterDataObserver(observer);
                        lifecycleOwner.getLifecycle().removeObserver(this);
                    }
                }
            });
        }
    }

    private void expandAppbarIfNecessary(@NonNull RecyclerView recyclerView) {
        new RecyclerViewFinishListener(recyclerView, () -> {
            CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
            behavior.setDragCallback(new AppBarLayout.Behavior.DragCallback() {
                @Override
                public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                    return recyclerView.isNestedScrollingEnabled();
                }
            });
            boolean canScrollDown = recyclerView.canScrollVertically(1);
            boolean canScrollUp = recyclerView.canScrollVertically(-1);
            if (!canScrollDown && !canScrollUp) {
                appBarLayout.setExpanded(true, true);
                recyclerView.setNestedScrollingEnabled(false);
            } else {
                recyclerView.setNestedScrollingEnabled(true);
            }
        });
    }

    private boolean checkScrollableRecyclerView(RecyclerView recyclerView, RecyclerView.Adapter<?> adapter) {
        if (adapter != null) {
                if (adapter.getItemCount() == 0) {
                    return false;
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
                    return lastVisibleItem < adapter.getItemCount() - 1 || firstVisibleItem > 0;
                } else if (adapter.getItemCount() == 0) {
                    return false;
                }
        }
        return false;
    }

    public @Nullable View getView(@IdRes int viewId) {
        return views.get(viewId);
    }

    private void findToolbar() {
        for (int i = 0; i < appBarLayout.getChildCount(); i++) {
            final View view = appBarLayout.getChildAt(i);
            if (view instanceof Toolbar) {
                toolbar = (Toolbar) view;
                activity.setSupportActionBar(toolbar);
            }
        }
    }

    private void observeLifecycle(@NotNull Lifecycle lifecycle) {
        lifecycle.addObserver((LifecycleEventObserver) (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_DESTROY)) {
                controllers.remove(lifecycle);
            }
        });
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public void setToolbar(@LayoutRes int resId) {
        View view = LayoutInflater.from(appBarLayout.getContext()).inflate(resId, null);
        if (hasToolbar() && toolbar.getClass() == view.getClass()) {
            return;
        }
        if (view instanceof Toolbar) {
            if (hasToolbar()) {
                appBarLayout.removeView(toolbar);
            }
            toolbar = (Toolbar) view;
            appBarLayout.addView(toolbar);
            activity.setSupportActionBar(toolbar);
        } else {
            throw new RuntimeException("View does not instance of toolbar: " + view.getClass());
        }
    }

    public boolean hasToolbar() {
        return toolbar != null;
    }

    public void setTitle(String title) {
        activity.setTitle(title);
    }

    public void removeView(@NotNull View view) {
        views.remove(view.getId());
        appBarLayout.removeView(view);
    }

    public void showToolbar() {
        appBarLayout.setExpanded(true, true);
    }

    public enum EXPAND {
        IF_CAN_SCROLL, ALWAYS, DISABLE
    }
}
