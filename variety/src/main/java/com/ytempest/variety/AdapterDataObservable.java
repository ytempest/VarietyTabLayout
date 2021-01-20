package com.ytempest.variety;

import android.database.Observable;

/**
 * @author heqidu
 * @since 2021/1/15
 */
public class AdapterDataObservable extends Observable<AdapterDataObserver> {

    public void notifyDataSetChanged() {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onDataSetChanged();
        }
    }

    public void notifyItemChanged(int position) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onItemChanged(position);
        }
    }

    public void notifyItemInserted(int position) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onItemInserted(position);
        }
    }

    public void notifyItemRemoved(int position) {
        for (int i = mObservers.size() - 1; i >= 0; i--) {
            mObservers.get(i).onItemRemoved(position);
        }
    }
}
