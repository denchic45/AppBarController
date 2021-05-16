package com.denchic45.sample;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.denchic45.appbarcontroller.AppBarController;

public class CellFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";

    private String argMessage;
    private TextView tvMessage;
    private final String[] bigList = new String[]{
            "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
            "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
            "Китти", "Масяня", "Симба", "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
            "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
            "Китти", "Масяня", "Симба"
    };

    private final String[] smallList = new String[]{"Рыжик", "Барсик", "Мурзик"};
    private RecyclerView rv;
    private SimpleAdapter adapter;

    @NonNull
    public static CellFragment newInstance(String message) {
        CellFragment fragment = new CellFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    public void putList(String[] list) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            adapter.setList(list);
            adapter.notifyDataSetChanged();
        },500);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cell, container, false);
        tvMessage = root.findViewById(R.id.tv_message);
        AppBarController controller = AppBarController.findController(getActivity());
        if (getArguments() != null) {
            argMessage = getArguments().getString(ARG_MESSAGE);
            tvMessage.setText(argMessage);
            controller.setTitle(argMessage);
        }
        rv = root.findViewById(R.id.list);
        controller.setExpandableIfViewCanScroll(rv);
        adapter = new SimpleAdapter(bigList);
        rv.setAdapter(adapter);
        return root;
    }
}