package com.dipuguide.finslice.presentation.screens.onBoard

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dipuguide.finslice.R

class OnBoardingViewModel : ViewModel() {
    fun getIntroPagerData(context: Context): List<IntroPagerModel> {
        return listOf(
            IntroPagerModel(
                image = R.drawable.image_1,
                title = context.getString(R.string.title_1),
                description = context.getString(R.string.desc_1)
            ),

            IntroPagerModel(
                image = R.drawable.image_2,
                title = context.getString(R.string.title_2),
                description = context.getString(R.string.desc_2)
            ),

            IntroPagerModel(
                image = R.drawable.image_3,
                title = context.getString(R.string.title_3),
                description = context.getString(R.string.desc_3)
            ),

            IntroPagerModel(
                image = R.drawable.image_4,
                title = context.getString(R.string.title_4),
                description = context.getString(R.string.desc_4)
            ),
            IntroPagerModel(
                image = R.drawable.image_5,
                title = context.getString(R.string.title_5),
                description = context.getString(R.string.desc_5)
            )
        )

    }

}