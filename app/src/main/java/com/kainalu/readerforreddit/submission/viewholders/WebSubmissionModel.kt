package com.kainalu.readerforreddit.submission.viewholders

import com.airbnb.epoxy.EpoxyModelClass
import com.kainalu.readerforreddit.R

@EpoxyModelClass(layout = R.layout.submission_web_item)
abstract class WebSubmissionModel : BaseSubmissionModel<WebSubmissionHolder>()

class WebSubmissionHolder : BaseHolder()
