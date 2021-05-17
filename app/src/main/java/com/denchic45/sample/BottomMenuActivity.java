package com.denchic45.sample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.denchic45.appbarcontroller.AppBarController;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BottomMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_menu);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bnv);
        AppBarLayout appBarLayout = findViewById(R.id.appbar);
        AppBarController appBarController = AppBarController.create(this, appBarLayout);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);

        appBarController.addView(LayoutInflater.from(this).inflate(R.layout.custom_view, null, false),
                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        appBarController.addView(LayoutInflater.from(this).inflate(R.layout.custom_view2, null, false),
                AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP | AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> appBarController.showToolbar());

        Button btnSmallList = findViewById(R.id.btn_small_list);
        btnSmallList.setOnClickListener(v -> {
            CellFragment primaryNavigationFragment = (CellFragment) fragment.getChildFragmentManager().getPrimaryNavigationFragment();
            primaryNavigationFragment.putList(new String[]{"sdf","dfgfgdfg"});
        });

        Button btnBigList = findViewById(R.id.btn_big_list);
        btnBigList.setOnClickListener(v -> {
            CellFragment primaryNavigationFragment = (CellFragment) fragment.getChildFragmentManager().getPrimaryNavigationFragment();
            primaryNavigationFragment.putList(new String[]{
                    "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька", "Томасина", "Кристина", "Пушок", "Дымка", "Кузя",
                    "Китти", "Масяня", "Симба", "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
                    "Томасина", "Кристина", "Пушок", "Дымка", "Кузя", "Китти", "Масяня", "Симба"});
        });
    }
}