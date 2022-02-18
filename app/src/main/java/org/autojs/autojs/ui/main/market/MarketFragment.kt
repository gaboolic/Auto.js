package org.autojs.autojs.ui.main.market

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.stardust.autojs.workground.WrapContentLinearLayoutManager
import com.stardust.util.ClipboardUtil
import kotlinx.android.synthetic.main.fragment_market.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.autojs.autojs.R
import org.autojs.autojs.network.TopicService
import org.autojs.autojs.network.entity.topic.Topic
import org.autojs.autojs.ui.main.ViewPagerFragment
import org.autojs.autojs.ui.widget.AvatarView

class MarketFragment : ViewPagerFragment(0) {

    private val mTopics = ArrayList<Topic>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_market, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        topicsView.layoutManager = WrapContentLinearLayoutManager(context)
        topicsView.adapter = Adapter()
        swipeRefreshLayout.setOnRefreshListener {
            refresh()
        }
        refresh()
    }

    private fun refresh() {
        GlobalScope.launch(Dispatchers.Main) {
            swipeRefreshLayout.isRefreshing = true
            try {
                Log.d("market_fragment", "before call url")
                val topics = TopicService.getScriptsTopics()
                mTopics.clear()
                mTopics.addAll(topics)
                topicsView.adapter!!.notifyDataSetChanged()
                swipeRefreshLayout.isRefreshing = false
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onBackPressed(activity: Activity): Boolean {
        return false
    }

    override fun onFabClick(fab: FloatingActionButton) {

    }

    inner class TopicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private lateinit var topic: Topic

        private val rootView: TextView = itemView.findViewById(R.id.root)
        private val titleView: TextView = itemView.findViewById(R.id.title)
        private val avatarView: AvatarView = itemView.findViewById(R.id.avatar)
        private val usernameView: TextView = itemView.findViewById(R.id.username)
        private val dateView: TextView = itemView.findViewById(R.id.date)
        private val upvoteView: ImageText = itemView.findViewById(R.id.upvote)
        private val downvoteView: ImageText = itemView.findViewById(R.id.downvote)
        private val downloadView: ImageText = itemView.findViewById(R.id.download)
        private val starView: ImageText = itemView.findViewById(R.id.star)

        init {
            upvoteView.setOnClickListener {

            }
            downvoteView.setOnClickListener {

            }
            starView.setOnClickListener {

            }
            downloadView.setOnClickListener {
                ClipboardUtil.setClip(context, topic.titleRaw)
                Toast.makeText(context, "已复制", Toast.LENGTH_SHORT).show()
            }
        }

        fun bind(topic: Topic) {
            this.topic = topic
            rootView.setText("")
            titleView.text = topic.title
//            avatarView.setUser(topic.user)
//            usernameView.text = topic.user.username
//            dateView.text = DateTimeFormat.mediumDateTime().print(topic.timestamp.toLong())

        }
    }

    inner class Adapter : RecyclerView.Adapter<TopicViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicViewHolder {
            return TopicViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_topic, parent, false))
        }

        override fun getItemCount(): Int {
            return mTopics.size
        }

        override fun onBindViewHolder(holder: TopicViewHolder, position: Int) {
            val topic = mTopics[position]
            holder.bind(topic)
        }

    }
}