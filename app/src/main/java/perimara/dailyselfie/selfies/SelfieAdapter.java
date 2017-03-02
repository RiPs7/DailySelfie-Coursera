package perimara.dailyselfie.selfies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import perimara.dailyselfie.R;
import perimara.dailyselfie.activities.ViewActivity;

/**
 * Created by periklismaravelias on 26/02/17.
 */
public class SelfieAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<Selfie> mList;

    public SelfieAdapter(Context context){
        mContext = context;
        mList = new ArrayList();
    }

    public void add(Selfie selfie){
        mList.add(selfie);
        notifyDataSetChanged();
    }

    public void clear(){
        mList.clear();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Selfie selfie = mList.get(position);

        LinearLayout selfie_layout = (LinearLayout)LayoutInflater.from(mContext).inflate(R.layout.selfie_overview, null);
        selfie_layout.setLongClickable(true);

        ImageView thumbnail = (ImageView)selfie_layout.findViewById(R.id.selfieThumbnail);
        TextView name = (TextView)selfie_layout.findViewById(R.id.selfieName);

        thumbnail.setImageBitmap(selfie.getThumbnail());
        name.setText(selfie.getName());

        selfie_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File f = new File(selfie.getPath());
                Intent viewIntent = new Intent(mContext, ViewActivity.class);
                viewIntent.setDataAndType(Uri.fromFile(f), "image/*");
                mContext.startActivity(viewIntent);
            }
        });

        return selfie_layout;
    }
}
