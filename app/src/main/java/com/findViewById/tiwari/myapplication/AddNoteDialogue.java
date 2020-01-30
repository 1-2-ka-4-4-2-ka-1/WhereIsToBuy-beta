package com.findViewById.tiwari.myapplication;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("ValidFragment")
public class AddNoteDialogue extends AppCompatDialogFragment {

    private EditText note;


    private String mNote ;
    private int pos;


    private DialogueListener listener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener= (DialogueListener) context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    @SuppressLint("ValidFragment")
    public AddNoteDialogue(String note, int pos){
        this.mNote = note;
        this.pos = pos;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Dialog builder = new Dialog(getActivity(), R.style.CustomDialog);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View view = layoutInflater.inflate(R.layout.add_edit_user_dialogue,null);

        note = view.findViewById(R.id.ed_new_note);




        note.setText(mNote);




        Button cancel= view.findViewById(R.id.btn_cancel_dialogue);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        Button ok = view.findViewById(R.id.btn_ok_dialogue);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getTag().equals("Add Note")){
                    if(note.getText().toString().trim().equals("")){

                        Toast.makeText(getContext(),"Can not be empty",Toast.LENGTH_LONG).show();

                    }else {
                        listener.addNoteDialogue(note.getText().toString());
                        builder.dismiss();
                    }}


            }
        });
        builder.setContentView(view);


        return builder;
    }



    public interface DialogueListener{
        void addNoteDialogue(String note);
    }

}
