package com.phonemedia;


import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private ListView songsList;
    private String[] items;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        songsList = view.findViewById(R.id.song_list);

        ArrayList<File> mySongs = findSongs(Environment.getExternalStorageDirectory());
        items = new String[mySongs.size()];
        for ( int i = 0; i<mySongs.size(); i++) {

            items[i] = mySongs.get(i).getName().toString()
                    .replace(".mp3","").replace(".wav","");
            Toast.makeText(getContext(), ""+mySongs.get(i).getName().toString(), Toast.LENGTH_SHORT).show();

        }
        ArrayAdapter <String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, items);
        songsList.setAdapter(adapter);
        songsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), MusicActivity.class);
                startActivity(intent);
            }
        });
    }

    private ArrayList<File> findSongs(File fileRoot) {
        ArrayList<File> allFiles = new ArrayList<>();
        File[] files = fileRoot.listFiles();
        for (File singeFile : files) {
            if (singeFile.isDirectory() && !singeFile.isHidden()) {
                allFiles.addAll(findSongs(singeFile));
            } else {
                if (singeFile.getName().endsWith(".mp3") || singeFile.getName().endsWith(".wav")) {
                    allFiles.add(singeFile);
                }
            }
        }
        return allFiles;
    };
}
