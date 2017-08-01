package com.ng.tselebro.bakingapp.recipeDetails.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.ng.tselebro.bakingapp.Model.Step;
import com.ng.tselebro.bakingapp.R;
import com.ng.tselebro.bakingapp.recipeDetails.ExpandableDetailsSteps;
import com.ng.tselebro.bakingapp.recipeDetails.adapters.recipeStepAdapter.StepViewHolder;
import com.ng.tselebro.bakingapp.recipeDetails.fragments.MasterListStepFragment;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class recipeStepAdapter extends
        ExpandableRecyclerViewAdapter<StepViewHolder, recipeStepAdapter.steps_viewHolders> {

    private final MasterListStepFragment.RecipeStepItemListener mItemListener;
    private List<Step> mStep;


    public recipeStepAdapter(List<? extends ExpandableGroup> groups, MasterListStepFragment.RecipeStepItemListener recipeStepItemListener) {
        super(groups);
        mItemListener = recipeStepItemListener;
        notifyDataSetChanged();
    }

    @Override
    public StepViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.heading_steps, parent, false);

        return new StepViewHolder(view);
    }

    @Override
    public steps_viewHolders onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.steps_item, parent, false);
        return  new steps_viewHolders(view, mItemListener);
    }


    @Override
    public void onBindChildViewHolder(steps_viewHolders holder, int flatPosition, ExpandableGroup group, int childIndex) {
        mStep = ((ExpandableDetailsSteps) group).getItems();
        setList(mStep);
        Step step = mStep.get(childIndex);
        holder.setSteps(step.getShortDescription());
    }

    @Override
    public void onBindGroupViewHolder(StepViewHolder holder, int flatPosition, ExpandableGroup group) {
            holder.setStepsTitle(group);
    }

    private Step getItem(int position){
        return mStep.get(position);
    }


    private void setList(List<Step> list) {
        this.mStep = list;
    }


    class StepViewHolder extends GroupViewHolder {

        @BindView(R.id.list_item_step_name)
        TextView steps;
        @BindView(R.id.list_item_step_arrow)
        ImageView arrow;


        public StepViewHolder(View itemView) {
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

        public  void  setStepsTitle (ExpandableGroup step){
            if (step instanceof ExpandableDetailsSteps){
                steps.setText(step.getTitle());
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

    class steps_viewHolders extends ChildViewHolder implements View.OnClickListener {
        @BindView(R.id.steps_texview)
        TextView steps;

        private final MasterListStepFragment.RecipeStepItemListener mItemListener;

        public steps_viewHolders(View itemView, MasterListStepFragment.RecipeStepItemListener listener) {
            super(itemView);
            mItemListener = listener;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        public  void setSteps(String name){
            steps.setText(name);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition()-1;
            Step step = getItem(position);
            mItemListener.onStepClick(step, position);
        }

    }


}
