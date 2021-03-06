package com.example.sztangli.widgedemo.expandabelListView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sztangli.widgedemo.R;

import java.util.List;

/**
 * Created by sztangli on 2017/7/7.
 */

public class AnimationAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private Context mContext;
    private List<String> parentList;
    private List<String> childList;
    public AnimationAdapter(Context mContext,List<String> parentList,List<String> childList){
        this.mContext=mContext;
        this.parentList=parentList;
        this.childList=childList;
    }

    @Override
    public int getGroupCount() {
        return parentList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return parentList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        AnimationAdapter.ParentViewHolder parentViewHolder=null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_help_parent, null);
            parentViewHolder=new AnimationAdapter.ParentViewHolder();
            parentViewHolder.helpTitle= (TextView) convertView.findViewById(R.id.help_title);
            parentViewHolder.indicator= (ImageView) convertView.findViewById(R.id.help_indicator);
            convertView.setTag(parentViewHolder);
        }else {
            parentViewHolder= (AnimationAdapter.ParentViewHolder) convertView.getTag();
        }

        if(isExpanded){
            parentViewHolder.indicator.setImageResource(R.mipmap.guanbi);
            parentViewHolder.helpTitle.setTextColor(mContext.getResources().
                    getColor(R.color.list_text_orange_bg));
        }else{
            parentViewHolder.indicator.setImageResource(R.mipmap.dakai);
            parentViewHolder.helpTitle.setTextColor(mContext.getResources().
                    getColor(R.color.black));
        }
        parentViewHolder.helpTitle.setText(parentList.get(groupPosition));

        return convertView;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        AnimationAdapter.ChildViewHolder childViewHolder=null;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.item_help_child, null);
            childViewHolder=new AnimationAdapter.ChildViewHolder();
            childViewHolder.helpContent= (TextView) convertView.findViewById(R.id.help_content);
            convertView.setTag(childViewHolder);
        }else {
            childViewHolder= (AnimationAdapter.ChildViewHolder) convertView.getTag();
        }

        childViewHolder.helpContent.setText(childList.get(groupPosition));
        return convertView;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


    class ParentViewHolder {
        TextView helpTitle;
        ImageView indicator;
    }

    class ChildViewHolder{
        TextView helpContent;
    }
}
