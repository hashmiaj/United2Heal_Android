package com.u2h.user.united2healandroid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class ChooseSchool extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.choose_school,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final UserInfo application=((UserInfo)getActivity().getApplication());

        ArrayList<String> rolesList = new ArrayList<>();
        rolesList.add("Volunteer");
        rolesList.add("Admin");

        TextView pageTitle = (TextView)view.findViewById(R.id.pageTitle);
        ImageView logoVCU=(ImageView)view.findViewById(R.id.imageViewVCU);
        final Spinner roleSpinner = (Spinner)view.findViewById(R.id.roleSpinner);

        logoVCU.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.setSchoolName("VCU");
                gotoPasswordPage();
            }
        });
        ImageView logoGMU=(ImageView)view.findViewById(R.id.imageViewGMU);
        logoGMU.setOnClickListener(new ImageView.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.setSchoolName("GMU");
                gotoPasswordPage();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item, rolesList);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_layout_dropdown_resource);

        roleSpinner.setAdapter(arrayAdapter);
        roleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((UserInfo) getActivity().getApplication()).setRole(roleSpinner.getAdapter().getItem(i).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void gotoPasswordPage(){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment=new PasswordPage();
        fragmentTransaction.replace(R.id.MainFrame,fragment);
        fragmentTransaction.commit();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Enter Password");

    }
}
