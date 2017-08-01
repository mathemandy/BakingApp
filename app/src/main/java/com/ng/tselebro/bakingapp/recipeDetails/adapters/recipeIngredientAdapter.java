package com.ng.tselebro.bakingapp.recipeDetails.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.Ingredient;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipeDetails.ExpandableDetailsIngredients;
import com.ng.tselebro.bakingapp.recipeDetails.ViewHolders.ingredient_ViewHolder;
import com.ng.tselebro.bakingapp.recipeDetails.adapters.recipeIngredientAdapter.recipeIngredient;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class recipeIngredientAdapter extends
        ExpandableRecyclerViewAdapter<recipeIngredient, ingredient_ViewHolder> {


    public recipeIngredientAdapter(List<? extends ExpandableGroup> groups) {
        super(groups);
    }

    @Override
    public recipeIngredient onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.heading_ingredient, parent, false);
        return  new recipeIngredient(view);

    }

    @Override
    public ingredient_ViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_item, parent, false);
        return  new ingredient_ViewHolder(view);
    }
    @Override
    public void onBindChildViewHolder(ingredient_ViewHolder holder, int flatPosition,
                                      ExpandableGroup group, int childIndex) {
        final Ingredient ingredient = ((ExpandableDetailsIngredients) group).getItems().get(childIndex);

        holder.setIngredient_name(ingredient.getIngredient());
        holder.setIngredient_quantity(String.valueOf(ingredient.getQuantity()));
        holder.setIngredient_measure(ingredient.getMeasure());
    }


    @Override
    public void onBindGroupViewHolder(recipeIngredient holder, int flatPosition, ExpandableGroup group) {
            holder.setRecipeStepsOringredients(group);
    }

    public  class recipeIngredient extends GroupViewHolder {

    @BindView(R.id.list_item_step_name)
    TextView ingredient;

    @BindView(R.id.list_item_step_arrow)
    ImageView arrow;


    public recipeIngredient(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
    @Override
    public void expand() {
        animateExpand();
    }
    @Override
    public void collapse() {
        animateCollapse();
    }


    public  void  setRecipeStepsOringredients (ExpandableGroup ingredient){
        if (ingredient instanceof ExpandableDetailsIngredients){
            this.ingredient.setText(ingredient.getTitle());
        }

    }
    private void animateExpand(){
        RotateAnimation rotate =
                new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);

    }
    private void animateCollapse() {
        RotateAnimation rotate =
                new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }



}
}
