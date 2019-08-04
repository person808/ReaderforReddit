package com.kainalu.readerforreddit.tree

import com.kainalu.readerforreddit.models.LinkData

class LinkNode(linkData: LinkData, depth: Int) : AbstractNode(depth), LinkData by linkData
