package com.example.nofoodsharingproject.fragments.getter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nofoodsharingproject.R;

import java.util.ArrayList;

public class GetterNotifyF extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_getter_notify, container, false);


        RecyclerView rv = view.findViewById(R.id.recycler); /*
        DataBaseHandler bd = new DataBaseHandler(this);
        ArrayList<Products> productsList = (ArrayList<Products>) bd.getAllProd();
        ProductRecyclerAdapter adapter = new ProductRecyclerAdapter(RootActivity.this, productsList);
        rv.setAdapter(adapter);
        какая надо разобраться*/

        return view;
    }
}