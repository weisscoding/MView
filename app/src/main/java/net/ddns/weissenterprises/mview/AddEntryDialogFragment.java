package net.ddns.weissenterprises.mview;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

public class AddEntryDialogFragment extends DialogFragment {
    public interface AddEntryDialogFragmentListener {
        public boolean onDialogClick(String val, int factor);
    }


    AddEntryDialogFragmentListener mListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ViewGroup viewGroup = (ViewGroup) inflater.inflate(R.layout.dialog_add_entry, null);

        builder.setView(viewGroup);

        builder.setPositiveButton("+", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

                mListener.onDialogClick(((TextView)viewGroup.findViewById(R.id.dialogEntryVal)).getText().toString(), 1);
            }
        });
        builder.setNegativeButton("-", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int id){

                mListener.onDialogClick(((TextView)viewGroup.findViewById(R.id.dialogEntryVal)).getText().toString(), -1);
            }
        });

        return builder.create();
    }


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            mListener = (AddEntryDialogFragmentListener)activity;
        } catch (ClassCastException e){
            throw new ClassCastException(activity.toString()
            + " must implement AddEntryDialogFragmentListener");
        }
    }
}
