package com.jeanpaulo.reignandroidtest.view.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLinearLayoutManager(context: Context) : LinearLayoutManager(context) {

    constructor(
        context: Context, @RecyclerView.Orientation orientation: Int,
        reverseLayout: Boolean
    ) : this(context){
        setOrientation(orientation)
        setReverseLayout(reverseLayout)
    }


    override fun supportsPredictiveItemAnimations(): Boolean {
        return false
    }

}