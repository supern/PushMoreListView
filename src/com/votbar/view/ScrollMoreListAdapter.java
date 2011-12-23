/**
 * 
 */
package com.votbar.view;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;

/**
 * @author sanping.li@alipay.com
 *
 */
public abstract class ScrollMoreListAdapter extends BaseAdapter implements OnItemClickListener{
    protected Context mContext;
    private View mFootView;
    private MoreListener mMoreListener;
    protected ArrayList<?> mListDatas;

    public ScrollMoreListAdapter(Context context,ArrayList<?> data) {
        mContext = context;
        mListDatas = data;
    }

    @Override
    public int getCount() {
        if(mListDatas==null)
            return 0;
        else if(hasMore())
            return mListDatas.size()+1;
        else
            return mListDatas.size();
    }

    @Override
    public Object getItem(int position) {
        if(position!=getCount()-1||!hasMore())
            return mListDatas.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      if(position==getCount()-1&&hasMore()){
          return getFoot();
      }else
          return getItemView(position, convertView, parent);
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        if(position!=getCount()-1||!hasMore())
            itemClick(arg0, arg1, position, arg3);
        
    }

    private View getFoot() {
        mFootView = LayoutInflater.from(mContext).inflate(R.layout.list_more, null);
        Button button = (Button) mFootView.findViewById(R.id.query_retry);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reset();
                if(mMoreListener!=null)
                    mMoreListener.onMore();
            }
        });
        return mFootView;
    }
    
    /**
     * 获取更多失败
     */
    public void getMorefailed(){
        if(mFootView!=null){
            View failView = mFootView.findViewById(R.id.list_more_fail);
            View loadingView = mFootView.findViewById(R.id.list_more_loading);
            failView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.INVISIBLE);
        }
    }

    public void reset(){
        if(mFootView!=null){
            View failView = mFootView.findViewById(R.id.list_more_fail);
            View loadingView = mFootView.findViewById(R.id.list_more_loading);
            failView.setVisibility(View.INVISIBLE);
            loadingView.setVisibility(View.VISIBLE);
        }
    }
    
    
    @Override
    public int getItemViewType(int position) {
        if(position==getCount()-1&&hasMore())
            return 0;
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public MoreListener getMoreListener() {
        return mMoreListener;
    }

    public void setMoreListener(MoreListener moreListener) {
        mMoreListener = moreListener;
    }

    /**
     * 获取更多接口
     *
     */
    public interface MoreListener{
        /**
         * 获取更多回调函数
         */
        public void onMore();
    }
    
    /**
     * 是否还有更多
     */
    protected abstract boolean hasMore();
    /**
     * 获取每项的视图
     */
    protected abstract View getItemView(int position, View convertView, ViewGroup parent);
    /**
     * 点击一项
     */
    protected abstract void itemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

}
