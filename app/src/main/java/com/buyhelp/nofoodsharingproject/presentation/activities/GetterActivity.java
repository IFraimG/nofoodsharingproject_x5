package com.buyhelp.nofoodsharingproject.presentation.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.buyhelp.nofoodsharingproject.R;
import com.buyhelp.nofoodsharingproject.domain.helpers.PermissionHandler;
import com.buyhelp.nofoodsharingproject.databinding.ActivityGetterBinding;


public class GetterActivity extends AppCompatActivity {
    private NavController navController;
    private ActivityGetterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGetterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PermissionHandler.requestCalendarPermissions(this);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_getter_fragment);
        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.getterNavigation, navController);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        binding = null;
    }

    public void setBottomNavigationVisibility(boolean isVisible) {
        if (isVisible) binding.getterNavigation.setVisibility(View.VISIBLE);
        else binding.getterNavigation.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }
}
