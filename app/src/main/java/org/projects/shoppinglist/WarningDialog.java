package org.projects.shoppinglist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by leon on 14-03-2016.
 */
public class WarningDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder alert = new AlertDialog.Builder(
                getActivity());
        alert.setTitle(getResources().getString(R.string.delete_all_lines));
        alert.setMessage(getResources().getString(R.string.are_you_sure));
        alert.setPositiveButton(getResources().getString(R.string.yes), pListener);
        alert.setNegativeButton(getResources().getString(R.string.no), nListener);

        return alert.create();

    }
    DialogInterface.OnClickListener pListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            positiveClick();
        }

    };
    DialogInterface.OnClickListener nListener = new DialogInterface.OnClickListener()
    {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            negativeClick();
        }
    };
    protected void positiveClick()
    {

    }
    protected void negativeClick()
    {

    }

}
