package com.example.myapplication.bean;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.R;

public class HolderBrand extends RecyclerView.ViewHolder {
    private TextView id;
    private TextView name;
    private TextView description;
    private TextView time;

    public HolderBrand(View itemView) {
        super(itemView);
        bindIds();
    }

    private void bindIds() {
        id = (TextView) itemView.findViewById(R.id.id);
        name = (TextView) itemView.findViewById(R.id.name);
        description = (TextView) itemView.findViewById(R.id.description);
        time = (TextView) itemView.findViewById(R.id.time);
    }

    public void setData(BeanBrand brand, boolean visibility) {
        itemView.setVisibility(visibility ? View.VISIBLE : View.GONE);
        if (!visibility)
            return;
        id.setText(brand.getId() == 0 ? "" : brand.getId() + "");
        name.setText(brand.getName());
        description.setText(brand.getDescription());
        time.setText(brand.getCreatedAt());
    }
}
