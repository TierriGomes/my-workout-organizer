package com.tierriapps.myworkoutorganizer.feature_main.presenter.fragments

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tierriapps.myworkoutorganizer.R
import com.tierriapps.myworkoutorganizer.databinding.ActivityMainBinding
import com.tierriapps.myworkoutorganizer.databinding.FragmentDoTrainingSessionBinding
import com.tierriapps.myworkoutorganizer.feature_main.domain.notification_service.MyBackGroundService
import com.tierriapps.myworkoutorganizer.feature_main.presenter.MainActivity
import com.tierriapps.myworkoutorganizer.feature_main.presenter.adapters.DoTrainingSessionAdapter
import com.tierriapps.myworkoutorganizer.feature_main.presenter.models.DivisionForUi
import com.tierriapps.myworkoutorganizer.feature_main.presenter.viewmodels.DoTrainingSessionViewModel
import com.tierriapps.myworkoutorganizer.feature_main.utils.adaptersutil.CustomizedLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import java.security.Provider.Service

@AndroidEntryPoint
class DoTrainingSessionFragment : Fragment() {
    private lateinit var binding: FragmentDoTrainingSessionBinding
    private val viewModel: DoTrainingSessionViewModel by viewModels()

    private val myBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (p1?.action == MyBackGroundService.Actions.GIVE_DATA_TO_FRAGMENT.toString()){
                val divisionString = p1.extras?.getString("divisionForUi", "")
                if (divisionString != null && divisionString != ""){
                    val type = object : TypeToken<DivisionForUi>(){}.type
                    val division = Gson().fromJson(divisionString, type) as DivisionForUi
                    viewModel.setDivisionForUi(division)
                }
            }
        }

    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDoTrainingSessionBinding.inflate(inflater, container, false)
        val name = arguments?.getString("divisionName", "null")
        if (name != null && name != "null" && viewModel.divisionStatus.value == null){
            viewModel.getActualDivisionToDo(name)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSaveTrainingDone.setOnClickListener {
            val snackbar = Snackbar.make(
                requireContext(), binding.root,
                "Save training?", Snackbar.LENGTH_SHORT)
            snackbar.setAction("Yes"){
                viewModel.createTraining()
                Intent(requireContext(), MyBackGroundService::class.java).also {
                    it.action = MyBackGroundService.Actions.STOP.toString()
                    requireActivity().startService(it)
                }
            }
            snackbar.show()
        }

        binding.buttonOpenInNotificationBar.setOnClickListener {
            val division = viewModel.divisionStatus.value?:return@setOnClickListener
            val gson = Gson().toJson(division)
            (requireActivity() as MainActivity).startDoTrainingService(gson)
        }
    }

    override fun onResume() {
        super.onResume()
        Intent(requireContext(), MyBackGroundService::class.java).also {
            it.action = MyBackGroundService.Actions.FRAGMENT_ASKS_FOR_DATA.toString()
            requireActivity().startService(it)
        }
        val toolBar = (requireActivity() as MainActivity).binding.toolbar
        binding.recyclerViewDoTrainingSession.layoutManager = CustomizedLayoutManager(requireContext())
        viewModel.divisionStatus.observe(viewLifecycleOwner){ divisionForUi ->
            if (divisionForUi == null){
                val snackbar = Snackbar.make(
                    requireContext(),
                    binding.root,
                    "Cannot Load Data",
                    Snackbar.LENGTH_LONG).show()
                return@observe
            }
            val textColor = ContextCompat.getColor(requireContext(), divisionForUi.colorForTexts())
            binding.buttonSaveTrainingDone.setTextColor(textColor)
            binding.buttonOpenInNotificationBar.setTextColor(textColor)
            toolBar.setBackgroundResource(divisionForUi.colorForButtonAndHints())
            binding.constraintLayotDoTrainingSession.setBackgroundResource(divisionForUi.colorForBackGround())
            val adapter = DoTrainingSessionAdapter(divisionForUi.exercises, divisionForUi.colorForTexts())
            binding.recyclerViewDoTrainingSession.adapter = adapter
        }
        viewModel.jobStatus.observe(viewLifecycleOwner){
            if (it == null){
                binding.progressBar2.visibility = View.VISIBLE
            }else {
                binding.progressBar2.visibility = View.GONE
                Toast.makeText(requireContext(), it.asString(requireContext()), Toast.LENGTH_LONG).show()
                if (it.asString(requireContext()) == "Training Done And Saved!"){
                    findNavController().navigateUp()
                    onDestroy()
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val intentFilter = IntentFilter(MyBackGroundService.Actions.GIVE_DATA_TO_FRAGMENT.toString())
        context.registerReceiver(myBroadcastReceiver, intentFilter)
    }

    override fun onDetach() {
        super.onDetach()
        context?.unregisterReceiver(myBroadcastReceiver)
    }
}