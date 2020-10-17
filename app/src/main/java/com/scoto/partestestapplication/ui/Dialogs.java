package com.scoto.partestestapplication.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.scoto.partestestapplication.R;
import com.scoto.partestestapplication.utils.OperationTypeDef;


public class Dialogs extends DialogFragment {
    public static final String TAG = "Dialogs";
    private TextView msg;
    private ImageView contentOfContainer;
    private ImageView container;


//    @Retention(RetentionPolicy.SOURCE)
//    @IntDef({OperationTypeDef.SUCCESS, OperationTypeDef.FAILURE, OperationTypeDef.UPDATE, OperationTypeDef.EMPTY_FIELD})
//    public @interface OperationTypeDef {
//        int SUCCESS = 1;
//        int FAILURE = 0;
//        int UPDATE = 2;
//        int EMPTY_FIELD = -1;
//    }


    public Dialogs() {

        //Empty Constructor is required for Dialog Fragment
        //Make sure not to add arguments to the constructor
        //Use 'newInstance' instead shown below

    }

    public static Dialogs newInstance(String msg, @OperationTypeDef int OPERATION_CODE) {

        Bundle args = new Bundle();
        Dialogs fragment = new Dialogs();
        args.putInt("operation_code", OPERATION_CODE);
        args.putString("message", msg);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Get screen width,height and set the dialog to full screen.
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);

        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TO set full screen dialog, used custom Style.
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        msg = view.findViewById(R.id.msg);
        container = view.findViewById(R.id.container);
        contentOfContainer = view.findViewById(R.id.contentOfContainer);

        if (getArguments().getString("message") != null) {
            String message = getArguments().getString("message");
            if (message.length() > 0) {
                msg.setText(message);
            }

        }

        int code = getArguments().getInt("operation_code");

        switch (code) {
            case OperationTypeDef.FAILURE:
                contentOfContainer.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.close1));
                break;
            case OperationTypeDef.UPDATE:
                contentOfContainer.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.refresh1));
                break;
            case OperationTypeDef.EMPTY_FIELD:
                contentOfContainer.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.empty));
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) contentOfContainer.getLayoutParams();
                params.setMargins(0, 0, 0, 0);
                msg.setTextSize(18);
                break;
            case OperationTypeDef.SUCCESS:
                contentOfContainer.setImageDrawable(ContextCompat.getDrawable(getContext(), R.mipmap.tick1));
                break;
            default:
                break;
        }


    }


}
