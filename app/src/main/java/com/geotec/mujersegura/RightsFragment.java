package com.geotec.mujersegura;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.geotec.mujersegura.adapters.Rights;
import com.geotec.mujersegura.adapters.RightsCardAdapter;
import com.geotec.mujersegura.others.Complements;
import com.geotec.mujersegura.others.Internet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class RightsFragment extends Fragment {

    private Context context;

    private View view;
    private RecyclerView recyclerView;
    private ProgressBar pbrLoadOnline;
    private TextView lblSample;

    private RightsCardAdapter rightsCardAdapter;
    private ArrayList<Rights> rightsArrayList;

    private Complements C;
    private RightsFragmentListener rightsFragmentListener;
    private Rights rights;

    private boolean loadOnline = false;

    public RightsFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_rights, container, false);

        recyclerView    = (RecyclerView) view.findViewById(R.id.rcl_rights_online);
        pbrLoadOnline   = (ProgressBar) view.findViewById(R.id.pbr_rights_load_online);
        lblSample       = (TextView) view.findViewById(R.id.lbl_rights_sample);

        C = new Complements(this.context);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        checkedLoadOnline();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        if (context instanceof RightsFragmentListener) {
            rightsFragmentListener = (RightsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement RightsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        rightsFragmentListener = null;
    }

    private void checkedLoadOnline() {
        if (loadOnline) {
            pbrLoadOnline.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            //lblSample.setVisibility(View.VISIBLE);
            iniCardsOnline();
        } else {
            if (Internet.isConnected(getContext()))
                rightsFragmentListener.requestData(null, C.Api(R.string.api_rights), false);
            else C.PrintDialog(null, getString(R.string.error_connected_internet));
        }
    }

    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;
    private void iniCardsOnline() {
        if (rightsCardAdapter == null) {
            rightsCardAdapter = new RightsCardAdapter(this.context, rightsArrayList);
        }
        recyclerView.setAdapter(rightsCardAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.context);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItems    = linearLayoutManager.getChildCount();
                totalItems      = linearLayoutManager.getItemCount();
                scrollOutItems  = linearLayoutManager.findFirstVisibleItemPosition();
                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    C.PrintDialog(null, "Se cargarán más!");
                }
            }
        });
        rightsCardAdapter.setOnItemListener(new RightsCardAdapter.OnItemListener() {
            @Override
            public void onDownload(int position) {
                downloadItem(position);
            }
        });
    }

    private void downloadItem(int position) {
        removeItem(position);
        addItem(0);
    }

    private void removeItem(int position){
        rights = rightsArrayList.get(position);
        if (rights.isDownload()) rights.setDownload(false);
        else rights.setDownload(true);
        rightsArrayList.remove(position);
        rightsCardAdapter.notifyItemRemoved(position);
    }

    public void addItem(int position){
        //position = 0;
        rightsArrayList.add(position, rights);
        rightsCardAdapter.notifyItemInserted(position);
        //rightsCardAdapter.notifyDataSetChanged();
    }

    public void arrivedData(JSONObject r) {
        try {
            if (r.getBoolean(getString(R.string.rest_json_response_result))) {
                loadOnline = true;
                rightsArrayList = new ArrayList<>();
                JSONArray arrayOnline = r.getJSONArray(getString(R.string.rest_json_response_data));
                for (int  i = 0; i < arrayOnline.length(); i++) {
                    Rights rights = new Rights(
                            (long) i,
                            arrayOnline.getJSONObject(i).getString(getString(R.string.rest_var_right_cve)),
                            arrayOnline.getJSONObject(i).getString(getString(R.string.rest_var_right_name)),
                            arrayOnline.getJSONObject(i).getString(getString(R.string.rest_var_right_description)),
                            arrayOnline.getJSONObject(i).getString(getString(R.string.rest_var_right_source)),
                            arrayOnline.getJSONObject(i).getString(getString(R.string.rest_var_right_timestamp)),
                            false
                    );
                    rightsArrayList.add(rights);
                }
                checkedLoadOnline();
            } else {
                C.PrintDialog(null, r.getString(getString(R.string.rest_json_response_message)));
            }
        } catch (JSONException e) {
            C.PrintDialog(null, getString(R.string.error_try_json));
            e.printStackTrace();
        }
    }

    public interface RightsFragmentListener {
        void requestData(JSONObject params, String url, boolean viewProgress);
    }
}
