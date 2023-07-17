package com.yuriikonovalov.helia.presentation.utils

import com.yuriikonovalov.helia.R
import com.yuriikonovalov.helia.domain.valueobjects.Gender

val Gender.nameStringResId
    get() = when (this) {
        Gender.MALE -> R.string.gender_male
        Gender.FEMALE -> R.string.gender_female
    }