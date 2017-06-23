package com.ng.tselebro.bakingapp.recipeDetails.ViewHolders;

import android.view.View;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mathemandy on 20 Jun 2017.
 */

public class ingredient_ViewHolder extends ChildViewHolder {
    @BindView(R.id.ingredient_name)
    TextView ingredient_name;
    @BindView (R.id.ingredient_measure)
    TextView ingredient_measure;
    @BindView(R.id.ingredient_quantity)
    TextView ingredient_quantity;


    public ingredient_ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void setIngredient_name(String name) {
        ingredient_name.setText(name);
    }

    public void setIngredient_measure (String measure){
        ingredient_measure.setText(measure);
    }

    public void setIngredient_quantity (String quantity){
        ingredient_quantity.setText(quantity);
    }


}
