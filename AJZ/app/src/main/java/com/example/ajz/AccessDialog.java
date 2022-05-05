package com.example.ajz;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class AccessDialog extends AppCompatDialogFragment {
    private EditText editAdminPassword;
    private AccessDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // "inflates" admin_input.xml into a view
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View adminVerificationView = inflater.inflate(R.layout.admin_input, null);

        DialogInterface.OnClickListener clickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String accessCode = editAdminPassword.getText().toString();
                listener.verifyAdminAccess(accessCode);
            }
        };

        // setting the alertdialog builder properties
        builder.setView(adminVerificationView);
        builder.setCancelable(true);
        builder.setTitle("Enter Admin Access Code");
        builder.setPositiveButton("Confirm", clickListener);

        editAdminPassword = adminVerificationView.findViewById(R.id.AdminPasswordEdit);

        return builder.create();
    }

    //runs before onCreateDialog
    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            listener = (AccessDialogListener) context;
        } catch (Exception e) {
            throw new ClassCastException(context.toString() + " must implement AccessDialogListener");
        }
    }

    // Initialize functions needed in alert
    public interface AccessDialogListener {
        void verifyAdminAccess(String accessCode);
    }
}
