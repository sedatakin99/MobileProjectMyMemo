package com.example.mymemo;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class VerificationDialog extends DialogFragment {

    private static final String TAG = "Verification Dialog";

    public interface PasswordVerification{
        void sendInput(String input);
    }
    public PasswordVerification passwordVerification;

    private EditText passwordInput;
    private TextView actionOkey, actionCancel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_verification_layout, container, false);
        actionCancel = view.findViewById(R.id.action_cancel);
        actionOkey = view.findViewById(R.id.action_ok);
        passwordInput = view.findViewById(R.id.passwordVerification);

        actionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        actionOkey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = passwordInput.getText().toString();
                passwordVerification.sendInput(input);
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            passwordVerification = (PasswordVerification) getActivity();
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement Verification Dialog");
        }
    }
}
