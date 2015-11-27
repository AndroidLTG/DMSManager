package androidltg.stanstudios.com.dmsmanager;

/**
 * Created by DMSv4 on 9/8/2015.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;


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

public class GridViewTableOpenAdapter extends ArrayAdapter<TableOpen> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<TableOpen> data = new ArrayList<TableOpen>();

    public GridViewTableOpenAdapter(Context context, int layoutResourceId, ArrayList<TableOpen> data) {
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
            holder.sohoadon = (TextView) row.findViewById(R.id.openhoadonso);
            holder.tenban = (TextView) row.findViewById(R.id.openbanso);
            holder.thoigianvao = (TextView) row.findViewById(R.id.opendate);
            holder.tongtien = (TextView) row.findViewById(R.id.openmoney);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }


        TableOpen item = data.get(position);
        holder.sohoadon.setText("SỐ: "+item.getstt());
        holder.tenban.setText(item.getTitle());
        String[] time = item.getstartdate().split("T");

        holder.thoigianvao.setText("GIỜ "+time[1].substring(0,5));
        holder.tongtien.setText(item.getprices());

        return row;
    }

    static class ViewHolder {
        TextView sohoadon,tenban,thoigianvao,tongtien;

    }
}