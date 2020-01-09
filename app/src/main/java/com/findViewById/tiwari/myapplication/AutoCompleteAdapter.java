package com.findViewById.tiwari.myapplication;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter {


    private List<String> tempItems;
    private List<String>    suggestions;
    private FilterListenerInterface filterListeners;

    public AutoCompleteAdapter(Context context, int resource, List<String> items)
    {
        super(context, resource, 0, items);

        tempItems = new ArrayList<>(items);
        suggestions = new ArrayList<>();
    }

    public void setFilterListeners(FilterListenerInterface filterFinishedListener)
    {
        filterListeners = filterFinishedListener;
    }

    @Override
    public Filter getFilter()
    {
        return nameFilter;
    }

    Filter nameFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            if (constraint != null)
            {
                suggestions.clear();
                for (String names : tempItems)
                {
                    if (names.toLowerCase().startsWith(constraint.toString().toLowerCase()))
                    {
                        suggestions.add(names);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            }
            else
            {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            List<String> filterList = (ArrayList<String>) results.values;

            if (filterListeners != null && filterList!= null)
                filterListeners.filteringFinished(filterList.size());

            if (results != null && results.count > 0)
            {
                clear();
                for (String item : filterList)
                {
                    add(item);
                    notifyDataSetChanged();
                }
            }
        }
    };
}
