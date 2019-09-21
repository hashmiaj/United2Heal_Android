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
import android.widget.ImageView;

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
        ImageView logoVCU=(ImageView)view.findViewById(R.id.imageViewVCU);
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

    }
    public void gotoPasswordPage(){
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        Fragment fragment=new PasswordPage();
        fragmentTransaction.replace(R.id.MainFrame,fragment);
        fragmentTransaction.commit();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Enter Password");

    }
}
