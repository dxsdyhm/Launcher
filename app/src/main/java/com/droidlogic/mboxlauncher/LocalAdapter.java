package com.droidlogic.mboxlauncher;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LocalAdapter extends BaseAdapter implements Filterable {
    /* access modifiers changed from: private */
    public List<? extends Map<String, ?>> mData;
    private int mDropDownResource;
    private SimpleFilter mFilter;
    /* access modifiers changed from: private */
    public String[] mFrom;
    private LayoutInflater mInflater;
    private int mResource;
    /* access modifiers changed from: private */
    public int[] mTo;
    /* access modifiers changed from: private */
    public ArrayList<Map<String, ?>> mUnfilteredData;
    private ViewBinder mViewBinder;

    private class SimpleFilter extends Filter {
        private SimpleFilter() {
        }

        /* access modifiers changed from: protected */
        public FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();
            if (LocalAdapter.this.mUnfilteredData == null) {
                LocalAdapter.this.mUnfilteredData = new ArrayList(LocalAdapter.this.mData);
            }
            if (prefix == null || prefix.length() == 0) {
                ArrayList<Map<String, ?>> list = LocalAdapter.this.mUnfilteredData;
                results.values = list;
                results.count = list.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();
                ArrayList<Map<String, ?>> unfilteredValues = LocalAdapter.this.mUnfilteredData;
                int count = unfilteredValues.size();
                ArrayList<Map<String, ?>> newValues = new ArrayList<>(count);
                for (int i = 0; i < count; i++) {
                    Map<String, ?> h = (Map) unfilteredValues.get(i);
                    if (h != null) {
                        int len = LocalAdapter.this.mTo.length;
                        for (int j = 0; j < len; j++) {
                            String[] words = ((String) h.get(LocalAdapter.this.mFrom[j])).split(" ");
                            int wordCount = words.length;
                            int k = 0;
                            while (true) {
                                if (k >= wordCount) {
                                    break;
                                } else if (words[k].toLowerCase().startsWith(prefixString)) {
                                    newValues.add(h);
                                    break;
                                } else {
                                    k++;
                                }
                            }
                        }
                    }
                }
                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        /* access modifiers changed from: protected */
        public void publishResults(CharSequence constraint, FilterResults results) {
            LocalAdapter.this.mData = (List) results.values;
            if (results.count > 0) {
                LocalAdapter.this.notifyDataSetChanged();
            } else {
                LocalAdapter.this.notifyDataSetInvalidated();
            }
        }
    }

    public interface ViewBinder {
        boolean setViewValue(View view, Object obj, String str);
    }

    public LocalAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to) {
        this.mData = data;
        this.mDropDownResource = resource;
        this.mResource = resource;
        this.mFrom = from;
        this.mTo = to;
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return this.mData.size();
    }

    public Object getItem(int position) {
        return this.mData.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, this.mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
        View v;
        if (convertView == null) {
            v = this.mInflater.inflate(resource, parent, false);
        } else {
            v = convertView;
        }
        try {
            bindView(position, v);
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append(">>>");
            sb.append(e.getMessage());
            Log.d("LocalAdapter", sb.toString());
        }
        return v;
    }

    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, this.mDropDownResource);
    }

    private void bindView(int position, View view) {
        Map dataSet = (Map) this.mData.get(position);
        if (dataSet != null) {
            ViewBinder binder = this.mViewBinder;
            String[] from = this.mFrom;
            int[] to = this.mTo;
            int count = to.length;
            for (int i = 0; i < count; i++) {
                View v = view.findViewById(to[i]);
                if (v != null) {
                    Object data = dataSet.get(from[i]);
                    String text = data == null ? "" : data.toString();
                    if (text == null) {
                        text = "";
                    }
                    boolean bound = false;
                    if (binder != null) {
                        bound = binder.setViewValue(v, data, text);
                    }
                    if (bound) {
                        continue;
                    } else if (v instanceof Checkable) {
                        if (data instanceof Boolean) {
                            ((Checkable) v).setChecked(((Boolean) data).booleanValue());
                        } else if (v instanceof TextView) {
                            setViewText((TextView) v, text);
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append(v.getClass().getName());
                            sb.append(" should be bound to a Boolean, not a ");
                            sb.append(data == null ? "<unknown type>" : data.getClass());
                            throw new IllegalStateException(sb.toString());
                        }
                    } else if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof ImageView) {
                        if (data instanceof Integer) {
                            ((ImageView) v).setImageResource(((Integer) data).intValue());
                        } else if (data instanceof Drawable) {
                            ((ImageView) v).setImageDrawable((Drawable) data);
                        } else {
                            try {
                                ((ImageView) v).setImageBitmap((Bitmap) data);
                            } catch (NumberFormatException e) {
                                ((ImageView) v).setImageURI(Uri.parse(text));
                            }
                        }
                    } else if (!(v instanceof RelativeLayout)) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(v.getClass().getName());
                        sb2.append(" is not a  view that can be bounds by this LocalAdapter");
                        throw new IllegalStateException(sb2.toString());
                    } else if (data instanceof Integer) {
                        v.setBackgroundResource(((Integer) data).intValue());
                    }
                }
            }
        }
    }

    public void setViewText(TextView v, String text) {
        v.setText(text);
    }

    public Filter getFilter() {
        if (this.mFilter == null) {
            this.mFilter = new SimpleFilter();
        }
        return this.mFilter;
    }
}
