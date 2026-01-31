package com.dipuguide.finslice.presentation.screens.starter.onBoard

import android.content.Context
import androidx.lifecycle.ViewModel
import com.dipuguide.finslice.R
import com.dipuguide.finslice.domain.model.IntroPagerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class OnBoardingViewModel @Inject constructor() : ViewModel() {

    val introPagerData = listOf(
        IntroPagerModel(
            image = R.drawable.image_1,
            titleResId = R.string.title_1,
            descriptionResId = R.string.desc_1
        ),
        IntroPagerModel(
            image = R.drawable.image_2,
            titleResId = R.string.title_2,
            descriptionResId = R.string.desc_2
        ),
        IntroPagerModel(
            image = R.drawable.image_3,
            titleResId = R.string.title_3,
            descriptionResId = R.string.desc_3
        ),
        IntroPagerModel(
            image = R.drawable.image_4,
            titleResId = R.string.title_4,
            descriptionResId = R.string.desc_4
        ),
        IntroPagerModel(
            image = R.drawable.image_5,
            titleResId = R.string.title_5,
            descriptionResId = R.string.desc_5
        )
    )

}