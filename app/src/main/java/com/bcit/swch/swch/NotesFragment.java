package com.bcit.swch.swch;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.burt.swch_planner.R;

/**
 * Created by Burt on 2017-11-26.
 */

public class NotesFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View myView;
        myView = inflater.inflate(R.layout.notes_layout, container, false);
        return myView;

    }
}
