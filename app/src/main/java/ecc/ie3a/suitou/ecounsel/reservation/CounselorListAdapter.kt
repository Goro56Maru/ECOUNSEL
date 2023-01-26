package ecc.ie3a.suitou.ecounsel.reservation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ecc.ie3a.suitou.ecounsel.R

class CounselorListAdapter (private val CounselorData: MutableList<Counselor_Data>)
    : RecyclerView.Adapter<CounselorListAdapter.ListViewHolder>(){

    lateinit var listener: OnItemClickListener

    class ListViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var Counselor_Button: Button = view.findViewById(R.id.counselor_button)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val item = layoutInflater.inflate(R.layout.counselor_sell,parent, false)
        return ListViewHolder(item)
    }

    override fun getItemCount(): Int {
        return CounselorData.size
    }


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.Counselor_Button.text = CounselorData[position].CounselorName
        holder.Counselor_Button.setOnClickListener {
            listener.onItemClickListener(it, position, CounselorData[position])

        }
    }

    //インターフェースの作成
    interface OnItemClickListener{
        fun onItemClickListener(view: View, position: Int, clickedText: Counselor_Data) {
        }
    }

    // リスナー
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}