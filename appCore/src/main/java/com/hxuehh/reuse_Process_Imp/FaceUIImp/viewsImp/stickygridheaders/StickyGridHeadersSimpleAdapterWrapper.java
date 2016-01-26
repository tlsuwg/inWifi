/*
 Copyright 2013 Tonic Artos

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */

package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.stickygridheaders;

import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter wrapper to insert extra views and otherwise hack around GridView to
 * add sections and headers.
 * 
 * @author Tonic Artos
 */
public class StickyGridHeadersSimpleAdapterWrapper extends BaseAdapter implements
        StickyGridHeadersBaseAdapter {
    private StickyGridHeadersSimpleAdapter mDelegate;
    private HeaderData headerfooter;
    private HeaderData[] mHeaders;
    private List<HeaderData> headers;
    private boolean mfooter=false;
    public StickyGridHeadersSimpleAdapterWrapper(StickyGridHeadersSimpleAdapter adapter) {
    	
        mDelegate = adapter;
        adapter.registerDataSetObserver(new DataSetObserverExtension());

        headers = generateHeaderList(adapter);
        if(!mfooter){
            mHeaders=headers.toArray(new HeaderData[headers.size()]);
        }

        
    }

    @Override
    public int getCount() {
        return mDelegate.getCount();
    }

    @Override
    public int getCountForHeader(int position) {
        return mHeaders[position].getCount();
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        return mDelegate.getHeaderView(mHeaders[position].getRefPosition(), convertView, parent);
    }

    @Override
    public Object getItem(int position) {
        return mDelegate.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return mDelegate.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegate.getItemViewType(position);
    }

    @Override
    public int getNumHeaders() {
        return mHeaders.length;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return mDelegate.getView(position, convertView, parent);
    }

    @Override
    public int getViewTypeCount() {
        return mDelegate.getViewTypeCount();
    }

    @Override
    public boolean hasStableIds() {
        return mDelegate.hasStableIds();
    }
    public void addFooterData(boolean foot){
        mfooter=foot;
    	if(foot){
    		 headerfooter = new HeaderData(mDelegate.getCount()-1);
    	    	headers.add(headerfooter);
    	    	
    	}
    	mHeaders=headers.toArray(new HeaderData[headers.size()]);
	 
 }
    protected List<HeaderData> generateHeaderList(StickyGridHeadersSimpleAdapter adapter) {
        Map<String, HeaderData> mapping = new HashMap<String, HeaderData>();
        List<HeaderData> headers = new ArrayList<HeaderData>();

        for (int i = 0; i < adapter.getCount(); i++) {
            String headerId = adapter.getHeaderId(i);
            HeaderData headerData = mapping.get(headerId);
            if (headerData == null) {
                headerData = new HeaderData(i);
                headers.add(headerData);
            }
            headerData.incrementCount();
            mapping.put(headerId, headerData);
        }
        /*HeaderData headerfooter = new HeaderData(adapter.getCount()-1);
        headers.add(headerfooter);*/
      //  return headers.toArray(new HeaderData[headers.size()]);
        return headers;
    }

    private final class DataSetObserverExtension extends DataSetObserver {
        @Override
        public void onChanged() {
        	headers = generateHeaderList(mDelegate);
        	if(headerfooter!=null){
        		headers.add(headerfooter);
        	}
        	mHeaders=headers.toArray(new HeaderData[headers.size()]);
            notifyDataSetChanged();
        }

        @Override
        public void onInvalidated() {
        	headers = generateHeaderList(mDelegate);
        	if(headerfooter!=null){
        		headers.add(headerfooter);
        	}
        	mHeaders=headers.toArray(new HeaderData[headers.size()]);
            notifyDataSetInvalidated();
        }
    }

    private class HeaderData {
        private int mCount;

        private int mRefPosition;

        public HeaderData(int refPosition) {
            mRefPosition = refPosition;
            mCount = 0;
        }

        public int getCount() {
            return mCount;
        }

        public int getRefPosition() {
            return mRefPosition;
        }

        public void incrementCount() {
            mCount++;
        }
    }

	@Override
	public View getFooterView(int mHeader, View tag, ViewGroup parent) {
		// TODO Auto-generated method stub
		return mDelegate.getFooterView();
	}
}
