package com.github.ayvazj.hashadapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LinkedHashMapAdapter<K, V> extends BaseAdapter implements Filterable {

    // When set, filter operates on KEY
    public static final int FLAG_FILTER_ON_KEY = 0x1;

    // When set, filter operates on VALUE
    public static final int FLAG_FILTER_ON_VALUE = 0x2;

    // When set, filter result string is the VALUE (Default uses KEY)
    public static final int FLAG_FILTER_RESULT_USE_VALUE = 0x4;


    private LinkedHashMap<K, V> mMapData;

    /**
     * Lock used to modify the content of {@link #mMapData}. Any write operation
     * performed on the Map should be synchronized on this lock. This lock is also
     * used by the filter (see {@link #getFilter()} to make a synchronized copy of
     * the original array of mMapData.
     */
    private final Object mLock = new Object();

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter.
     */
    private int mResource;

    /**
     * The resource indicating what views to inflate to display the content of this
     * array adapter in a drop down widget.
     */
    private int mDropDownResource;

    /**
     * If the inflated resource is not a TextView, {@link #mFieldId} is used to find
     * a TextView inside the inflated views hierarchy. This field must contain the
     * identifier that matches the one defined in the resource file.
     */
    private int mFieldId = 0;

    /**
     * Indicates whether or not {@link #notifyDataSetChanged()} must be called whenever
     * {@link #mMapData} is modified.
     */
    private boolean mNotifyOnChange = true;

    private Context mContext;

    // A copy of the original mMapData array, initialized from and then used instead as soon as
    // the mFilter ArrayFilter is used. mMapData will then only contain the filtered values.
    private LinkedHashMap<K, V> mOriginalMapData;

    private MapFilter mFilter;

    private LayoutInflater mInflater;

    private int mFlags = FLAG_FILTER_ON_KEY;
    // region Ctors

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     */
    public LinkedHashMapAdapter(Context context, int resource) {
        init(context, resource, 0, new LinkedHashMap<K, V>(), FLAG_FILTER_ON_KEY);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a TextView to use when
     *                 instantiating views.
     * @param flags
     */
    public LinkedHashMapAdapter(Context context, int resource, int flags) {
        init(context, resource, 0, new LinkedHashMap<K, V>(), flags);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     * @param flags
     */
    public LinkedHashMapAdapter(Context context, int resource, int textViewResourceId, int flags) {
        init(context, resource, textViewResourceId, new LinkedHashMap<K, V>(), flags);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param mapData  The LinkedHashMap to use as the data source
     */
    public LinkedHashMapAdapter(Context context, int resource, LinkedHashMap<K, V> mapData) {
        init(context, resource, 0, mapData, FLAG_FILTER_ON_KEY);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param mapData  The LinkedHashMap to use as the data source
     * @param flags
     */
    public LinkedHashMapAdapter(Context context, int resource, LinkedHashMap<K, V> mapData, int flags) {
        init(context, resource, 0, mapData, flags);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param mapData  The LinkedHashMap to use as the data source
     */
    public LinkedHashMapAdapter(Context context, int resource, int textViewResourceId, LinkedHashMap<K, V> mapData) {
        init(context, resource, textViewResourceId, mapData, FLAG_FILTER_ON_KEY);
    }

    /**
     * Constructor
     *
     * @param context  The current context.
     * @param resource The resource ID for a layout file containing a layout to use when
     *                 instantiating views.
     * @param mapData  The LinkedHashMap to use as the data source
     * @param flags
     */
    public LinkedHashMapAdapter(Context context, int resource, int textViewResourceId, LinkedHashMap<K, V> mapData, int flags) {
        init(context, resource, textViewResourceId, mapData, flags);
    }

    private void init(Context context, int resource, int textViewResourceId, LinkedHashMap<K, V> mapData, int flags) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        mMapData = mapData;
        mOriginalMapData = new LinkedHashMap<K, V>();
        mOriginalMapData.putAll(mMapData);
        mFieldId = textViewResourceId;
        mFlags = flags;
    }

    //endregion

    /**
     * Maps the specified key to the specified value.
     *
     * @param key   the key.
     * @param value the value.
     * @return the value of any previous mapping with the specified key or
     * {@code null} if there was no such mapping.
     */
    public V put(K key, V value) {
        V result;
        synchronized (mLock) {
            if (mOriginalMapData != null) {
                result = mOriginalMapData.put(key, value);
            } else {
                result = mMapData.put(key, value);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        return result;
    }

    /**
     * Removes the mapping with the specified key from this map.
     *
     * @param key the key of the mapping to remove.
     * @return the value of the removed mapping or {@code null} if no mapping
     * for the specified key was found.
     */
    public V remove(K key) {
        V result;
        synchronized (mLock) {
            if (mOriginalMapData != null) {
                result = mOriginalMapData.remove(key);
            } else {
                result = mMapData.remove(key);
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
        return result;
    }

    /**
     * Removes the mapping with the specified key from this map.
     *
     * @param key the key of the mapping to remove.
     * @return the value of the removed mapping or {@code null} if no mapping
     * for the specified key was found.
     */
    public V get(K key) {
        V result;
        synchronized (mLock) {
            result = mMapData.get(key);
        }
        return result;
    }

    /**
     * This override is done for LinkedHashMap performance: iteration is cheaper
     * via LinkedHashMap nxt links.
     */
    public boolean containsValue(V value) {
        boolean result;
        synchronized (mLock) {
            result = mMapData.containsValue(value);
        }
        return result;
    }

    /**
     * Remove all elements from the list.
     */
    public void clear() {
        synchronized (mLock) {
            if (mOriginalMapData != null) {
                mOriginalMapData.clear();
            } else {
                mMapData.clear();
            }
        }
        if (mNotifyOnChange) notifyDataSetChanged();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        mNotifyOnChange = true;
    }

    /**
     * Control whether methods that change the list ({@link #put},
     * {@link #remove}, {@link #clear}) automatically call
     * {@link #notifyDataSetChanged}.  If set to false, caller must
     * manually call notifyDataSetChanged() to have the changes
     * reflected in the attached view.
     * <p/>
     * The default is true, and calling notifyDataSetChanged()
     * resets the flag to true.
     *
     * @param notifyOnChange if true, modifications to the list will
     *                       automatically call {@link
     *                       #notifyDataSetChanged}
     */
    public void setNotifyOnChange(boolean notifyOnChange) {
        mNotifyOnChange = notifyOnChange;
    }


    /**
     * Returns the context associated with this array adapter. The context is used
     * to create views from the resource passed to the constructor.
     *
     * @return The Context associated with this adapter.
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getCount() {
        return this.mMapData.entrySet().size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Entry<K, V> getItem(int position) {
        int i = 0;
        for (Entry<K, V> entry : mMapData.entrySet()) {
            if (i++ == position) return entry;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public long getItemId(int position) {
        Entry<K, V> item = getItem(position);
        return item == null ? 0 : item.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        Entry<K, V> item = getItem(position);
        if (item instanceof CharSequence) {
            text.setText((CharSequence) item.getValue());
        } else {
            text.setText(item.getValue().toString());
        }

        return view;
    }

    /**
     * <p>Sets the layout resource to create the drop down views.</p>
     *
     * @param resource the layout resource defining the drop down views
     * @see #getDropDownView(int, android.view.View, android.view.ViewGroup)
     */
    public void setDropDownViewResource(int resource) {
        this.mDropDownResource = resource;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new MapFilter();
        }
        return mFilter;
    }

    public void setFlags(int flags) {
        this.mFlags = flags;
    }

    /**
     * <p>An Map filter constrains the content of the HashMap adapter with
     * a prefix. Each item that does not start with the supplied prefix
     * is removed from the list.</p>
     */
    private class MapFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalMapData == null) {
                synchronized (mLock) {
                    mOriginalMapData = new LinkedHashMap<K, V>(mMapData);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                LinkedHashMap<K, V> map;
                synchronized (mLock) {
                    map = new LinkedHashMap<K, V>(mOriginalMapData);
                }
                results.values = map;
                results.count = map.size();
            } else {
                String prefixString = prefix.toString().toLowerCase();

                LinkedHashMap<K, V> values;
                synchronized (mLock) {
                    values = new LinkedHashMap<K, V>(mOriginalMapData);
                }

                final LinkedHashMap<K, V> newValues = new LinkedHashMap<K, V>();
                for (Entry<K, V> entry : values.entrySet()) {
                    final K key = entry.getKey();
                    final String keyText = key.toString().toLowerCase();
                    final V value = entry.getValue();
                    final String valueText = value.toString().toLowerCase();

                    boolean matched = false;
                    if ((mFlags & FLAG_FILTER_ON_KEY) == FLAG_FILTER_ON_KEY) {
                        if (keyText.startsWith(prefixString)) {
                            matched = true;
                        } else {
                            final String[] words = keyText.split(" ");
                            final int wordCount = words.length;

                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    matched = true;
                                    break;
                                }
                            }
                        }
                    }
                    if ((mFlags & FLAG_FILTER_ON_VALUE) == FLAG_FILTER_ON_VALUE) {
                        if (valueText.startsWith(prefixString)) {
                            matched = true;
                        } else {
                            final String[] words = valueText.split(" ");
                            final int wordCount = words.length;

                            for (int k = 0; k < wordCount; k++) {
                                if (words[k].startsWith(prefixString)) {
                                    matched = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (matched) {
                        newValues.put(key, value);
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }

            return results;
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            CharSequence result;
            if (resultValue == null) {
                result = "";
            }
            if ((mFlags & FLAG_FILTER_RESULT_USE_VALUE) == FLAG_FILTER_RESULT_USE_VALUE) {
                return ((Entry<String, String>) resultValue).getValue();
            } else {
                return ((Entry<String, String>) resultValue).getKey();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mMapData = (LinkedHashMap<K, V>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
