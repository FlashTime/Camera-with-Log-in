package com.example.danijel.execomhackathon.FragmentsUlogovan;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.danijel.execomhackathon.ImageAdapter;
import com.example.danijel.execomhackathon.OnlyMonumentActivity;
import com.example.danijel.execomhackathon.R;
import com.example.danijel.execomhackathon.db.DatabaseHelper;
import com.example.danijel.execomhackathon.db.Monuments;
import com.example.danijel.execomhackathon.db.User;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;


public class MyFragment extends Fragment {

    private GridView gridview;

    public MyFragment() {
        // Required empty public constructor
    }
    public static MyFragment newInstance() {
        return new MyFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my, container, false);

        DatabaseHelper dbHelper = OpenHelperManager.getHelper(getActivity(), DatabaseHelper.class);
        RuntimeExceptionDao<Monuments, Integer> monDao = dbHelper.getMonumentsRuntimeDao();
        RuntimeExceptionDao<User, Integer> usersDao = dbHelper.getUserRuntimeDao();
        List<Monuments> mon = null;
        try {
            mon = monDao.queryForEq("user", usersDao.queryForEq("is", 1).get(0).getEmail());

            gridview = (GridView) view.findViewById(R.id.gridviewimg);
            gridview.setAdapter(new ImageAdapter(getActivity(), mon));

            gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View v,
                                        int position, long id) {
                    Intent i = new Intent(getActivity(), OnlyMonumentActivity.class);
                    i.putExtra("Idmonument",position);
                    startActivity(i);
                }
            });

        }catch (Exception e){
            Log.i("nesto uredu nije","@@@@@@@@@@@@@@@@@@");
        }

        return view;
    }
}
