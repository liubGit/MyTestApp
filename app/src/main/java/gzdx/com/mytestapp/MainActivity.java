package gzdx.com.mytestapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;

import com.xrefresh_library.Refresh.widget.LoadMoreRecyclerView;
import com.xrefresh_library.Refresh.widget.RefreshLoadMoreView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        RefreshLoadMoreView loadMoreView = (RefreshLoadMoreView) findViewById(R.id.refresh_load_more);

        final List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add("第" + i + "条数据");
        }

        final TestAdapterRefresh adapterRefresh = new TestAdapterRefresh(this, list);
        loadMoreView.setAdapter(adapterRefresh);
        loadMoreView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        adapterRefresh.replace(list);
                    }
                });
            }
        });

        loadMoreView.setOnLoadMoreListener(new LoadMoreRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        adapterRefresh.addAll(list);
                    }
                });
            }
        });
    }

    private void initView() {
        findViewById(R.id.test_tbar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_tbar:

                break;
        }
    }
}
