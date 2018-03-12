package com.example.tarikpc.tinder;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.tarikpc.tinder.model.FeedData;
import java.util.List;

/**
 * Created by Tarik PC on 07-03-18.
 */

public class CardAdapter extends ArrayAdapter<FeedData> {

    private int _resource;

    public CardAdapter(@NonNull Context context, int resource, @NonNull List<FeedData> objects) {
        super(context, resource, objects);
        _resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_user_card, parent, false);
        FeedData profile = getItem(position);
        TextView name = convertView.findViewById(R.id.name);
        ImageView profilePic = convertView.findViewById(R.id.profilePic);
        if(profile.getIssuingUser().getFirstName() != null) name.setText(profile.getIssuingUser().getFirstName());
        else if(profile.getIssuingUser().getLastName() != null) name.setText(profile.getIssuingUser().getLastName());
        else name.setText(profile.getIssuingUser().getEmail());
        Glide.with(this.getContext()).load(profile.getIssuingUser().getProfileImage()).into(profilePic);
        return convertView;
    }
}
