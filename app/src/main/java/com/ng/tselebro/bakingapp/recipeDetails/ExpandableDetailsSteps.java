package com.ng.tselebro.bakingapp.recipeDetails;

import com.ng.tselebro.bakingapp.Model.Step;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;


public class ExpandableDetailsSteps extends ExpandableGroup<Step> {
    public ExpandableDetailsSteps(String title, List<Step> items) {
        super(title, items);
    }
}
