package com.kainalu.readerforreddit.submission

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.kainalu.readerforreddit.models.LinkData
import com.kainalu.readerforreddit.models.LinkDataImpl
import com.kainalu.readerforreddit.network.ApiService
import com.kainalu.readerforreddit.network.Resource
import com.kainalu.readerforreddit.network.models.Link
import com.kainalu.readerforreddit.network.models.SubmissionItem
import com.kainalu.readerforreddit.tree.*
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubmissionRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getSubmission(
        subreddit: String,
        threadId: String,
        sort: SubmissionSort
    ): LiveData<Resource<LinkNode>> = liveData {
        emit(Resource.Loading<LinkNode>(null))
        try {
            val submission = apiService.getSubmission(subreddit, threadId, sort.urlString)

            if (submission.size != 2) {
                Log.e(TAG, "Response should contain 2 items. Found ${submission.size} items.")
                emit(Resource.Error<LinkNode>(null, null))
                return@liveData
            }

            val link = submission.first().children.first() as Link
            val comments = submission.last().children
            emit(Resource.Success(LinkNode(LinkDataImpl(link), comments)))
        } catch (e: HttpException) {
            emit(Resource.Error(error = e))
        }
    }

    suspend fun loadChildren(
        linkData: LinkData,
        sort: SubmissionSort,
        moreNode: MoreNode
    ): LiveData<Resource<Any>> = liveData {
        val ids = moreNode.childIds.take(LOAD_CHILDREN_LIMIT)
        moreNode.childIds.removeAll(ids)

        emit(Resource.Loading<Any>(null))
        try {
            val children = apiService.getChildren(
                children = TextUtils.join(",", ids),
                linkId = linkData.name,
                sort = sort.urlString
            )

            val rootNodes = buildTree(children)
            // We can only have one top level more node but if we are loading from a top level
            // more node, we may not have loaded all its childIds so we need to merge the old
            // more node with the new ones
            var rootMoreNode: MoreNode = moreNode
            var hasRootMoreNode = moreNode.childIds.size > 0
            rootNodes.forEach {
                if (it is MoreNode) {
                    hasRootMoreNode = true
                    rootMoreNode = rootMoreNode.merge(it)
                }
            }
            val newNodes = rootNodes.filter { it !is MoreNode }.toMutableList()
            if (hasRootMoreNode) {
                newNodes.add(rootMoreNode)
            }
            moreNode.parent!!.replaceChild(moreNode, newNodes)
            emit(Resource.Success<Any>(Any()))
        } catch (e: HttpException) {
            Log.e(TAG, e.message(), e)
            emit(Resource.Error<Any>(null, null))
        }
    }

    /**
     * Builds a list of trees from a flat list of comments.
     * This is needed because the /api/morechildren endpoint returns a flat list instead
     * of a tree.
     */
    private fun buildTree(items: List<SubmissionItem>): List<AbstractNode> {
        // https://stackoverflow.com/questions/444296/how-to-efficiently-build-a-tree-from-a-flat-structure
        // Create a map of ids to nodes, then loop over the nodes and attach them to their parent.
        // We use a node depth of 0 to indicate that a node is a root node.
        val lookup = mutableMapOf<String, AbstractNode>()
        items.forEach { item ->
            lookup[item.name] = NodeFactory.create(item)
        }

        lookup.values.forEach { node ->
            if (node is NestedItem) {
                val possibleParent = lookup[node.parentId]
                if (possibleParent != null) {
                    possibleParent.addChild(node)
                    // Mark the parent as a possible root node if we have not determined
                    // if it is a root yet and mark the node as a child node
                    if (possibleParent.depth == -1) {
                        possibleParent.depth = 0
                    }
                    node.depth = possibleParent.depth + 1
                } else {
                    // If there is no parent, the node is a root node
                    node.depth = 0
                }
            }
        }

        return lookup.values.filter { it.depth == 0 }
    }

    companion object {
        private const val TAG = "SubmissionRepository"
        private const val LOAD_CHILDREN_LIMIT = 100
    }
}