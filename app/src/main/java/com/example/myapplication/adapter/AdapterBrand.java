package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication.R;
import com.example.myapplication.bean.BeanBrand;
import com.example.myapplication.bean.HolderBrand;

import java.util.ArrayList;


public class AdapterBrand extends android.support.v7.widget.RecyclerView.Adapter<HolderBrand> {

    private ArrayList<BeanBrand> brands;

    public AdapterBrand(ArrayList<BeanBrand> brands) {
        this.brands = brands;
    }

    @Override
    public HolderBrand onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HolderBrand(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_brand, parent, false));
    }

    @Override
    public void onBindViewHolder(HolderBrand holder, int position) {
        if (position != brands.size())
            holder.setData(brands.get(position), true);
        else
            holder.setData(null, false);
    }

    @Override
    public int getItemCount() {
        return brands == null ? 0 : brands.size() + 1;
    }

    public void setBrands(ArrayList<BeanBrand> brands) {
        this.brands = brands;
        notifyDataSetChanged();
    }
}
