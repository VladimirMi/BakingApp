package io.github.vladimirmi.bakingapp.data.entity;

import android.annotation.SuppressLint;

/**
 * Ingredient model.
 */

@SuppressWarnings("unused")
public class Ingredient {

    private float quantity;
    private String measure;
    private String ingredient;

    @SuppressLint("DefaultLocale")
    public String getQuantity() {
        if (quantity % 1 == 0) {
            return String.format("%.0f %s", quantity, measure);
        } else {
            return String.format("%.1f %s", quantity, measure);
        }
    }

    public String getIngredient() {
        return ingredient;
    }

}
