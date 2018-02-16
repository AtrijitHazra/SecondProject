package firebase.demo.firebaseapplicationdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import firebase.demo.firebaseapplicationdemo.model.HeaderHolder;
import firebase.demo.firebaseapplicationdemo.model.IconTreeItemHolder;
import firebase.demo.firebaseapplicationdemo.model.PlaceHolderHolder;
import firebase.demo.firebaseapplicationdemo.model.ProfileHolder;
import firebase.demo.firebaseapplicationdemo.model.SocialViewHolder;

public class TreeStructure extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tree_structure);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RelativeLayout container = findViewById(R.id.container);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        TreeNode root = TreeNode.root();

        TreeNode myProfile = new TreeNode(new IconTreeItemHolder.IconTreeItem("My Profile")).setViewHolder(new ProfileHolder(this));
        TreeNode myChoice = new TreeNode(new IconTreeItemHolder.IconTreeItem("My Chice")).setViewHolder(new ProfileHolder(this));
        TreeNode myNews = new TreeNode(new IconTreeItemHolder.IconTreeItem("My News")).setViewHolder(new ProfileHolder(this));

        addProfileData(myProfile);
        addProfileData(myChoice);
        addProfileData(myNews);

        root.addChildren(myProfile,myChoice,myNews);

        AndroidTreeView tView = new AndroidTreeView(this, root);
        tView.setDefaultAnimation(true);
        tView.setDefaultContainerStyle(R.style.TreeNodeStyleDivided, true);
        container.addView(tView.getView());
    }

    private void addProfileData(TreeNode profile) {
        TreeNode socialNetworks = new TreeNode(new IconTreeItemHolder.IconTreeItem("Social")).setViewHolder(new IconTreeItemHolder(this));
        TreeNode places = new TreeNode(new IconTreeItemHolder.IconTreeItem("Places")).setViewHolder(new IconTreeItemHolder(this));

        TreeNode facebook = new TreeNode(new SocialViewHolder.SocialItem("FB")).setViewHolder(new SocialViewHolder(this));
        TreeNode linkedin = new TreeNode(new SocialViewHolder.SocialItem("Ln")).setViewHolder(new SocialViewHolder(this));
        TreeNode google = new TreeNode(new SocialViewHolder.SocialItem("G+")).setViewHolder(new SocialViewHolder(this));
        TreeNode twitter = new TreeNode(new SocialViewHolder.SocialItem("TW")).setViewHolder(new SocialViewHolder(this));

        TreeNode lake = new TreeNode(new PlaceHolderHolder.PlaceItem("A rose garden")).setViewHolder(new PlaceHolderHolder(this));
        TreeNode mountains = new TreeNode(new PlaceHolderHolder.PlaceItem("The white house")).setViewHolder(new PlaceHolderHolder(this));

        places.addChildren(lake, mountains);
        socialNetworks.addChildren(facebook, google, twitter, linkedin);
        profile.addChildren(socialNetworks, places);
    }

}
