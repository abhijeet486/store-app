package com.store.app.ui.deals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.store.app.R
import com.store.app.data.local.entity.DealEntity
import com.store.app.databinding.FragmentDealsBinding
import com.store.app.databinding.DialogDealDetailBinding
import com.store.app.ui.adapter.DealAdapter

class DealsFragment : Fragment() {

    private var _binding: FragmentDealsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DealsViewModel by viewModels()
    private lateinit var dealAdapter: DealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDealsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        dealAdapter = DealAdapter { deal ->
            showDealDetailDialog(deal)
        }

        binding.recyclerViewDeals.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = dealAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshDeals()
        }
    }

    private fun observeViewModel() {
        viewModel.deals.observe(viewLifecycleOwner) { deals ->
            dealAdapter.submitList(deals)
            if (deals.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerViewDeals.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerViewDeals.visibility = View.VISIBLE
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                viewModel.clearError()
            }
        }
    }

    private fun showDealDetailDialog(deal: DealEntity) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogDealDetailBinding.inflate(layoutInflater)
        
        dialogBinding.dealTitle.text = deal.title
        dialogBinding.dealDescription.text = deal.description
        dialogBinding.originalPrice.text = String.format("$%.2f", deal.originalPrice)
        dialogBinding.discountedPrice.text = String.format("$%.2f", deal.discountedPrice)
        dialogBinding.discountPercentage.text = String.format("%d%% OFF", deal.discountPercentage)
        
        dialogBinding.btnAddToCart.setOnClickListener {
            Toast.makeText(context, R.string.item_added, Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        
        dialogBinding.btnClose.setOnClickListener {
            dialog.dismiss()
        }
        
        dialog.setContentView(dialogBinding.root)
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
