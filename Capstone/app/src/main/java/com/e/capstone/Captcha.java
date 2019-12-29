package com.e.capstone;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class Captcha extends DialogFragment {
    private ImageView imageView = null;
    private EditText editText = null;
    private Button button = null;
    private String host = CommonUtil.host;

    public static Captcha newInstance() {
        return new Captcha();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.captcha_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void init(View view1) {
        imageView = view1.findViewById(R.id.imageView);
        editText = view1.findViewById(R.id.editText);
        button = view1.findViewById(R.id.button);
        callImageService("getCaptcha", null);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!AnalyseFragment.validated) {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("captcha", editText.getText());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callJSONService("validateCaptcha", Request.Method.POST, json);
                }
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void callJSONService(final String serviceUrl, int method, final JSONObject postData) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, host + serviceUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if ("validateCaptcha".equals(serviceUrl)) {
                            try {
                                if ("success".equals(response.getString("response"))) {
                                    getDialog().dismiss();
                                    AnalyseFragment.validated = true;
                                } else {
                                    callImageService("getCaptcha", null);
                                    AnalyseFragment.validated = false;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void callImageService(final String serviceUrl, final Map<String, String> params) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ImageRequest imageRequest = new ImageRequest(host + serviceUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        if ("getCaptcha".equals(serviceUrl) && null != response) {
                            imageView.setImageBitmap(response);
                        }
                    }
                }, 0,0, ImageView.ScaleType.CENTER_CROP,null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(imageRequest);
    }
}
