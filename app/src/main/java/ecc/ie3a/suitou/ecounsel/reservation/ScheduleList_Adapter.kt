package ecc.ie3a.suitou.ecounsel.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ecc.ie3a.suitou.ecounsel.R

class ScheduleList_Adapter (private val Schedule_Data: MutableList<Schedule_Data>)
    : RecyclerView.Adapter<ScheduleList_Adapter.ListViewHolder>(){

    lateinit var listener: OnItemClickListener

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var Schedule_Time: TextView = view.findViewById(R.id.time)
        var Possible_View: LinearLayout = view.findViewById(R.id.possible_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.schedule_sell,parent, false)
        return ListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return Schedule_Data.size
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.Schedule_Time.text = Schedule_Data[position].Time
        if(!Schedule_Data[position].Possible){
           holder.Possible_View.visibility = LinearLayout.INVISIBLE
        }else{
            holder.Possible_View.visibility = LinearLayout.VISIBLE
        }

        holder.Possible_View.setOnClickListener {
            listener.onItemClickListener(it, position, Schedule_Data[position])
        }
    }

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: Schedule_Data) {
        }
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}