package ar.com.myanotator.myanotator.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ar.com.myanotator.myanotator.R
import ar.com.myanotator.myanotator.data.models.ToDoData
import ar.com.myanotator.myanotator.databinding.FragmentListBinding
import ar.com.myanotator.myanotator.fragments.list.adapter.ListAdapter
import ar.com.myanotator.myanotator.presentation.SharedViewModel
import ar.com.myanotator.myanotator.presentation.ToDoViewModel
import ar.com.myanotator.myanotator.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

class ListFragment : Fragment(R.layout.fragment_list), SearchView.OnQueryTextListener {

    private val mToDoViewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private lateinit var binding: FragmentListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Set Menu
        setHasOptionsMenu(true)

        //Hide soft keyboard
        hideKeyboard(requireActivity())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentListBinding.bind(view)
        floatingActionButton()
        navigateToAddFragment()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        binding.rvList.adapter = adapter
        //binding.rvList.layoutManager = LinearLayoutManager(requireContext())
        //binding.rvList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL )
        swipeToDelete(binding.rvList)
        binding.rvList.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        setAllData()
        sharedViewModel.emptyDatabase.observe(viewLifecycleOwner, Observer { showError ->
            showEmptyDatabaseViews(showError)
        })
    }

    private fun setAllData() {
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer {
            sharedViewModel.checkIfDatabaseEmpty(it)
            adapter.setData(it)
        })
    }

    //Swipe to delete, remember to do the class SwipeToDelete before
    private fun swipeToDelete(recyclerView: RecyclerView) {
        val swipeToDeleteCallBack = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //Delete Item
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)
                Toast.makeText(
                    requireContext(), "Successfully Removed ${deletedItem.title}",
                    Toast.LENGTH_SHORT
                ).show()
                //Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallBack)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun showEmptyDatabaseViews(showError: Boolean) {
        if (showError) {
            binding.imgEmpty.isVisible = true
            binding.tvEmpty.isVisible = true
        } else {
            binding.imgEmpty.isVisible = false
            binding.tvEmpty.isVisible = false
        }
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
        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete_all -> {confirmRemoval()}
            R.id.menu_priority_high -> {mToDoViewModel.sortByHighPriority.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })}
            R.id.menu_priority_low -> {mToDoViewModel.sortByLowPriority.observe(viewLifecycleOwner, Observer {
                adapter.setData(it)
            })}
        }
        return super.onOptionsItemSelected(item)
    }

    //Show AlertDialog to Confirm delete All elements
    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(), "All was successfully removed",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete all?")
        builder.setMessage("Are you sure you want to remove all the notes?")
        builder.create().show()
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackBar = Snackbar.make(view, "Deleted '${deletedItem.title}'",
            Snackbar.LENGTH_LONG)
        snackBar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
            /*This line its only for the recyclerView animation in linearLayoutManager
            *
            *adapter.notifyItemChanged(position)
            *
            * */
        }
        snackBar.show()
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchThroughDatabase(query)
        }else{
            setAllData()
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (!query.isNullOrEmpty()){
            searchThroughDatabase(query)
        }else{
            setAllData()
        }
        return true
    }

    private fun searchThroughDatabase(query: String?) {
        val searchQuery = "%$query%"

        //Doing query and setting the information on adapter
        mToDoViewModel.searchDataBase(searchQuery).observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.setData(it)
            }
        })
    }
}
