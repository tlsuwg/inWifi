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


import android.content.Context;
import android.database.DataSetObserver;
import android.hxuehh.com.R;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Adapter wrapper to insert extra views and otherwise hack around GridView to
 * add sections and headers.
 * @author Tonic Artos
 */
public class StickyGridHeadersBaseAdapterWrapper3 extends BaseAdapter {
	private static final int sNumViewTypes = 4;
	protected static final int ID_FILLER = -0x02;
	protected static final int ID_HEADER = -0x01;
	protected static final int ID_HEADER_FILLER = -0x03;
	protected static final int POSITION_FILLER = -0x01;
	protected static final int POSITION_HEADER = -0x04;
	protected static final int POSITION_HEADER_FILLER = -0x03;
	protected static final int VIEW_TYPE_FILLER = 0x00;
	protected static final int VIEW_TYPE_HEADER = 0x01;
	protected static final int VIEW_TYPE_HEADER_FILLER = 0x02;
	protected static final int ITEM_VIEW_TYPE_HEADER_OR_FOOTER_FILLER=-0x05;
	private boolean isHasHeader=false;
    private boolean footer=false;
	private final Context mContext;
	private int mCount;
	private boolean mCounted = false;
	FullWidthFixedViewLayout header;
	FixedViewInfo fillerView;
	private DataSetObserver mDataSetObserver = new DataSetObserver() {
		@Override
		public void onChanged() {
			updateCount();
		}

		@Override
		public void onInvalidated() {
			mCounted = false;
		}
	};
	private final StickyGridHeadersBaseAdapter mDelegate;
	private StickyGridHeadersGridView mGridView;
	private View mLastHeaderViewSeen;
	private View mLastViewSeen;
	private int mNumColumns = 1;

	public StickyGridHeadersBaseAdapterWrapper3(Context context,
			StickyGridHeadersGridView gridView,
			StickyGridHeadersBaseAdapter delegate) {
		mContext = context;
		mDelegate = delegate;
		mGridView = gridView;
		delegate.registerDataSetObserver(mDataSetObserver);
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	public void addHeader(View v) {
		
		fillerView = new FixedViewInfo();
		header = new FullWidthFixedViewLayout(mContext);
		header.addView(v);
	        fillerView.view = v;
	        fillerView.viewContainer = header;
	        
	        
	        
	        
		//header.addView(view);
		//mLastHeaderViewSeen = header;
		if (null != v) {
			isHasHeader = true;
		}
	}
    public boolean getIsHasHeader(){
        return isHasHeader;
    }
	@Override
	public int getCount() {
		if (mCounted) {
			return mCount;
		}
		mCount = 0;
		int numHeaders = mDelegate.getNumHeaders();
		if (numHeaders == 0) {
			if (isHasHeader) {
				mCount = mDelegate.getCount() + mNumColumns;
			} else {
				mCount = mDelegate.getCount();
			}

			mCounted = true;
			return mCount;
		}
		for (int i = 0; i < numHeaders; i++) {
			// Pad count with space for header and trailing filler in header
			// group.
			mCount += mDelegate.getCountForHeader(i)
					+ unFilledSpacesInHeaderGroup(i) + mNumColumns;
			
		}
		if (isHasHeader) {
			mCount = mCount + mNumColumns;
		} 
		mCounted = true;
		return mCount;
	}

	/**
	 * Get the data item associated with the specified position in the data set.
	 * <p>
	 * Since this wrapper inserts fake entries to fill out items grouped by
	 * header and also spaces to insert headers into some positions will return
	 * null.
	 * </p>
	 * 
	 * @param position
	 *            Position of the item whose data we want within the adapter's
	 *            data set.
	 * @return The data at the specified position.
	 */
	@Override
	public Object getItem(int position) throws ArrayIndexOutOfBoundsException {
		if(isHasHeader){
			
		}
		Position adapterPosition = translatePosition(position);
		if (adapterPosition.mPosition == POSITION_FILLER
				|| adapterPosition.mPosition == POSITION_HEADER) {
			// Fake entry in view.
			return null;
		}
		return mDelegate.getItem(adapterPosition.mPosition);
	}

	@Override
	public long getItemId(int position) {
		Position adapterPosition = translatePosition(position);
		if(adapterPosition.mPosition==AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER){
			return AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
		}
		if (adapterPosition.mPosition == POSITION_HEADER) {
			return ID_HEADER;
		}
		if (adapterPosition.mPosition == POSITION_FILLER) {
			return ID_FILLER;
		}
		if (adapterPosition.mPosition == POSITION_HEADER_FILLER) {
			return ID_HEADER_FILLER;
		}
		return mDelegate.getItemId(adapterPosition.mPosition);
	}

	@Override
	public int getItemViewType(int position) {
		Position adapterPosition = translatePosition(position);
		if(adapterPosition.mPosition==AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER){
			return AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER;
		}
		if (adapterPosition.mPosition == POSITION_HEADER) {
			return VIEW_TYPE_HEADER;
		}
		if (adapterPosition.mPosition == POSITION_FILLER) {
			return VIEW_TYPE_FILLER;
		}
		if (adapterPosition.mPosition == POSITION_HEADER_FILLER) {
			return VIEW_TYPE_HEADER_FILLER;
		}
		if(adapterPosition.mPosition==ITEM_VIEW_TYPE_HEADER_OR_FOOTER_FILLER){
			return ITEM_VIEW_TYPE_HEADER_OR_FOOTER_FILLER;
		}
		int itemViewType = mDelegate.getItemViewType(adapterPosition.mPosition);
		if (itemViewType == IGNORE_ITEM_VIEW_TYPE) {
			return itemViewType;
		}
		if(isHasHeader){
			return itemViewType +sNumViewTypes;
		}
		else{
			return itemViewType + 3;
		}
	
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Position adapterPosition = translatePosition(position);
		if(adapterPosition.mPosition ==AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER){
			return header;
		}
		if(adapterPosition.mPosition==ITEM_VIEW_TYPE_HEADER_OR_FOOTER_FILLER){
			   if (convertView == null) {
                   convertView = new View(parent.getContext());
               }
               convertView.requestLayout();
               // We need to do this because GridView uses the height of the last item
               // in a row to determine the height for the entire row.
               if (mNumColumns != 1) {
                   convertView.setVisibility(View.INVISIBLE);
               }
               if (header.getMeasuredHeight() != 0) {
                   convertView.setMinimumHeight(header.getHeight());
               } else {
                   //  footerViewContainer.measure(0,0);
                   convertView.setMinimumHeight(header.getHeight());
               }

               // convertView.setMinimumHeight(500);
               return convertView;
		}
		else if (adapterPosition.mPosition == POSITION_HEADER) {
			HeaderFillerView v = getHeaderFillerView(adapterPosition.mHeader,
					convertView, parent);
			View view = mDelegate.getHeaderView(adapterPosition.mHeader,
					(View) v.getTag(), parent);


            TextView viewssd=(TextView) view.findViewById(R.id.headertitle);
            TextView viewtime=(TextView) view.findViewById(R.id.headertitletime);
            final ImageView imageViewfooter=(ImageView) view.findViewById(R.id.footer);
            if((getCount()-mNumColumns)<=position&&position<getCount()&&footer){
                imageViewfooter.setVisibility(View.VISIBLE);

               /* imageViewfooter.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        imageViewfooter.setVisibility(View.VISIBLE);
                    }
                }, 1000);*/
                viewssd.setVisibility(View.GONE);
                viewtime.setVisibility(View.GONE);
            }else{
                imageViewfooter.setVisibility(View.GONE);
                viewssd.setVisibility(View.VISIBLE);
                viewtime.setVisibility(View.VISIBLE);
            }
			mGridView.detachHeader((View) v.getTag());
			v.setTag(view);
			mGridView.attachHeader(view);
			convertView = v;
			mLastHeaderViewSeen = v;
			v.forceLayout();
		} else if (adapterPosition.mPosition == POSITION_HEADER_FILLER) {
			convertView = getFillerView(convertView, parent,
					mLastHeaderViewSeen);
			convertView.forceLayout();
		} else if (adapterPosition.mPosition == POSITION_FILLER) {
			convertView = getFillerView(convertView, parent, mLastViewSeen);
		} else {
			convertView = mDelegate.getView(adapterPosition.mPosition,
					convertView, parent);
			mLastViewSeen = convertView;
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		if(isHasHeader){
			return mDelegate.getViewTypeCount() + sNumViewTypes;
		}
		else {
			return mDelegate.getViewTypeCount() + 3;
		}
		
	}

	/**
	 * @return the adapter wrapped by this adapter.
	 */
	public StickyGridHeadersBaseAdapter getWrappedAdapter() {
		return mDelegate;
	}

	@Override
	public boolean hasStableIds() {
		return mDelegate.hasStableIds();
	}

	@Override
	public boolean isEmpty() {
		return mDelegate.isEmpty();
	}

	@Override
	public boolean isEnabled(int position) {
		Position adapterPosition = translatePosition(position);
		if (adapterPosition.mPosition == POSITION_FILLER
				|| adapterPosition.mPosition == POSITION_HEADER) {
			return false;
		}
		return mDelegate.isEnabled(adapterPosition.mPosition);
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		super.registerDataSetObserver(observer);
		mDelegate.registerDataSetObserver(observer);
	}

	public void setNumColumns(int numColumns) {
		mNumColumns = numColumns;
		mCounted = false;
		// notifyDataSetChanged();
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		super.unregisterDataSetObserver(observer);
		mDelegate.unregisterDataSetObserver(observer);
	}

	private FillerView getFillerView(View convertView, ViewGroup parent,
			View lastViewSeen) {
		FillerView fillerView = (FillerView) convertView;
		if (fillerView == null) {
			fillerView = new FillerView(mContext);
		}

		fillerView.setMeasureTarget(lastViewSeen);

		return fillerView;
	}

	private HeaderFillerView getHeaderFillerView(int headerPosition,
			View convertView, ViewGroup parent) {
		HeaderFillerView headerFillerView = (HeaderFillerView) convertView;
		if (headerFillerView == null) {
			headerFillerView = new HeaderFillerView(mContext);
		}

		return headerFillerView;
	}

	/**
	 * Counts the number of items that would be need to fill out the last row in
	 * the group of items with the given header.
	 * 
	 * @param header
	 *            Header set of items are grouped by.
	 * @return The count of unfilled spaces in the last row.
	 */
	private int unFilledSpacesInHeaderGroup(int header) {
		// If mNumColumns is equal to zero we will have a divide by 0 exception
		if (mNumColumns == 0) {
			return 0;
		}

		int remainder = mDelegate.getCountForHeader(header) % mNumColumns;
		return remainder == 0 ? 0 : mNumColumns - remainder;
	}

	protected long getHeaderId(int position) {
		return translatePosition(position).mHeader;
	}

	protected View getHeaderView(int position, View convertView,
			ViewGroup parent) {
		if (mDelegate.getNumHeaders() == 0) {
			return null;
		}

		return mDelegate.getHeaderView(translatePosition(position).mHeader,
				convertView, parent);
	}

	protected Position translatePosition(int position) {
		
		if(isHasHeader){
			if(position<mNumColumns){
				if(position==0){
				return	new Position(AdapterView.ITEM_VIEW_TYPE_HEADER_OR_FOOTER, 0);
				}
				else{
					return new Position(ITEM_VIEW_TYPE_HEADER_OR_FOOTER_FILLER, 0);
				}
			}
		}
		
		
		int numHeaders = mDelegate.getNumHeaders();
		if (numHeaders == 0) {
			if (position >= mDelegate.getCount()) {
				return new Position(POSITION_FILLER, 0);
			}
			return new Position(position, 0);
		}

		// Translate GridView position to Adapter position.
		//除去header和填充的位置 即gridview中item text的位置
		int adapterPosition = position;
		//全局位置 所有的
		int place = position;
		if(isHasHeader){
			adapterPosition=position-mNumColumns;
			place=position-mNumColumns;
		}
		int i;

		for (i = 0; i < numHeaders; i++) {
			int sectionCount = mDelegate.getCountForHeader(i);

			// Skip past fake items making space for header in front of
			// sections.
			if (place == 0) {
				// Position is first column where header will be.
				return new Position(POSITION_HEADER, i);
			}
			place -= mNumColumns;
			if (place < 0) {
				// Position is a fake so return null.
				return new Position(POSITION_HEADER_FILLER, i);
			}
			adapterPosition -= mNumColumns;

			if (place < sectionCount) {
				return new Position(adapterPosition, i);
			}

			// Skip past section end of section row filler;
			int filler = unFilledSpacesInHeaderGroup(i);
			adapterPosition -= filler;
			place -= sectionCount + filler;

			if (place < 0) {
				// Position is a fake so return null.
				return new Position(POSITION_FILLER, i);
			}
		}

		// Position is a fake.
		return new Position(POSITION_FILLER, i);
	}

	protected void updateCount() {
		mCount = 0;
		int numHeaders = mDelegate.getNumHeaders();
		if (numHeaders == 0) {
			mCount = mDelegate.getCount();
			mCounted = true;
			if(isHasHeader){
				mCount=mCount+mNumColumns;
			}
			return;
		}
		for (int i = 0; i < numHeaders; i++) {
			mCount += mDelegate.getCountForHeader(i) + mNumColumns;
		}
		if(isHasHeader){
			mCount=mCount+mNumColumns;
		}
		mCounted = true;
	}
    private static class FixedViewInfo {
        /**
         * The view to add to the grid
         */
        public View view;
        public ViewGroup viewContainer;
        /**
         * The data backing the view. This is returned from {@link android.widget.ListAdapter#getItem(int)}.
         */
        public Object data;
        /**
         * <code>true</code> if the fixed view should be selectable in the grid
         */
        public boolean isSelectable;
    }

    private class FullWidthFixedViewLayout extends FrameLayout {
        public FullWidthFixedViewLayout(Context context) {
            super(context);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int targetWidth = mGridView.getMeasuredWidth()
                    - mGridView.getPaddingLeft()
                    - mGridView.getPaddingRight();
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(targetWidth,
                    MeasureSpec.getMode(widthMeasureSpec));
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
	/**
	 * Simple view to fill space in grid view.
	 * 
	 * @author Tonic Artos
	 */
	protected class FillerView extends View {
		private View mMeasureTarget;

		public FillerView(Context context) {
			super(context);
		}

		public FillerView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public FillerView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public void setMeasureTarget(View lastViewSeen) {
			mMeasureTarget = lastViewSeen;
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(
					mMeasureTarget.getMeasuredHeight(), MeasureSpec.EXACTLY);
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}

	/**
	 * A view to hold the section header and measure the header row height
	 * correctly.
	 * 
	 * @author Tonic Artos
	 */
	protected class HeaderFillerView extends FrameLayout {
		private int mHeaderId;

		public HeaderFillerView(Context context) {
			super(context);
		}

		public HeaderFillerView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public HeaderFillerView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
		}

		public int getHeaderId() {
			return mHeaderId;
		}

		/**
		 * Set the adapter id for this header so we can easily pull it later.
		 */
		public void setHeaderId(int headerId) {
			mHeaderId = headerId;
		}

		@Override
		protected LayoutParams generateDefaultLayoutParams() {
			return new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			View v = (View) getTag();
			ViewGroup.LayoutParams params = v.getLayoutParams();
			if (params == null) {
				params = generateDefaultLayoutParams();
				v.setLayoutParams(params);
			}
			if (v.getVisibility() != View.GONE) {
				int heightSpec = getChildMeasureSpec(
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						0, params.height);
				int widthSpec = getChildMeasureSpec(
						MeasureSpec.makeMeasureSpec(mGridView.getWidth(),
								MeasureSpec.EXACTLY), 0, params.width);
				v.measure(widthSpec, heightSpec);
			}
			setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
					v.getMeasuredHeight());
		}
	}
    public void addFooterData(boolean b) {
        // TODO Auto-generated method stub
        footer=b;
    }
	protected class HeaderHolder {
		protected View mHeaderView;
	}

	protected class Position {
		protected int mHeader;

		protected int mPosition;

		protected Position(int position, int header) {
			mPosition = position;
			mHeader = header;
		}
	}
}
