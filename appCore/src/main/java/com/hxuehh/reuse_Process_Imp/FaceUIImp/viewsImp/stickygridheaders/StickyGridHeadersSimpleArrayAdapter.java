package com.hxuehh.reuse_Process_Imp.FaceUIImp.viewsImp.stickygridheaders;///*
// Copyright 2013 Tonic Artos
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// */
//
//package com.tuan800.framework.dataFaceLoadView.views.stickygridheaders;
//
//import android.app.Activity;
//import android.content.Context;
//import android.graphics.Color;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.BaseAdapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import Analytics;
//import com.tuan800.framework.app.devInfo.ScreenUtil;
//import com.tuan800.framework.dataFaceLoadView.faceUI.views.ViewKeys;
//import com.tuan800.framework.develop.LogUtil;
//import Image13lLoader;
//
//import SuApplication;
//import com.hxuehh.rebirth.beans.SellTipTable;
//import com.hxuehh.rebirth.models.faceBeans.Goods;
//import AnalyticsInfo;
//import DateUtil;
//import SignSellTipUtil;
//import Tao800Util;
//
//
//import java.util.Arrays;
//import java.util.List;
//
///**
// * @param <T>
// * @author Tonic Artos
// */
//public class StickyGridHeadersSimpleArrayAdapter<T> extends BaseAdapter implements
//        StickyGridHeadersSimpleAdapter {
//    protected static final String TAG = StickyGridHeadersSimpleArrayAdapter.class.getSimpleName();
//
//    private int mHeaderResId;
//    private LayoutInflater mInflater;
//    private int mItemResId;
//
//    private List<T> mItems;
//    private Context mContext;
//    private int viewtype;
//
//    public StickyGridHeadersSimpleArrayAdapter(Context context, List<T> items, int headerResId,
//                                               int itemResId ,int viewtype) {
//        this.viewtype=viewtype;
//        select_device_type(context, items, headerResId, itemResId);
//    }
//
//    public StickyGridHeadersSimpleArrayAdapter(Context context, T[] items, int headerResId,
//                                               int itemResId) {
//        select_device_type(context, Arrays.asList(items), headerResId, itemResId);
//    }
//
//    @Override
//    public boolean areAllItemsEnabled() {
//        return false;
//    }
//
//    @Override
//    public int getCount() {
//        return mItems.size();
//    }
//
//    @Override
//    public String getHeaderId(int position) {
//        Goods item = (Goods) getItem(position);
//        /*CharSequence value;
//        if (item instanceof CharSequence) {
//            value = (CharSequence)item;
//        } else {
//            value = item.toString();
//        }
//*/
//        return item.begin_time;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public View getHeaderView(int position, View convertView, ViewGroup parent) {
//        HeaderViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(mHeaderResId, parent, false);
//            holder = new HeaderViewHolder();
//            holder.textView = (TextView) convertView.findViewById(R.id.headertitle);
//            holder.textViewtime = (TextView) convertView.findViewById(R.id.headertitletime);
//            convertView.setTag(holder);
//        } else {
//            holder = (HeaderViewHolder) convertView.getTag();
//        }
//
//        Goods item = (Goods) getItem(position);
////        CharSequence string;
////        if (item instanceof CharSequence) {
////            string = (CharSequence)item;
////        } else {
////            string = item.toString();
////        }
//
//        // set header text as first char in string
//        String begintime = item.begin_time.trim();
//        // String ymd=begintime.substring(0,);
//        String[] total = begintime.split(" ");
//
//        holder.textView.setText("【" + total[0] + "】");
//        holder.textViewtime.setTextColor(Color.RED);
//        holder.textViewtime.setText(total[1]+"开抢");
//        return convertView;
//    }
//
//    @Override
//    public T getItem(int position) {
//        return mItems.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    @SuppressWarnings("unchecked")
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        if(viewtype== ViewKeys.AutoView_PADSTIDIYView_4){
//         convertView=getFeatureNoticeView(position,convertView, parent);
//        }
//         else if (viewtype== ViewKeys.AutoView_PADSTIDIYView){
//            convertView=getSpecialView(position, convertView, parent);
//        }
//        return convertView;
//        //Image13lLoader.getInstance().loadImage(((Deal)mItems.get(position)).image_url_normal, holder.imageView);
//
//    }
//    public View getSpecialView(int position, View convertView, ViewGroup parent){
//        final Goods deal = ((Goods) mItems.get(position));
//        final ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(mItemResId, parent, false);
//            holder = new ViewHolder();
//            holder.mainImage = (ImageView) convertView.findViewById(R.id.mainimage);
//            holder.newProduct = (ImageView) convertView.findViewById(R.id.newproduct);
//            holder.brand = (ImageView) convertView.findViewById(R.id.brand);
//            holder.img_clock = (ImageView) convertView.findViewById(R.id.img_clock);
//
//            holder.onlyphone = (TextView) convertView.findViewById(R.id.onlyphone);
//            holder.tv_item_branddetail = (TextView) convertView.findViewById(R.id.tv_item_branddetail);
//            holder.tv_deal_price = (TextView) convertView.findViewById(R.id.tv_deal_price);
//            holder.tv_deal_orginal_price = (TextView) convertView.findViewById(R.id.tv_deal_orginal_price);
//            holder.tv_deal_credits = (TextView) convertView.findViewById(R.id.tv_deal_credits);
//            holder.zhe = (TextView) convertView.findViewById(R.id.zhe);
//            holder.saletotal = (TextView) convertView.findViewById(R.id.saletotal);
//            holder.postage = (TextView) convertView.findViewById(R.id.postage);
//
//            holder.dealState= (ImageView) convertView.findViewById(R.id.iv_deal_state_special);
//
//            holder.mainImage.getLayoutParams().height = mesureImage();
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        //销售状态
//        holder.dealState.setVisibility(View.INVISIBLE);
//        if (deal.oos == 1) {//售光
//            if (DateUtil.afterNow(deal.begin_time)) {  // 未开始
//                holder.dealState.setVisibility(View.VISIBLE);
//                holder.dealState.setImageResource(R.drawable.ic_todaydeals_no_start);
//            } else {
//                holder.dealState.setVisibility(View.VISIBLE);
//                holder.dealState.setImageResource(R.drawable.ic_todaydeals_sell_out);
//            }
//        } else {//在售
//            if (DateUtil.afterNow(deal.begin_time)) { // 未开始
//                holder.dealState.setVisibility(View.VISIBLE);
//                holder.dealState.setImageResource(R.drawable.ic_todaydeals_no_start);
//            } else {
//                if (DateUtil.afterNow(deal.expire_time)) {
//                    holder.dealState.setVisibility(View.GONE);
//                } else {
//                    holder.dealState.setVisibility(View.VISIBLE);
//                    holder.dealState.setImageResource(R.drawable.ic_todaydeals_finish);
//
//                }
//            }
//        }
//        Image13lLoader.getInstance().loadImage(deal.image_url_hd2, holder.mainImage);
//        if ("1".equals(deal.deal_type)) {
//            holder.brand.setImageResource(R.drawable.specialsales);
//        } else {
//            if (deal.shop_type == 0) {
//                holder.brand.setImageResource(R.drawable.taobao);
//            } else if (deal.shop_type == 1) {
//                holder.brand.setImageResource(R.drawable.tmall);
//            }
//        }
//        if (deal.isZhuanXiang) {
//            holder.onlyphone.setVisibility(View.VISIBLE);
//        } else {
//            holder.onlyphone.setVisibility(View.GONE);
//        }
//
//        if(deal.today == 1){
//            holder.newProduct.setVisibility(View.VISIBLE);
//        }else{
//            holder.newProduct.setVisibility(View.GONE);
//        }
//
//        holder.tv_item_branddetail.setText(deal.title);
//        holder.tv_deal_price.setText("￥" + Tao800Util.getPrice(deal.price));
//        Tao800Util.setPaintFlags(holder.tv_deal_orginal_price);
//        holder.tv_deal_orginal_price.setText("￥" + Tao800Util.getPrice(deal.list_price));
//        if (deal.isBackIntegration) {
//            holder.tv_deal_credits.setText("+" + String.valueOf(getScore(deal)) + "积分");
//        } else {
//            holder.tv_deal_credits.setText("");
//        }
//        holder.zhe.setText(Tao800Util.getDiscount(deal.price, deal.list_price) + "折");
//     /*  String total= (deal.sales_count+"").trim();
//        if(total.length()>=5){
//          String wprice=total.substring(4);
//            String price=total.substring(0,4);
//
//        }*/
//
//        holder.saletotal.setText("已售" + Tao800Util.getSalesCount(deal.sales_count) + "件");
//        if (deal.isBaoYou) {
//            holder.postage.setText("包邮");
//        } else {
//            holder.postage.setText("");
//        }
//
//
//
//      /*  holder.mDealPriceTv.setVisibility(View.VISIBLE);
//        holder.mDealDiscountTv.setVisibility(View.VISIBLE);
//        holder.mDealPriceTv.setText("￥" + Tao800Util.getPrice(price));
//
//        Tao800Util.setPaintFlags(holder.mDealOrginalPriceTv);
//
//        String discount = Tao800Util.getDiscount(price, list_price);
//        if (!discount.equals("") && !discount.equals("10.0")) {
//            holder.mDealOrginalPriceTv.setVisibility(View.VISIBLE);
//            holder.mDealOrginalPriceTv.setText("￥" + Tao800Util.getPrice(list_price));
//            holder.mDealDiscountTv.setText(Tao800Util.getDiscount(price, list_price) + "折");
//        } else {
//            holder.mDealOrginalPriceTv.setVisibility(View.GONE);
//            holder.mDealDiscountTv.setText("热卖");
//        }
//*/
//        holder.img_clock.setVisibility(View.GONE);
//        if (isSetTip(deal)) {
//            holder.img_clock.setVisibility(View.VISIBLE);
//        }
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setClickable(false);
//
//                if (isSetTip(deal)) {
//                    tipCancleAuction(deal, v, holder.img_clock);
//                } else {
//                    tipAction(deal, v, holder.img_clock);
//                }
//            }
//        });
//
//        return convertView;
//    }
//
//
// /*   private static String getImageUrl(Goods_4 goods_4) {
//        if (SuApplication.netType != ConnectivityManager.TYPE_WIFI) {
//            return goods_4.image_url_hd1;
//        }
//
//        String imgUrl;
//
//        switch (ScreenUtil.getScreenDensity(Application.getInstance())) {
//            case ScreenUtil.DENSITY_720:
//                imgUrl = goods_4.image_url_hd2;
//                break;
//
//            case ScreenUtil.DENSITY_1080:
//                imgUrl = goods_4.image_url_hd3;
//                break;
//
//            default:
//                imgUrl = goods_4.image_url_hd1;
//                break;
//        }
//
//        return imgUrl;
//    }*/
//
//    public View getFeatureNoticeView(int position, View convertView, ViewGroup parent){
//        final Goods deal = ((Goods) mItems.get(position));
//        final ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(mItemResId, parent, false);
//            holder = new ViewHolder();
//            holder.mainImage = (ImageView) convertView.findViewById(R.id.mainimage);
//            holder.newProduct = (ImageView) convertView.findViewById(R.id.newproduct);
//            holder.brand = (ImageView) convertView.findViewById(R.id.brand);
//            holder.img_clock = (ImageView) convertView.findViewById(R.id.img_clock);
//
//            holder.onlyphone = (TextView) convertView.findViewById(R.id.onlyphone);
//            holder.tv_item_branddetail = (TextView) convertView.findViewById(R.id.tv_item_branddetail);
//            holder.tv_deal_price = (TextView) convertView.findViewById(R.id.tv_deal_price);
//            holder.tv_deal_orginal_price = (TextView) convertView.findViewById(R.id.tv_deal_orginal_price);
//            holder.tv_deal_credits = (TextView) convertView.findViewById(R.id.tv_deal_credits);
//            holder.zhe = (TextView) convertView.findViewById(R.id.zhe);
//            holder.saletotal = (TextView) convertView.findViewById(R.id.saletotal);
//            holder.postage = (TextView) convertView.findViewById(R.id.postage);
//            holder.dealState= (ImageView) convertView.findViewById(R.id.iv_deal_state_special);
//            holder.mainImage.getLayoutParams().height = mesureImage();
//            convertView.setTag(holder);
//
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.saletotal.setVisibility(View.GONE);
//        holder.postage.setVisibility(View.GONE);
//        holder.zhe.setVisibility(View.GONE);
//        //holder.dealState.setVisibility(View.GONE);
//        Image13lLoader.getInstance().loadImage(deal.image_url_hd2, holder.mainImage);
//        if ("1".equals(deal.deal_type)) {
//            holder.brand.setImageResource(R.drawable.specialsales);
//        } else {
//            if (deal.shop_type == 0) {
//                holder.brand.setImageResource(R.drawable.taobao);
//            } else if (deal.shop_type == 1) {
//                holder.brand.setImageResource(R.drawable.tmall);
//            }
//        }
//        if (deal.isZhuanXiang) {
//            holder.onlyphone.setVisibility(View.VISIBLE);
//        } else {
//            holder.onlyphone.setVisibility(View.GONE);
//        }
//
//        if(deal.today == 1){
//            holder.newProduct.setVisibility(View.VISIBLE);
//        }else{
//            holder.newProduct.setVisibility(View.GONE);
//        }
//        //销售状态
//        holder.dealState.setVisibility(View.INVISIBLE);
//        if (deal.oos == 1) {//售光
//            if (DateUtil.afterNow(deal.begin_time)) {  // 未开始
//                holder.dealState.setVisibility(View.VISIBLE);
//                holder.dealState.setImageResource(R.drawable.ic_todaydeals_no_start);
//            } else {
//                holder.dealState.setVisibility(View.VISIBLE);
//                holder.dealState.setImageResource(R.drawable.ic_todaydeals_sell_out);
//            }
//        } else {//在售
//            if (DateUtil.afterNow(deal.begin_time)) { // 未开始
//                holder.dealState.setVisibility(View.VISIBLE);
//                holder.dealState.setImageResource(R.drawable.ic_todaydeals_no_start);
//            } else {
//                if (DateUtil.afterNow(deal.expire_time)) {
//                    holder.dealState.setVisibility(View.GONE);
//                } else {
//                    holder.dealState.setVisibility(View.VISIBLE);
//                    holder.dealState.setImageResource(R.drawable.ic_todaydeals_finish);
//
//                }
//            }
//        }
//        holder.tv_item_branddetail.setText(deal.title);
//        holder.tv_deal_price.setText("￥" + Tao800Util.getPrice(deal.price));
//        Tao800Util.setPaintFlags(holder.tv_deal_orginal_price);
//        holder.tv_deal_orginal_price.setText("￥" + Tao800Util.getPrice(deal.list_price));
//        holder.tv_deal_credits.setText(Tao800Util.getDiscount(deal.price, deal.list_price) + "折");
//       /* if (deal.isBackIntegration) {
//            holder.tv_deal_credits.setText("+" + String.valueOf(getScore(deal)) + "积分");
//        } else {
//            holder.tv_deal_credits.setText("");
//        }*/
//        holder.zhe.setText(Tao800Util.getDiscount(deal.price, deal.list_price) + "折");
//     /*  String total= (deal.sales_count+"").trim();
//        if(total.length()>=5){
//          String wprice=total.substring(4);
//            String price=total.substring(0,4);
//
//        }*/
//
//        holder.saletotal.setText("已售" + Tao800Util.getSalesCount(deal.sales_count) + "件");
//        if (deal.isBaoYou) {
//            holder.postage.setText("包邮");
//        } else {
//            holder.postage.setText("");
//        }
//
//
//
//      /*  holder.mDealPriceTv.setVisibility(View.VISIBLE);
//        holder.mDealDiscountTv.setVisibility(View.VISIBLE);
//        holder.mDealPriceTv.setText("￥" + Tao800Util.getPrice(price));
//
//        Tao800Util.setPaintFlags(holder.mDealOrginalPriceTv);
//
//        String discount = Tao800Util.getDiscount(price, list_price);
//        if (!discount.equals("") && !discount.equals("10.0")) {
//            holder.mDealOrginalPriceTv.setVisibility(View.VISIBLE);
//            holder.mDealOrginalPriceTv.setText("￥" + Tao800Util.getPrice(list_price));
//            holder.mDealDiscountTv.setText(Tao800Util.getDiscount(price, list_price) + "折");
//        } else {
//            holder.mDealOrginalPriceTv.setVisibility(View.GONE);
//            holder.mDealDiscountTv.setText("热卖");
//        }
//*/
//        holder.img_clock.setVisibility(View.GONE);
//        if (isSetTip(deal)) {
//            holder.img_clock.setVisibility(View.VISIBLE);
//        }
//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setClickable(false);
//
//                if (isSetTip(deal)) {
//                    tipCancleAuction(deal, v, holder.img_clock);
//                } else {
//                    tipAction(deal, v, holder.img_clock);
//                }
//            }
//        });
//
//        return convertView;
//    }
//
//    private void tipAction(Goods deal, final View clickView, final ImageView imageView) {
//        Analytics.onEvent(mContext, AnalyticsInfo.EVENT_CLOCK, "d:" + deal.id);
//        if (SignSellTipUtil.sign(deal)) {
//            SignSellTipUtil.getDealSellTipCount(deal.id);
//            showRemindView(clickView, imageView);
//            Tao800Util.showShortToast(mContext, "设置成功,开抢5分钟前提示你哦～");
//        } else {
//            clickView.setClickable(true);
//        }
//    }
//
//    private void tipCancleAuction(Goods deal, final View clickView, final ImageView imageView) {
//        if (SignSellTipUtil.remove(deal)) {
//            hideRemindView(clickView, imageView);
//            Tao800Util.showShortToast(mContext, "取消提醒");
//        } else {
//            clickView.setClickable(true);
//        }
//    }
//
//    private void showRemindView(final View clickView, final ImageView imageView) {
//        imageView.setVisibility(View.VISIBLE);
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.remind_icon_out);
//        imageView.startAnimation(animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                clickView.setClickable(true);
//                imageView.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    private void hideRemindView(final View clickView, final ImageView imageView) {
//        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.remind_icon_in);
//        imageView.startAnimation(animation);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                clickView.setClickable(true);
//                imageView.setVisibility(View.GONE);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }
//
//    private boolean isSetTip(Goods mDeal) {
//        try {
//            Goods tipDeal = SellTipTable.getInstance().getGoods_4ById(mDeal.id);
//            if (tipDeal == null) {
//                return false;
//            } else {
//                return true;
//            }
//        } catch (Exception e) {
//            LogUtil.w(e);
//            return false;
//        }
//
//    }
//
//
//    private int mesureImage() {
//        if (ScreenUtil.WIDTH == 0) {
//            ScreenUtil.setDisplay((Activity) mContext);
//        }
//
//        return 374 * ((ScreenUtil.WIDTH - ScreenUtil.dip2px(mContext, 64)) / 4) / 342;
//    }
//
//    private int mesureImage(Activity context, int width, int height, Goods deal) {
//
//        if (ScreenUtil.WIDTH == 0) {
//            ScreenUtil.setDisplay(context);
//        }
//
//        if (width == 0 || height == 0) {
//            return deal.itemWidth;
//        } else {
//            return (deal.itemWidth * height) / width;
//        }
//    }
//
//    private void select_device_type(Context context, List<T> items, int headerResId, int itemResId) {
//        this.mItems = items;
//        mContext = context;
//        this.mHeaderResId = headerResId;
//        this.mItemResId = itemResId;
//        mInflater = LayoutInflater.from(context);
//    }
//
//    protected class HeaderViewHolder {
//        public TextView textView;
//        public TextView textViewtime;
//    }
//
//
//    protected class ViewHolder {
//        ImageView mainImage;
//        ImageView newProduct;
//        ImageView brand;
//        ImageView img_clock;
//        ImageView dealState;
//
//
//        TextView onlyphone;
//        TextView tv_item_branddetail;
//        TextView tv_deal_price;
//        TextView tv_deal_orginal_price;
//        TextView tv_deal_credits;
//
//        TextView zhe;
//        TextView saletotal;
//        TextView postage;
//    }
//
//    @Override
//    public View getFooterView() {
//        // TODO Auto-generated method stub
//        TextView v = new TextView(mContext);
//        v.setText("footer");
//        //v.setVisibility(View.GONE);
//        return v;
//    }
//
//    private int getScore(Goods deal) {
//        switch (SuApplication.userGrade) {
//            case 0:
//                return deal.z0Score;
//            case 1:
//                return deal.z1Score;
//            case 2:
//                return deal.z2Score;
//            case 3:
//                return deal.z3Score;
//            case 4:
//                return deal.z4Score;
//
//            default:
//                return deal.z4Score;
//        }
//    }
//}
