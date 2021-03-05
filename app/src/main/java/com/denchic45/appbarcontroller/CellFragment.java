package com.denchic45.appbarcontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.denchic45.library.AppBarController;

public class CellFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";

    private String argMessage;
    private TextView tvMessage;

    public static CellFragment newInstance(String message) {
        CellFragment fragment = new CellFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_cell, container, false);
        tvMessage = root.findViewById(R.id.tv_message);
        if (getArguments() != null) {
            argMessage = getArguments().getString(ARG_MESSAGE);
            tvMessage.setText(argMessage);
            AppBarController.findController(getActivity()).setTitle(argMessage);
        }

        ListView listView = root.findViewById(R.id.list);

        listView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new String[]{
                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
                "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
                "Китти", "Масяня", "Симба", "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
                "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
                "Китти", "Масяня", "Симба"
        }));
        listView.setNestedScrollingEnabled(true);
        return root;
    }
}