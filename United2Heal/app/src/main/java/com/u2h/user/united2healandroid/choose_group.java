package com.u2h.user.united2healandroid;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import org.w3c.dom.Text;

public class choose_group extends Fragment {
String[] groupList={"","A","B","C","D","E","F","G","H","I","J","K","L"};
Spinner groupSpinner;
DrawerLayout drawer;
Toolbar toolbar;
NavigationView navigationView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_choose_group,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        drawer=(DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        toolbar=(Toolbar) getActivity().findViewById(R.id.toolbar);

        super.onViewCreated(view, savedInstanceState);
        Button submit= (Button) view.findViewById(R.id.submitGroupButton);
        navigationView=(NavigationView) getActivity().findViewById(R.id.nav_view);
        int count=navigationView.getHeaderCount();
        final View headerView= navigationView.getHeaderView(0);
        final TextView headerTextView= headerView.findViewById(R.id.headerTextView);
        submit.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((UserInfo)getActivity().getApplication()).getGroupName().equals(""))
                {
                    Toast.makeText(getActivity().getApplication(),"Please pick a group!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getActivity().getApplication(),"You are now in Group "+((UserInfo)getActivity().getApplication()).getGroupName(),Toast.LENGTH_SHORT).show();

                    FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
                    Fragment fragment= new SearchItems();

                    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                            getActivity(), drawer,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
                    drawer.addDrawerListener(toggle);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    headerTextView.setText("Group "+((UserInfo)getActivity().getApplication()).getGroupName());
                    toggle.syncState();
                    toggle.setDrawerIndicatorEnabled(true);
                    fragmentTransaction.replace(R.id.MainFrame,fragment);
                    fragmentTransaction.commit();
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Search Items");

                }
            }
        });
        groupSpinner=(Spinner) view.findViewById(R.id.groupSpinner);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getContext(),R.layout.spinner_item,groupList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown_resource);
        groupSpinner.setAdapter(arrayAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((UserInfo)getActivity().getApplication()).setGroupName(groupSpinner.getAdapter().getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


}
