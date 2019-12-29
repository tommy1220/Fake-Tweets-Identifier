package com.e.capstone;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AnalyseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AnalyseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnalyseFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String host = CommonUtil.host;
    public static boolean validated = false;
    private EditText editText = null;
    private TextView textView2 = null;
    private TextView textView3 = null;
    private TextView textView4 = null;
    private TextView textView5 = null;
    private TextView textView6 = null;
    private TextView textView7 = null;
    private TextView textView8 = null;
    private TextView textView9 = null;
    private TextView textView10 = null;
    private TextView textView11 = null;
    private TextView textView12 = null;
    private TextView textView13 = null;
    private TextView textView14 = null;
    private TextView textView15 = null;
    private TextView textView16 = null;
    private TextView textView17 = null;
    private TextView textView18 = null;
    private TextView textView19 = null;
    private TextView textView20 = null;
    private TextView textView21 = null;
    private TextView textView22 = null;
    private String tweetId = null;
    private Button button = null;
    private Button button3 = null;
    private Button button4 = null;
    private ToggleButton toggleButton2 = null;
    private WebView webView = null;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AnalyseFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AnalyseFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AnalyseFragment newInstance(String param1, String param2) {
        AnalyseFragment fragment = new AnalyseFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void init(View view1) {
        System.out.println("hello inside the onCreateView");
        editText = view1.findViewById(R.id.editText);

        textView2 = view1.findViewById(R.id.textView2);
        textView3 = view1.findViewById(R.id.textView3);
        textView4 = view1.findViewById(R.id.textView4);
        textView5 = view1.findViewById(R.id.textView5);
        textView6 = view1.findViewById(R.id.textView6);
        textView7 = view1.findViewById(R.id.textView7);
        textView8 = view1.findViewById(R.id.textView8);
        textView9 = view1.findViewById(R.id.textView9);
        textView10 = view1.findViewById(R.id.textView10);
        textView11 = view1.findViewById(R.id.textView11);
        textView12 = view1.findViewById(R.id.textView12);
        textView13 = view1.findViewById(R.id.textView13);
        textView14 = view1.findViewById(R.id.textView14);
        textView15 = view1.findViewById(R.id.textView15);
        textView16 = view1.findViewById(R.id.textView16);
        textView17 = view1.findViewById(R.id.textView17);
        textView18 = view1.findViewById(R.id.textView18);
        textView19 = view1.findViewById(R.id.textView19);
        textView20 = view1.findViewById(R.id.textView20);
        textView21 = view1.findViewById(R.id.textView21);
        textView22 = view1.findViewById(R.id.textView22);

        button = view1.findViewById(R.id.button);
        button3 = view1.findViewById(R.id.button3);
        button4 = view1.findViewById(R.id.button4);

        toggleButton2 = view1.findViewById(R.id.toggleButton2);

        webView = view1.findViewById(R.id.webView);

        toggleButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideShow();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!validated) {
                    DialogFragment newFragment = new Captcha();
                    final FragmentManager fragmentManager = getChildFragmentManager();
                    newFragment.show(fragmentManager, null);
                } else {
                    JSONObject json = new JSONObject();
                    try {
                        json.put("url", editText.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callJSONService("analyzeTweet", Request.Method.POST, json);
                }
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("tweetId", tweetId);
                    json.put("vote", "REAL");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callJSONService("recordpoll", Request.Method.POST, json);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                JSONObject json = new JSONObject();
                try {
                    json.put("tweetId", tweetId);
                    json.put("vote", "FAKE");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callJSONService("recordpoll", Request.Method.POST, json);
            }
        });

        textView5.setVisibility(View.INVISIBLE);
        textView6.setVisibility(View.INVISIBLE);
        textView7.setVisibility(View.INVISIBLE);
        textView8.setVisibility(View.INVISIBLE);
        textView9.setVisibility(View.INVISIBLE);
        textView10.setVisibility(View.INVISIBLE);

        textView11.setVisibility(View.INVISIBLE);
        textView12.setVisibility(View.INVISIBLE);
        textView13.setVisibility(View.INVISIBLE);
        textView14.setVisibility(View.INVISIBLE);
        textView15.setVisibility(View.INVISIBLE);
        textView16.setVisibility(View.INVISIBLE);
        textView17.setVisibility(View.INVISIBLE);
        textView18.setVisibility(View.INVISIBLE);
        textView19.setVisibility(View.INVISIBLE);
        textView20.setVisibility(View.INVISIBLE);
        textView21.setVisibility(View.INVISIBLE);
        textView22.setVisibility(View.INVISIBLE);
        button3.setVisibility(View.INVISIBLE);
        button4.setVisibility(View.INVISIBLE);
        toggleButton2.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_analyse, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void callJSONService(final String serviceUrl, int method, final JSONObject postData) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, host + serviceUrl, postData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if ("analyzeTweet".equals(serviceUrl) || "recordpoll".equals(serviceUrl)) {
                            renderPage(response);
                        } else if ("validateCaptcha".equals(serviceUrl)) {
                            try {
                                if ("success".equals(response.getString("response"))) {
                                    JSONObject json = new JSONObject();
                                    try {
                                        json.put("url", editText.getText().toString());
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    callJSONService("analyzeTweet", Request.Method.POST, json);
                                    validated = true;
                                } else {
                                    DialogFragment newFragment = new Captcha();
                                    final FragmentManager fragmentManager = getChildFragmentManager();
                                    newFragment.show(fragmentManager, null);
                                    validated = false;
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

    private void callStringService(final String serviceUrl, int method, final Map<String, String> params) {
        RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        StringRequest stringRequest = new StringRequest(method, host + serviceUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if ("url".equals(serviceUrl) || "tweet".equals(serviceUrl)) {
                            webView.loadData(response, "text/html", "UTF-8");
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void hideShow() {
        if (View.VISIBLE == textView5.getVisibility()) {
            textView5.setVisibility(View.INVISIBLE);
        } else {
            textView5.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView6.getVisibility()) {
            textView6.setVisibility(View.INVISIBLE);
        } else {
            textView6.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView7.getVisibility()) {
            textView7.setVisibility(View.INVISIBLE);
        } else {
            textView7.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView8.getVisibility()) {
            textView8.setVisibility(View.INVISIBLE);
        } else {
            textView8.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView9.getVisibility()) {
            textView9.setVisibility(View.INVISIBLE);
        } else {
            textView9.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView10.getVisibility()) {
            textView10.setVisibility(View.INVISIBLE);
        } else {
            textView10.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView12.getVisibility()) {
            textView12.setVisibility(View.INVISIBLE);
        } else {
            textView12.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView13.getVisibility()) {
            textView13.setVisibility(View.INVISIBLE);
        } else {
            textView13.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView14.getVisibility()) {
            textView14.setVisibility(View.INVISIBLE);
        } else {
            textView14.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView15.getVisibility()) {
            textView15.setVisibility(View.INVISIBLE);
        } else {
            textView15.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView16.getVisibility()) {
            textView16.setVisibility(View.INVISIBLE);
        } else {
            textView16.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView17.getVisibility()) {
            textView17.setVisibility(View.INVISIBLE);
        } else {
            textView17.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView18.getVisibility()) {
            textView18.setVisibility(View.INVISIBLE);
        } else {
            textView18.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView19.getVisibility()) {
            textView19.setVisibility(View.INVISIBLE);
        } else {
            textView19.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView20.getVisibility()) {
            textView20.setVisibility(View.INVISIBLE);
        } else {
            textView20.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView21.getVisibility()) {
            textView21.setVisibility(View.INVISIBLE);
        } else {
            textView21.setVisibility(View.VISIBLE);
        }

        if (View.VISIBLE == textView22.getVisibility()) {
            textView22.setVisibility(View.INVISIBLE);
        } else {
            textView22.setVisibility(View.VISIBLE);
        }
    }

    private void renderPage(JSONObject response) {
        try {

//            textView1.setText(Html.fromHtml(response.getString("error")));

            textView11.setVisibility(View.VISIBLE);
            button3.setVisibility(View.VISIBLE);
            button4.setVisibility(View.VISIBLE);
            toggleButton2.setVisibility(View.VISIBLE);
            webView.setVisibility(View.VISIBLE);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            Map<String, String> params = new HashMap<>();
            params.put("url", editText.getText().toString());
            callStringService("tweet", Request.Method.POST, params);

            textView2.setText(Html.fromHtml("According to our system's model trained with <b>J48</b> algorithm it's <b>" + response.getString("j48PredictedClass") + "</b>"));

            textView3.setText(Html.fromHtml("According to our system's model trained with <b>SVM</b> algorithm it's <b>" + response.getString("svmPredictedClass") + "</b>"));

            textView4.setText(Html.fromHtml(response.getString("fakeCount") + " people have voted its FAKE and " + response.getString("realCount") + " have voted its REAL"));

            JSONObject stats = new JSONObject(response.getString("featurevector"));

            tweetId = stats.getString("tweetID");

            if ((1 == stats.getInt("isUrlCredible")) && stats.getBoolean("containsUrl")) {
                textView5.setText(Html.fromHtml("<li>The URL's in the tweet content are <b>credible</b></li>"));
            } else if ((0 == stats.getInt("isUrlCredible")) && stats.getBoolean("containsUrl")) {
                textView5.setText(Html.fromHtml("<li style=\"color: red\">The URL's in the tweet content does <b>NOT</b> look credible</li>"));
            }

            if ((1 == stats.getInt("isImageCredible")) && stats.getBoolean("containsImages")) {
                textView6.setText(Html.fromHtml("<li>The images in the tweet content are <b>credible</b></li>"));
            } else if ((0 == stats.getInt("isImageCredible")) && stats.getBoolean("containsImages")) {
                textView6.setText(Html.fromHtml("<li style=\"color: red\">The images in the tweet content does <b>NOT</b> look credible</li>"));
            }

            if (stats.getBoolean("doesGoogleDocumentSearchForMediaEntityHaveFakeUrl") && stats.getBoolean("containsImages")) {
                textView7.setText(Html.fromHtml("<li style=\"color: red\">On doing a google document search for media entities in tweet, <b>there were media entities from known list of fake url's</b></li>"));
            } else if (!stats.getBoolean("doesGoogleDocumentSearchForMediaEntityHaveFakeUrl") && stats.getBoolean("containsImages")) {
                textView7.setText(Html.fromHtml("<li>On doing a google document search for media entities in tweet, <b>there were NO media entities from known list of fake url's</b></li>"));
            }

            if (stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveFakeUrl") && stats.getBoolean("containsImages")) {
                textView8.setText(Html.fromHtml("<li style=\"color: red\">On doing a google image search for media entities in tweet, <b>there were media entities from known list of fake url's</b></li>"));
            } else if (!stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveFakeUrl") && stats.getBoolean("containsImages")) {
                textView8.setText(Html.fromHtml("<li>On doing a google image search for media entities in tweet, <b>there were NO media entities from known list of fake url's</b></li>"));
            }

            if ((stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription") || stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle")) && stats.getBoolean("containsImages")) {
                textView9.setText(Html.fromHtml("<li style=\"color: red\">On doing a google image search for media entities in tweet, <b>there were search results that indicate the tweet is fake</b></li>"));
            } else if (((!stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveWordFakeInDescription")) || (stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveWordFakeInTitle"))) && stats.getBoolean("containsImages")) {
                textView9.setText(Html.fromHtml("<li>On doing a google image search for media entities in tweet, <b>there were NO search results that indicate the tweet is fake</b></li>"));
            }

            if (stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveDateMisaligned") && stats.getBoolean("containsImages")) {
                textView10.setText(Html.fromHtml("<li style=\"color: red\">On doing a google image search for media entities in tweet, <b>there were search results with same content much ahead of time than the incident</b></li>"));
            } else if (!stats.getBoolean("doesGoogleImageSearchForMediaEntityHaveDateMisaligned") && stats.getBoolean("containsImages")) {
                textView10.setText(Html.fromHtml("<li>On doing a google image search for media entities in tweet, <b>there were NO search results with same content much ahead of time than the incident</b></li>"));
            }

            if (stats.getBoolean("bestGuessForGoogleImageSearchForMediaEntityUnrelated") && stats.getBoolean("containsImages")) {
                textView12.setText(Html.fromHtml("<li style=\"color: red\">The best guess result of google image search for media entities in tweet seems <b>unrelated</b></li>"));
            } else if (!stats.getBoolean("bestGuessForGoogleImageSearchForMediaEntityUnrelated") && stats.getBoolean("containsImages")) {
                textView12.setText(Html.fromHtml("<li>The best guess result of google image search for media entities in tweet seems <b>related</b></li>"));
            }

            textView15.setText(Html.fromHtml("The sentiment score of the tweet is " + stats.getDouble("sentimentalScore") + " where <b>-1</b> indicate the tone of the tweet is most <b>negative</b> and <b>+1 </b>indicates tone of the tweet being <b>extremely positive</b>"));

            textView17.setText(Html.fromHtml("Tweet user Friends count " + stats.getInt("noOfFriends")));

            textView18.setText(Html.fromHtml("Tweet user Followers Count " + stats.getInt("noOfFollowers")));

            textView19.setText(Html.fromHtml("Tweet user Friend Follower Ratio " + stats.getInt("friendFollowerRatio")));

            if (1 == stats.getInt("isUserHasURL")) {
                textView20.setText(Html.fromHtml("Does the tweet user have URL? Yes"));
            } else {
                textView20.setText(Html.fromHtml("Does the tweet user have URL? No"));
            }

            if (1 == stats.getInt("isVerifiedUser")) {
                textView21.setText(Html.fromHtml("Is the user account verified? Yes"));
            } else {
                textView21.setText(Html.fromHtml("Is the user account verified? No"));
            }

            textView22.setText(Html.fromHtml("Number of tweets user has posted " + stats.getInt("noOfTweets")));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public interface OnHeadlineSelectedListener {
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
        } else {
            if (!validated) {
                DialogFragment newFragment = new Captcha();
                final FragmentManager fragmentManager = getChildFragmentManager();
                newFragment.show(fragmentManager, null);
            }
        }
    }
}