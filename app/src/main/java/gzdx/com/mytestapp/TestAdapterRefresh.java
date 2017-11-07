package gzdx.com.mytestapp;

import android.content.Context;

import com.xrefresh_library.Refresh.base.BaseLoadMoreViewAdapter;
import com.xrefresh_library.Refresh.base.BaseRecViewHolder;

import java.util.List;

/**
 * Created by liub on 2017/11/7 .
 */

public class TestAdapterRefresh extends BaseLoadMoreViewAdapter<String> {

    public TestAdapterRefresh(Context context, List<String> list) {
        super(context, R.layout.item_test_refresh_list, list);
    }

    @Override
    public void onBindViewHolder(BaseRecViewHolder viewHolder, String var2, int var3) {
        viewHolder.getTextView(R.id.item_name).setText(var2);
        viewHolder.getTextView(R.id.item_desc).setText(var2);

    }
}
