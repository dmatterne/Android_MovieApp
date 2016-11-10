package layout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
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
 * {@link GenreFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GenreFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenreFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match

    private OnFragmentInteractionListener mListener;

    public GenreFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment GenreFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenreFragment newInstance() {
        GenreFragment fragment = new GenreFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

//        AppController.getInstance().addOnMovieListChangedListener(this);
//        movieListAdapter.notifyDataSetChanged();
//
//        if (savedInstanceState != null) {
//            Parcelable listState = savedInstanceState.getParcelable(KEY_RECYCLER_STATE_GENRE);
//            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
//        }

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        AlertDialog genreDl = new AlertDialog.Builder(getActivity())
                .setTitle("Genre?")
                .setMessage("Please select a movie genre! :")
                .create();

        return genreDl;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_genre, container, false);
//
//        movieListAdapter = new MovieListAdapter(getContext(), AppController.getInstance().getMovieBasicList());
//
//        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewGenre);
//        recyclerView.setAdapter(movieListAdapter);
//        linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//
//        endlessScrollListener = new EndlessRecyclerScrollListener(linearLayoutManager) {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
//                // Triggered only when new data needs to be appended to the list
//                // Add whatever code is needed to append new items to the bottom of the list
////                AppController.getInstance().loadSearchMovieInfoByPage(page);
//            }
//        };
//        // Adds the scroll listener to RecyclerView
//        recyclerView.addOnScrollListener(endlessScrollListener);
//
////        AppController.getInstance().movieBasicResults(true);
//
//        return view;
//    }

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



}
