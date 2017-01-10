package com.example.redfoxoptimaiii.farmaide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by REDFOX™ OptimaIII on 1/10/2017.
 */

public class AdminUserTab extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.admin_user_tab, container, false);
        return rootView;
    }
}
