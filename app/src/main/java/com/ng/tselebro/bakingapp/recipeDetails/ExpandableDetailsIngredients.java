package com.ng.tselebro.bakingapp.recipeDetails;

import com.ng.tselebro.bakingapp.Model.Ingredient;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class ExpandableDetailsIngredients extends ExpandableGroup<Ingredient> {

    public ExpandableDetailsIngredients(String title, List<Ingredient> items) {
        super(title, items);
    }
}
