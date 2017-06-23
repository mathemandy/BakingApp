package com.ng.tselebro.bakingapp.recipeDetails.ViewHolders;


import android.view.View;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.R;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class steps_viewHolders extends ChildViewHolder {
    @BindView(R.id.steps_texview)
    TextView steps;

    public steps_viewHolders(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public  void setSteps(String name){
        steps.setText(name);

    }
}
