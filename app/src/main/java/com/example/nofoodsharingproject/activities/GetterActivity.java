package com.example.nofoodsharingproject.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import com.example.nofoodsharingproject.R;
import com.example.nofoodsharingproject.databinding.ActivityGetterBinding;
import com.example.nofoodsharingproject.utils.PermissionHandler;

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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        NavigationUI.onNavDestinationSelected(item, navController);
        return super.onOptionsItemSelected(item);
    }
}
