package com.bignerdranch.android.pract_19_1


import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract

import android.text.Editable
import android.text.TextWatcher
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


private const val DATE_FORMAT = "EEE, MMM, dd"
private const val REQUEST_CONTACT=1
class CrimeFragment : Fragment() {

    private lateinit var crime: Crime
    private lateinit var title: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox
    private lateinit var reportButton: Button
    private lateinit var suspectButton: Button
    private lateinit var listButton: Button
    private lateinit var addButoon: Button
    private val crimeDetailViewModel:CrimeDatailModel by lazy{
        ViewModelProviders.of(this).get(CrimeDatailModel::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        crime = Crime()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_crime, container, false)
        title = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        reportButton=view.findViewById(R.id.crime_report) as Button
        suspectButton=view.findViewById(R.id.crime_suspect) as Button
//        listButton = view.findViewById(R.id.listButton) as Button
        addButoon = view.findViewById(R.id.addButton) as Button

        dateButton.apply { text = crime.date.toString(); isEnabled = false }
        return view
    }


    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int,
                                           after: Int) {}

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int,
                                       count: Int) {
                crime.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {}
        }
        solvedCheckBox.setOnClickListener(){
            suspectButton.isEnabled = solvedCheckBox.isChecked
            reportButton.isEnabled = solvedCheckBox.isChecked
        }
        title.addTextChangedListener(titleWatcher)
        solvedCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                crime.isSolved = isChecked
            }
        }
        reportButton.setOnClickListener {
            Intent(Intent.ACTION_SEND).apply{
                type="text/plain"
                putExtra(Intent.EXTRA_TEXT,getCrimeReport())
                putExtra(Intent.EXTRA_SUBJECT,getString(R.string.crime_report_subject))
            }.also{
                    intent ->
                val chooserIntent= Intent.createChooser(intent,getString(R.string.send_report))
                startActivity(chooserIntent)
            }

        }
        suspectButton.apply{
            val pickContactIntent=Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            setOnClickListener {
                startActivityForResult(pickContactIntent, REQUEST_CONTACT)
            }
            val packageManager: PackageManager =requireActivity().packageManager
            val resolvedActivity: ResolveInfo?=packageManager.resolveActivity(pickContactIntent, PackageManager.MATCH_DEFAULT_ONLY)
            if (resolvedActivity==null){
                isEnabled=false
            }
        }
        addButoon.setOnClickListener(){
            var existingCrime:Crime?

            if (title.text.isNullOrEmpty()) {
                Toast.makeText(requireActivity(), "Введите преступление", Toast.LENGTH_SHORT).show()
            } else {
                val crimeTitle = title.text.toString()



                GlobalScope.launch(Dispatchers.IO) {
                    existingCrime = crimeDetailViewModel.getCrimeByTitle(title.text.toString())
                    CoroutineScope(Dispatchers.Main).launch {

                        if (title.text.isNotEmpty()) {

                            if (existingCrime == null) {

                                val crime = Crime()
                                crime.title = crimeTitle
                                crime.date = Date()
                                crime.isSolved = solvedCheckBox.isChecked
                                crimeDetailViewModel.add(crime)

                                title.text = null
                                Toast.makeText(
                                    requireActivity(),
                                    "Вы добавили новое преступление",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    requireActivity(),
                                    "В списке уже есть такое название",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(
                                requireActivity(),
                                "Нельзя пустое сообщение",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                }
            }
        }
    }
    private fun updateUI(){
        title.setText(crime.title)
        solvedCheckBox.apply {
            isChecked=crime.isSolved!!
            jumpDrawablesToCurrentState()
        }
        if (crime.suspect.isNotEmpty()){
            suspectButton.text=crime.suspect
        }
    }
    private fun getCrimeReport(): String{
        val solvedString = if (crime.isSolved == true){
            getString(R.string.crime_report_solved)
        }
        else{
            getString(R.string.crime_report_unsolved)
        }
        val dateString= DateFormat.format(DATE_FORMAT,crime.date).toString()
        var suspect=if (crime.suspect.isBlank()){
            getString(R.string.crime_report_no_suspect)
        }
        else{
            getString(R.string.crime_report_suspect, crime.suspect)
        }
        return getString(R.string.crime_report,crime.title,dateString,solvedString,suspect)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when{
            resultCode!= Activity.RESULT_OK -> return
            requestCode== REQUEST_CONTACT && data !=null ->{
                val contactUri: Uri? =data.data
                val queryFields=arrayOf(ContactsContract.Contacts.DISPLAY_NAME)
                val cursor =
                    contactUri?.let {
                        requireActivity().contentResolver.query(it,queryFields,null,
                            null)
                    }
                cursor?.use {
                    if (it.count==0){
                        return
                    }
                    it.moveToFirst()
                    val suspect=it.getString(0)
                    crime.suspect=suspect
                    crimeDetailViewModel.save(crime)
                    suspectButton.text=suspect
                }

            }
        }
    }
    companion object{
        fun newInstance() : CrimeListFragment{
            return CrimeListFragment()
        }
    }
}