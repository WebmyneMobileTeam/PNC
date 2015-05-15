package com.example.android.parvarish_nutricalculator.helpers;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.android.parvarish_nutricalculator.R;

/**
 * Simple view holder for a single text view.
 */
class IngredientViewHolder extends RecyclerView.ViewHolder {

    private TextView mTextView;

    IngredientViewHolder(View view) {
        super(view);

        mTextView = (TextView) view.findViewById(R.id.text);
    }

    public void bindItem(String text) {
        mTextView.setText(text);
    }

    @Override
    public String toString() {
        return mTextView.getText().toString();
    }
}
