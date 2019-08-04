package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.models.LinkData

class LinkNode(linkData: LinkData) : AbstractNode(), LinkData by linkData
