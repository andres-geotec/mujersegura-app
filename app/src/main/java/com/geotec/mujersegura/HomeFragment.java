package com.geotec.mujersegura;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.services.AlertService;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private Button btnStopAlert;

    private Complements C;

    private HomeFragmentListener homeFragmentListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        iniComponents();
        C = new Complements(getContext());

        return mView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_fragment_home_stop_alert:
                getContext().stopService(new Intent(getContext(), AlertService.class));
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //swtActiveBackgroundWPB.setChecked(homeFragmentListener.getActiveBackgroundWPB());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        /*if (context instanceof HomeFragmentListener) {
            homeFragmentListener = (HomeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement HomeFragmentListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        homeFragmentListener = null;
    }

    private void iniComponents() {
        btnStopAlert = (Button) mView.findViewById(R.id.btn_fragment_home_stop_alert);

        btnStopAlert.setOnClickListener(this);
    }

    private void setActivacionBackgroundWPB() {
        if (homeFragmentListener.getActiveBackgroundWPB()) {
            printToast("Desactivando acción...", Toast.LENGTH_SHORT);
            homeFragmentListener.setActiveBackgroundWPB(false);
        } else {
            printToast("Activando acción...", Toast.LENGTH_SHORT);
            if (!homeFragmentListener.setActiveBackgroundWPB(true)) {
                //swtActiveBackgroundWPB.setChecked(homeFragmentListener.getActiveBackgroundWPB());
            }
        }
    }

    private void printToast(String msg, int length) {
        Toast.makeText(getContext(), msg, length).show();
    }

    public interface HomeFragmentListener {
        boolean getActiveBackgroundWPB();
        boolean setActiveBackgroundWPB(boolean activeBackgroundWPB);
    }
}
