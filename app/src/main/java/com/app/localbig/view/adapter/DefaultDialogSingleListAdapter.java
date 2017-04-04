package com.app.localbig.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.app.localbig.R;
import com.app.localbig.helper.util.FontTypefaceUtils;

import java.util.List;


public class DefaultDialogSingleListAdapter extends BaseAdapter {
	Context ctx;
	LayoutInflater lInflater;
	List<String> data;

	public DefaultDialogSingleListAdapter(Context context, List<String> data) {
		ctx = context;
		this.data = data;
		lInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

		if (convertView == null) {
            convertView = lInflater.inflate(R.layout.dialog_customlistitem, parent, false);

            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.txt_title);
            holder.textView.setTypeface(FontTypefaceUtils.getRobotoCondensedRegular(ctx));
            convertView.setTag(holder);
		}else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(data.get(position));
		
		return convertView;
	}
    public class ViewHolder {
        TextView textView;
    }

}