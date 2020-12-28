package com.example.recipeapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.recipeapp.fragment.StepsSelectFragement;
import com.example.recipeapp.utils.Ingredient;
import com.example.recipeapp.utils.Recipe;
import com.example.recipeapp.utils.Step;

import java.util.List;

public class RecipeDetails extends AppCompatActivity {
    private Recipe recipe;
    private List<Ingredient> ingredientList;
    private List<Step> stepList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        Intent intent = getIntent();

        if (intent.getExtras() != null) {
            stepList = null;
            recipe = intent.getParcelableExtra("recipe");
            ingredientList = recipe.getIngredientList();
            stepList = recipe.getStepList();

            StepsSelectFragement stepsSelectFragement = new StepsSelectFragement();
            stepsSelectFragement.setIngredientList(ingredientList);
            stepsSelectFragement.setStepList(stepList);
            if (findViewById(R.id.recipe_details_step_viewer) != null) {
                stepsSelectFragement.setTablet(true);
            }


            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().add(R.id.recipe_details_holder, stepsSelectFragement).commit();
        }
    }
}
