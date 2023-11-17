package id.emergence.wher.utils.adapter

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import id.emergence.wher.databinding.ItemUserBinding
import id.emergence.wher.domain.model.User
import id.emergence.wher.ext.hashEmail
import id.emergence.wher.utils.adapter.UserPagingAdapter.UserViewHolder
import id.emergence.wher.utils.viewbinding.viewBinding

class UserPagingAdapter(
    private val imageLoader: ImageLoader,
    private val onClick: (User) -> Unit,
) : PagingDataAdapter<User, UserViewHolder>(USER_DIFF) {
    companion object {
        val USER_DIFF =
            object : DiffUtil.ItemCallback<User>() {
                override fun areItemsTheSame(
                    oldItem: User,
                    newItem: User,
                ): Boolean = oldItem.userId == newItem.userId

                override fun areContentsTheSame(
                    oldItem: User,
                    newItem: User,
                ): Boolean = oldItem == newItem
            }
    }

    inner class UserViewHolder(
        private val binding: ItemUserBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: User?) {
            if (data == null) return
            with(binding) {
                root.setOnClickListener { onClick(data) }
                val imgUrl = if(data.photoUrl.isNotEmpty()) {
                    data.photoUrl
                }else if(data.email.isNotEmpty()) {
                    hashEmail(data.email)
                }else {
                    "https://placekitten.com/144/144"
                }
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
                tvUsername.text = data.username
                tvDisplayName.text = data.name
            }
        }
    }

    override fun onBindViewHolder(
        holder: UserViewHolder,
        position: Int,
    ) {
        val data = getItem(position)
        holder.bind(data)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): UserViewHolder = UserViewHolder(parent.viewBinding(ItemUserBinding::inflate))
}
