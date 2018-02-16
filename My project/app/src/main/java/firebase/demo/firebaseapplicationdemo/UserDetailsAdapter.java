package firebase.demo.firebaseapplicationdemo;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserDetailsAdapter extends RecyclerView.Adapter<UserDetailsAdapter.VHolder> {

    private Activity context;
    private ArrayList<UserDetails> Files;
    String TAG = "UserDetailsAdapter";

    public UserDetailsAdapter(Activity context, ArrayList<UserDetails> Files) {
        this.Files = Files;
        this.context = context;
    }

    @Override
    public UserDetailsAdapter.VHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.accepted_hero_details, viewGroup, false);
        return new UserDetailsAdapter.VHolder(view);
    }

    @Override
    public void onBindViewHolder(final UserDetailsAdapter.VHolder viewHolder, final int i) {

        Log.d(TAG, "onBindViewHolder: ");
        final UserDetails heroModel = Files.get(i);
        viewHolder.name.setText(heroModel.getUserName());
        Picasso.with(context).load(heroModel.getUserImage()).into(viewHolder.image);
        viewHolder.email.setText(heroModel.getUserEmail());
    }

    @Override
    public int getItemCount() {
        if (Files == null)
            return 0;
        else
            return Files.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name;
        TextView email;

        private VHolder(final View itemView) {

            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.name);
            email = (TextView) itemView.findViewById(R.id.email);
        }

    }

    public interface ClickListener {
        void onClick(int position);

        void onLongClick(int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private UserDetailsAdapter.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final UserDetailsAdapter.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }


}
