package com.muhammed.citylabadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.muhammed.citylabadmin.helper.MyPreference;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NavController navController = Navigation.findNavController(this, R.id.main_nav_graph);
//        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main_nav_graph);
//        if(MyPreference.GetdataAdmin()!=null)
//         {
//            navGraph.setStartDestination(R.id.homeAdminScreen);
//        } else {
//            navGraph.setStartDestination(R.id.loginScreen);
//        }
//        navController.setGraph(navGraph);
    }
}