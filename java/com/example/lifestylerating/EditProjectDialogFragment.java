package com.example.lifestylerating;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by sohrab on 04.02.2016.
 */
public class EditProjectDialogFragment extends DialogFragment {

    private final String TAG = EditProjectDialogFragment.class.getSimpleName();
    public String MaxToAdd;
    public String countDescription;
    private EditDialogListener dialogListener;
    TextView progressCountTextView;
    TextView MaximumTextView;
    String T1,T2;
    int counter, Max;
    EditText etTermToAdd ;
    EditText etDescription;

    /*public void EditProjectDialogFragment(String T1, String T2, int counter, int Max){
         this.T1=T1;
        this.T2=T2;
        this.counter=counter;
        this.Max=Max;

    }*/
    static EditProjectDialogFragment newInstance(String T1, String T2, int count, int M) {
        EditProjectDialogFragment f = new EditProjectDialogFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("Maximum", M);
        args.putInt("con", count);
        args.putString("t1", T1);
        args.putString("t2", T2);
        f.setArguments(args);

        return f;
    }
    public interface EditDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (EditDialogListener) activity;
        } catch(Exception e) {
            throw new ClassCastException(activity.getClass()
                    .getName() + " muss das Interface " +
                    "\"EditProjectDialogFragment.EditDialogListener\""
                    + " implementieren!");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Max = getArguments().getInt("Maximum");
        counter = getArguments().getInt("con");
        T1 = getArguments().getString("t1");
        T2 = getArguments().getString("t2");
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        LayoutInflater inflater =getActivity().getLayoutInflater();
        final View sub_layout = inflater.inflate(R.layout.edit_project_dialog, null);
        progressCountTextView=(TextView) sub_layout.findViewById(R.id.tv_name_project_dialog);
        progressCountTextView.setText(T2);
        MaximumTextView=(TextView) sub_layout.findViewById(R.id.tv_desc_project_dialog);
        MaximumTextView.setText(T1);
        etTermToAdd = (EditText) sub_layout.findViewById(R.id.et_name_project_dialog);
        etDescription = (EditText) sub_layout.findViewById(R.id.et_desc_project_dialog);
        etTermToAdd.setText(Integer.toString(Max));
        etTermToAdd.selectAll();
        /*etTermToAdd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View myView, MotionEvent event) {
                etTermToAdd.selectAll();
                return true;
            }
        });*/
        etDescription.setText(Integer.toString(counter));
        /*etDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View myView, MotionEvent event) {
                etDescription.selectAll();
                return true;
            }
        });*/
        builder.setView(sub_layout)
                .setTitle("Setting")
                .setPositiveButton("OK",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {

                        EditProjectDialogFragment.this.MaxToAdd =
                                etTermToAdd.getText().toString().trim();
                        EditProjectDialogFragment.this.countDescription =
                                etDescription.getText().toString().trim();
                        Log.d(TAG, "Dialog \"edit_project\" durchlaufen");
                        dialogListener.onDialogPositiveClick(
                                EditProjectDialogFragment.this);
                    }
                })
                .setNegativeButton("CANSEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialogListener.onDialogNegativeClick(
                                        EditProjectDialogFragment.this);
                                Log.d(TAG, "Dialog \"edit_project\" abgebrochen");
                            }
                        });
        return builder.create();
    }

}