package io.darkbytes.blogapp.util;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

import io.darkbytes.blogapp.R;

public class ChipUtil {
    public static void createChips(List<String> tags, Context context, ChipGroup chipGroup) {
        if (tags == null)
            return;

        chipGroup.removeAllViews();
        tags.stream().forEach(tag -> {
            LayoutInflater inflater = LayoutInflater.from(context);
            Chip chip = (Chip) inflater.inflate(R.layout.post_tag, null, false);
            chip.setText(tag);
            chipGroup.addView(chip);
        });
    }
}
