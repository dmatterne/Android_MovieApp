package layout;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import be.david.mangaapp.AppController;
import be.david.mangaapp.EndlessRecyclerScrollListener;
import be.david.mangaapp.MovieListAdapter;
import be.david.mangaapp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MovieByGenreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MovieByGenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieByGenreFragment extends Fragment implements AppController.OnMovieListChangedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    private RecyclerView recyclerView;
    private MovieListAdapter movieListAdapter;
    private EndlessRecyclerScrollListener endlessScrollListener;
    private LinearLayoutManager linearLayoutManager;
    private final String KEY_RECYCLER_STATE = "recycler_state";
    private static Bundle mBundleRecyclerViewState;

    // TODO: Rename and change types of parameters
    private int whichGenre;

    private OnFragmentInteractionListener mListener;

    public MovieByGenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment MovieByGenreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieByGenreFragment newInstance(int param1) {
        MovieByGenreFragment fragment = new MovieByGenreFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            whichGenre = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        setHasOptionsMenu(false);

        View view =  inflater.inflate(R.layout.fragment_movie_by_genre, container, false);

        movieListAdapter = new MovieListAdapter(getContext(), AppController.getInstance().getMovieBasicList());

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewMovieByGenre);
        recyclerView.setAdapter(movieListAdapter);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        AppController.getInstance().fetchMovieByGenre(whichGenre);


//        endlessScrollListener = new EndlessRecyclerScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
//                AppController.getInstance().loadSearchMovieInfoByPage(page);
//            }
//        };
//        // Adds the scroll listener to RecyclerView
//        recyclerView.addOnScrollListener(endlessScrollListener);

//        AppController.getInstance().movieBasicResults(mParam1,true);

        return view;
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

    @Override
    public void onMovieListChanged() {
        movieListAdapter.notifyDataSetChanged();
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppController.getInstance().addOnMovieListChangedListener(this);
        movieListAdapter.notifyDataSetChanged();

        if (savedInstanceState != null) {
            Parcelable listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        mBundleRecyclerViewState = new Bundle();
        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);


        AppController.getInstance().removeOnMovieListChangedListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        AppController.getInstance().addOnMovieListChangedListener(this);
        movieListAdapter.notifyDataSetChanged();

        if (mBundleRecyclerViewState != null) {
            Parcelable listState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Parcelable listState = recyclerView.getLayoutManager().onSaveInstanceState();
        outState.putParcelable(KEY_RECYCLER_STATE, listState);


        AppController.getInstance().removeOnMovieListChangedListener(this);
    }
}
