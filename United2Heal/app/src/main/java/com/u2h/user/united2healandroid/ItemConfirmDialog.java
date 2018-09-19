package com.u2h.user.united2healandroid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class ItemConfirmDialog extends DialogFragment {
public interface ConfirmDialogListener{
    public void onConfirm();
}
    ConfirmDialogListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater= getActivity().getLayoutInflater();
        final View view= inflater.inflate(R.layout.item_confrim_dialog,null);
        TextView itemName=(TextView) view.findViewById(R.id.itemNameTextView);
        TextView itemQuantity=(TextView) view.findViewById(R.id.itemQuantityTextView);
        TextView itemBox=(TextView) view.findViewById(R.id.boxNumberTextView);
        TextView expirationDate=(TextView) view.findViewById(R.id.expirationTextView);
        itemName.setText(getArguments().getString("ITEM_NAME"));
        itemQuantity.setText(getArguments().getString("ITEM_QUANTITY"));
        itemBox.setText(getArguments().getString("BOX_NUMBER"));
        expirationDate.setText(getArguments().getString("EXPIRATION"));
        builder.setView(view).setTitle("Confirm Submit").setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
mListener.onConfirm();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        mListener=(ConfirmDialogListener) context;
        super.onAttach(context);
    }
}
