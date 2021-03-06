package com.example.sztangli.widgedemo.recycleListView;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.mvvmlibrary.base.data.BaseViewModel;
import com.google.android.material.snackbar.Snackbar;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sztangli.widgedemo.R;
import com.example.sztangli.widgedemo.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import com.example.easy_recycleview_library.adapter.BaseRecyclerViewAdapter;
import com.example.easy_recycleview_library.interfaces.OnItemClickListener;
import com.example.easy_recycleview_library.interfaces.OnItemLongClickListener;
import com.example.easy_recycleview_library.interfaces.OnLoadMoreListener;
import com.example.easy_recycleview_library.interfaces.OnNetWorkErrorListener;
import com.example.easy_recycleview_library.interfaces.OnRefreshListener;
import com.example.easy_recycleview_library.util.WeakHandler;
import com.guyuan.handlein.base.databinding.ItemPicScanBinding;
import com.guyuan.handlein.base.ui.fragment.BaseListFragment;

/**
 * Created by sztangli on 2017/8/15.
 */

public class RecycleFragment extends BaseListFragment<RecycleBean, ItemPicScanBinding, BaseViewModel> {

    public static final String TAG = "RecycleFragment";
    private Context context;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;
    private RecyclViewAdapter recyclViewAdapter;
    private WeakHandler handler = new WeakHandler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case -1:

                    int currentSize = recyclViewAdapter.getItemCount();

                    //模拟组装10个数据
                    List<RecycleBean> dataList = new ArrayList<>();
                    for (int i = 0; i < 10; i++) {
                        RecycleBean bean = new RecycleBean();
                        bean.setContent("data.add " + i);
                        dataList.add(bean);
                    }
                    recyclViewAdapter.addAll(dataList);
                    recycleView.refreshComplete(10);
                    recycleView.setNoMore(true);
                    recyclViewAdapter.notifyDataSetChanged();
                    break;
                case -2:
                    recyclViewAdapter.notifyDataSetChanged();
                    break;
                case -3:
                    recycleView.refreshComplete(10);
                    recyclViewAdapter.notifyDataSetChanged();
                    recycleView.setOnNetWorkErrorListener(new OnNetWorkErrorListener() {
                        @Override
                        public void reload() {
                            requestData();
                        }
                    });
                    break;
            }
        }
    };

    public static RecycleFragment newInstance() {

        Bundle args = new Bundle();

        RecycleFragment fragment = new RecycleFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initView() {

        context = getContext();

        recyclViewAdapter = new RecyclViewAdapter(context, listData, R.layout.item_recycleview);
        baseRecyclerViewAdapter = new BaseRecyclerViewAdapter(recyclViewAdapter);
        recycleView.setAdapter(baseRecyclerViewAdapter);

        recycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        recycleView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager
                .VERTICAL));

        View header = LayoutInflater.from(getContext()).inflate(R.layout.sample_header, (ViewGroup)
                rootView.getParent(), false);


        baseRecyclerViewAdapter.addHeaderView(header);
        baseRecyclerViewAdapter.addHeaderView(new SampleHeader(getContext()));


        baseRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(), "点击了", Toast.LENGTH_SHORT).show();
            }
        });

        baseRecyclerViewAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                Snackbar.make(view, "长按了", Snackbar.LENGTH_SHORT).show();
            }
        });

        empty_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 100; i++) {
                    RecycleBean bean = new RecycleBean();
                    bean.setContent("data " + i);
                    listData.add(bean);
                }
                recyclViewAdapter.notifyDataSetChanged();
            }
        });


        recycleView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recycleView.refreshComplete(10);
                    }
                }, 1000);
            }
        });

        recycleView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                requestData();
            }
        });

        recycleView.refresh();

    }

    @Override
    protected void refresh() {

    }

    @Override
    protected void loadMore() {

    }

    @Override
    protected boolean isPullEnable() {
        return false;
    }

    @Override
    protected boolean isLoadMoreEnable() {
        return false;
    }


    /**
     * 模拟请求网络
     */
    private void requestData() {

        new Thread() {

            @Override
            public void run() {
                super.run();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //模拟一下网络请求失败的情况
                if (NetworkUtils.isNetworkAvailable(getActivity())) {
                    handler.sendEmptyMessage(-1);
                } else {
                    handler.sendEmptyMessage(-3);
                }
            }
        }.start();
    }

    @Override
    protected int getVariableId() {
        return 0;
    }
}
