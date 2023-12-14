package id.emergence.wher.utils.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import id.emergence.wher.databinding.ItemUserBinding
import id.emergence.wher.domain.model.FriendRequest
import id.emergence.wher.utils.adapter.FriendRequestPagingAdapter.FriendRequestViewHolder
import id.emergence.wher.utils.viewbinding.viewBinding

class FriendRequestPagingAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (FriendRequest) -> Unit,
) : PagingDataAdapter<FriendRequest, FriendRequestViewHolder>(FRIEND_REQUEST_DIFF) {
    companion object {
        val FRIEND_REQUEST_DIFF =
            object : DiffUtil.ItemCallback<FriendRequest>() {
                override fun areItemsTheSame(
                    oldItem: FriendRequest,
                    newItem: FriendRequest,
                ): Boolean = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: FriendRequest,
                    newItem: FriendRequest,
                ): Boolean = oldItem == newItem
            }
    }

    override fun onBindViewHolder(
        holder: FriendRequestViewHolder,
        position: Int,
    ) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FriendRequestViewHolder = FriendRequestViewHolder(parent.viewBinding(ItemUserBinding::inflate))

    inner class FriendRequestViewHolder(
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: FriendRequest?) {
            if (data == null) return
            with(binding) {
                root.setOnClickListener { onClick(data) }
                with(data.requester) {
                    ivAvatar.apply {
                        val imgData =
                            ImageRequest
                                .Builder(this.context)
                                .data(imgUrl)
                                .target(this)
                                .transformations(CircleCropTransformation())
                                .allowHardware(true)
                                .build()
                        imageLoader.enqueue(imgData)
                    }
                    tvUsername.text = this.username
                    tvDisplayName.text = this.name
                }
            }
        }
    }
}
