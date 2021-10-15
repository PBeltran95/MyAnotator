package ar.com.myanotator.myanotator.fragments.list

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import ar.com.myanotator.myanotator.R
import ar.com.myanotator.myanotator.databinding.FragmentListBinding
import ar.com.myanotator.myanotator.presentation.ToDoViewModel

class ListFragment : Fragment(R.layout.fragment_list) {

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set Menu
        setHasOptionsMenu(true)
    }

    private lateinit var binding:FragmentListBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        floatingActionButton()
        navigateToAddFragment()
        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvList.adapter = adapter
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
    }

    private fun navigateToAddFragment() {
        binding.listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }
    }

    private fun floatingActionButton() {
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu, menu)
    }



}