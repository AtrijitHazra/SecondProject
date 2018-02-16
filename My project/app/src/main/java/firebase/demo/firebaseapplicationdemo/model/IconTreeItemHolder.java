package firebase.demo.firebaseapplicationdemo.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.unnamed.b.atv.model.TreeNode;

import firebase.demo.firebaseapplicationdemo.R;

/**
 * Created by Bogdan Melnychuk on 2/12/15.
 */
public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconTreeItem> {
    private TextView tvValue;
    private ImageView arrowView;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(final TreeNode node, IconTreeItem value) {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.layout_icon_node, null, false);
        tvValue = (TextView) view.findViewById(R.id.node_value);
        tvValue.setText(value.text);


        arrowView = (ImageView) view.findViewById(R.id.arrow_icon);


        return view;
    }

    @Override
    public void toggle(boolean active) {
        arrowView.setBackground(context.getResources().getDrawable(active ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_down));
    }

    public static class IconTreeItem {
        public int icon;
        public String text;

        public IconTreeItem(String text) {
//            this.icon = icon;
            this.text = text;
        }
    }
}
