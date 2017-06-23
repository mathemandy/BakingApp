package com.ng.tselebro.bakingapp.recipeDetails;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.POJO.Ingredient;
import com.ng.tselebro.bakingapp.Model.POJO.Recipe;
import com.ng.tselebro.bakingapp.Model.POJO.Step;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipeDetails.ViewHolders.ingredient_ViewHolder;
import com.ng.tselebro.bakingapp.recipeDetails.ViewHolders.steps_viewHolders;
import com.ng.tselebro.bakingapp.recipeDetails.recipeISAdapter.recipeIs;
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.models.ExpandableListPosition;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class recipeISAdapter  extends MultiTypeExpandableRecyclerViewAdapter<recipeIs, ChildViewHolder> {

    public static final int STEP_VIEW_TYPE = 3;
    public static final int INGREDIENT_VIEW_TYPE = 4;


    public recipeISAdapter(List<ExpandableDetails> list) {
        super(list);
    }


    @Override
    public recipeIs onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_spes_ingredient, parent, false);
        return  new recipeIs(view);
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case INGREDIENT_VIEW_TYPE:
                View ingredient = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_item, parent, false);
                return  new ingredient_ViewHolder(ingredient);
            case STEP_VIEW_TYPE:
                View steps =
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.steps_item, parent, false);
                return new steps_viewHolders(steps);
            default: throw  new IllegalArgumentException("Invalid viewType");
        }
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        int viewType = getItemViewType(flatPosition);

        Recipe recipe = ((ExpandableDetails) group).getItems().get(childIndex);
        List<Ingredient> ingredientList = recipe.getIngredients();
        Ingredient ingredient = ingredientList.get(childIndex);
        List<Step> stepsList = recipe.getSteps();
        Step steps = stepsList.get(childIndex);


        switch (viewType) {
            case INGREDIENT_VIEW_TYPE:
                ((ingredient_ViewHolder) holder).setIngredient_measure(ingredient.getMeasure());
                ((ingredient_ViewHolder) holder).setIngredient_name(ingredient.getIngredient());
                ((ingredient_ViewHolder) holder).setIngredient_quantity(String.valueOf(ingredient.getQuantity()));
            break;

            case STEP_VIEW_TYPE:
                ((steps_viewHolders) holder).setSteps(steps.getShortDescription());
        }


    }

    @Override
    public void onBindGroupViewHolder(recipeIs holder, int flatPosition, ExpandableGroup group) {
        holder.setRecipeStepsOringredients(group);
    }

    @Override
    public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
         switch (group.getTitle()){
             case "Steps" :
                 return STEP_VIEW_TYPE;
             default:
                 return INGREDIENT_VIEW_TYPE;
         }
    }

    @Override
    public boolean isGroup(int viewType) {
        return  viewType == ExpandableListPosition.GROUP;
    }

    @Override
    public boolean isChild(int viewType) {
        return viewType == STEP_VIEW_TYPE || viewType == INGREDIENT_VIEW_TYPE;
    }

    public  class recipeIs extends GroupViewHolder {

    @BindView(R.id.list_item_step_name)
    TextView steps;

    @BindView(R.id.list_item_step_arrow)
    ImageView arrow;


    public recipeIs(View itemView) {
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

    public  void  setRecipeStepsOringredients (ExpandableGroup recipe){
        if (recipe instanceof  ExpandableDetails){
            steps.setText(recipe.getTitle());
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
