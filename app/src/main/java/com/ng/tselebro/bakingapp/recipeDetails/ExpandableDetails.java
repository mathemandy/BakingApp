package com.ng.tselebro.bakingapp.recipeDetails;

import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class ExpandableDetails extends ExpandableGroup<Recipe> {

    public ExpandableDetails(String title, List<Recipe> items) {
        super(title, items);
    }
}
