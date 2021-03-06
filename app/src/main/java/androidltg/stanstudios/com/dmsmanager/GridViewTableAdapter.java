package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 8/31/2015.
 */
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewTableAdapter extends ArrayAdapter<TableItem> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<TableItem> data = new ArrayList<TableItem>();

    public GridViewTableAdapter(Context context, int layoutResourceId, ArrayList<TableItem> data) {
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
            holder.image = (ImageView) row.findViewById(R.id.image);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        TableItem item = data.get(position);
        holder.imageTitle.setText(item.getName());
        holder.imageTitle.setTextColor(Color.WHITE);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.banan);
        holder.image.setImageBitmap(bitmap);
        if(item.getStatus()==2) holder.imageTitle.setBackgroundColor(Color.RED);// ban day
        if(item.getStatus()==1) holder.imageTitle.setBackgroundColor(Color.BLUE);//ban dat truoc
        return row;
    }

    static class ViewHolder {
        TextView imageTitle;
        ImageView image;
    }
}