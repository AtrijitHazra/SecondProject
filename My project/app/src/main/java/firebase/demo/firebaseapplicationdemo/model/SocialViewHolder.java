package firebase.demo.firebaseapplicationdemo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import java.util.Random;

import firebase.demo.firebaseapplicationdemo.R;

/**
 * Created by Bogdan Melnychuk on 2/13/15.
 */
public class SocialViewHolder extends TreeNode.BaseNodeViewHolder<SocialViewHolder.SocialItem> {

    private static final String[] NAMES = new String[]{"Bruce Wayne", "Clark Kent", "Barry Allen", "Hal Jordan"};

    public SocialViewHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, SocialItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_social_node, null, false);


        TextView connectionsLabel = (TextView) view.findViewById(R.id.connections);
        Random r = new Random();
        connectionsLabel.setText(r.nextInt(150) + " connections");

        TextView userNameLabel = (TextView) view.findViewById(R.id.username);
        userNameLabel.setText(NAMES[r.nextInt(4)]);

        TextView sizeText = (TextView) view.findViewById(R.id.size);
        sizeText.setText(r.nextInt(10) + " items");

        TextView arrow_icon = (TextView) view.findViewById(R.id.arrow_icon);
        arrow_icon.setText(value.icon);

        return view;
    }

    @Override
    public void toggle(boolean active) {
    }


    public static class SocialItem {
        public String icon;

        public SocialItem(String icon) {
            this.icon = icon;
        }
        // rest will be hardcoded
    }

}
