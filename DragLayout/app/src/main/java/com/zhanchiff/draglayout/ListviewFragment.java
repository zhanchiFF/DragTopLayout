package com.zhanchiff.draglayout;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ListviewFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list, container, false);
        ListView mListView = (ListView) view.findViewById(R.id.list_view);

        List<String> datas = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            datas.add("Listview -> " + i);
        }
        mListView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.id_info, datas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                return super.getView(position, convertView, parent);
            }
        });
        mListView.setOnItemClickListener(mItemClickListener);

        return view;
    }

    private AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Toast.makeText(getActivity(),"点击 "+position,Toast.LENGTH_SHORT).show();
        }
    };


}
