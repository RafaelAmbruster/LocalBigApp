package com.app.localbig.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.app.localbig.R;
import com.app.localbig.helper.xml.XmlParserManagerApplicationCategory;
import com.app.localbig.model.ApplicationCategory;

import java.io.IOException;
import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private View view;
    private ArrayList<ApplicationCategory> categories;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, null);
        setHasOptionsMenu(true);
        return view;
    }

    private void LoadCategories() {
        try {
            categories = new XmlParserManagerApplicationCategory().Parse(getActivity().getAssets().open("data/Home.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(final Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

}
