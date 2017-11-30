package gzdx.com.mytestapp.ui.widget.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liub on 2017/11/29.
 */

public abstract class BaseFragement extends Fragment {
    private Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(setContextView(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        bindView(view, savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    protected abstract int setContextView();

    protected abstract void bindView(View view, Bundle savedInstanceState);
}
