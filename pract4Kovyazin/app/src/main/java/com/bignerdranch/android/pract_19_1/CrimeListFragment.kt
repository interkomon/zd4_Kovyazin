package com.bignerdranch.android.pract_19_1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CrimeListFragment"
class CrimeListFragment : Fragment() {

    private val common = 0
    private val serious = 3
    private lateinit var recyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProviders.of(this).get(CrimeListViewModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)
        recyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter=adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeListViewModel.crimeListLiveData?.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer {
                    crimes->crimes.let { Log.i(TAG,"Количество: ${crimes.size}")
                updateUI(crimes) }
            }
        )
    }
    private fun updateUI(crimes:List<Crime>){
        adapter=CrimeAdapter(crimes)
        recyclerView.adapter=adapter
    }

    companion object {
        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }
    /*private inner class CommonCrimeHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        init {
            itemView.setOnClickListener(this)
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }
        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT)
                .show()
        }
    }
    private inner class SeriousCrimeHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var crime: Crime

        private val callPolice: Button = itemView.findViewById(R.id.call_police)
        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)

        init {
            itemView.setOnClickListener(this)
            callPolice.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Warning")
                builder.setMessage("Are you sure you want to contact the police?")
                builder.setPositiveButton("Yes")
                { _, _ ->
                    Toast.makeText(context, "You called the police", Toast.LENGTH_SHORT).show()
                }
                builder.setNegativeButton("Cancel", null)
                builder.create()
                builder.show()
            }
        }

        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = this.crime.title
            dateTextView.text = this.crime.date.toString()
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
        }
    }*/
    private inner class CrimeAdapter(var crimes: List<Crime>):RecyclerView.Adapter<CrimeHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CrimeHolder {
            val view=layoutInflater.inflate(R.layout.list_item_common_crime,parent,false)
            return CrimeHolder(view)
        }


        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {

            val crime=crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }
    }
    private inner class CrimeHolder(view: View):RecyclerView.ViewHolder(view), View.OnClickListener {
        private lateinit var crime: Crime
        val titleTextView: TextView =itemView.findViewById(R.id.crime_title)
        val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        init {
            itemView.setOnClickListener(this)
        }
        fun bind(crime:Crime){
            this.crime=crime
            titleTextView.text=this.crime.title
            dateTextView.text=this.crime.date.toString()
        }
        override fun onClick(v: View) {

        }


    }


}
