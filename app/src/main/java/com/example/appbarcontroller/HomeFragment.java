package com.example.appbarcontroller;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.library.AppBarController;


public class HomeFragment extends Fragment {

    private AppBarController controller;
    private Button btnBottomMenu, btnTabs, btnDynamicallyChanges;
    private NavController navController;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        navController = NavHostFragment.findNavController(HomeFragment.this);
        controller = AppBarController.findController(getActivity());
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        btnBottomMenu = root.findViewById(R.id.btn_bottom_menu);
        btnTabs = root.findViewById(R.id.btn_tabs);
        btnDynamicallyChanges = root.findViewById(R.id.btn_dynamically_changes);

        return root;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_bottom, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBottomMenu.setOnClickListener(view1 ->
                navController.navigate(R.id.action_HomeFragment_to_bottomMenuActivity));

        btnTabs.setOnClickListener(v ->
                navController.navigate(R.id.action_HomeFragment_to_tabsActivity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}