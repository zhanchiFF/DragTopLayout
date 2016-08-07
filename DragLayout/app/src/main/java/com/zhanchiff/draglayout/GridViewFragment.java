package com.zhanchiff.draglayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

public class GridViewFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_gridview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        GridView gridView = (GridView) getView().findViewById(R.id.list_view);
        ArrayList<String> mDatas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            mDatas.add("GridView -> " + i);
        }
        gridView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.id_info, mDatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        });
    }


}
