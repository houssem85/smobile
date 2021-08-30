package fr.strada.smobile.ui.auth.tenant

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.strada.smobile.R
import fr.strada.smobile.ui.auth.tenant.CompanyAdapter.MyViewHolder
import kotlinx.android.synthetic.main.item_company.view.*

class CompanyAdapter(var companies: ArrayList<Companyitem>, val click: (Int) -> Unit) :
    RecyclerView.Adapter<MyViewHolder?>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_company, parent, false)
        return MyViewHolder(view)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val companie = companies[position]
        if (companie.isSelected) {
            holder.iconCheck.visibility = View.VISIBLE
        } else {
            holder.iconCheck.visibility = View.INVISIBLE
        }
        holder.companieName.text = companie.name
        holder.itemView.setOnClickListener {
            click(position)
        }
    }
    override fun getItemCount(): Int {
        return companies.size
    }
    class MyViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        val companieName = itemview.companyname
        val iconCheck = itemview.imageViewCheck
    }
}
data class Companyitem(val name: String,val tenant : String, var isSelected: Boolean = false)