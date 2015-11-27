package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 8/31/2015.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewItemSearchAdapter extends ArrayAdapter<ItemList> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<ItemList> data = new ArrayList<ItemList>();

    public GridViewItemSearchAdapter(Context context, int layoutResourceId, ArrayList<ItemList> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.imageTitle = (TextView) row.findViewById(R.id.text);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        ItemList item = data.get(position);
        holder.imageTitle.setText(item.getTitle());

        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
    }
}