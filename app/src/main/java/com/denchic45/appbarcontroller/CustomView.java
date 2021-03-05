package com.denchic45.appbarcontroller;

import android.content.Context;
import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

public class CustomView extends LinearLayout {
    private int color;

    public CustomView(Context context) {
        super(context);
        inflate();
        setId(R.id.accelerate);
        setBackgroundColor(Color.BLUE);
    }

    private void inflate() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view, this);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(v -> {
            color = Color.GREEN;
            setBackgroundColor(color);
        });
    }

    @Override
    public Parcelable onSaveInstanceState() {
        // Obtain any state that our super class wants to save.
        Parcelable superState = super.onSaveInstanceState();

        // Wrap our super class's state with our own.
        SavedState myState = new SavedState(superState);
        myState.color = color;

        // Return our state along with our super class's state.
        return myState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState savedState = (SavedState) state;

        super.onRestoreInstanceState(savedState.getSuperState());

        // Grab our properties out of our SavedState.
        this.color = savedState.color;

        setBackgroundColor(color);
        // Update our visuals in whatever way we want, like...
        requestLayout(); //...or...
        invalidate(); //...or...
    }

    private static class SavedState extends BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        int color;

        SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            color = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(color);
        }
    }
}
