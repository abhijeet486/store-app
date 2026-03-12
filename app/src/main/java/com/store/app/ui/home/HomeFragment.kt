package com.store.app.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.store.app.R
import com.store.app.data.local.entity.DealEntity
import com.store.app.data.local.entity.ProductEntity
import com.store.app.databinding.FragmentHomeBinding
import com.store.app.databinding.DialogProductDetailBinding
import com.store.app.databinding.DialogDealDetailBinding
import com.store.app.ui.adapter.DealAdapter
import com.store.app.ui.adapter.ProductAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter
    private lateinit var dealAdapter: DealAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupSwipeRefresh()
        observeViewModel()
    }

    private fun setupRecyclerViews() {
        productAdapter = ProductAdapter { product ->
            showProductDetailDialog(product)
        }
        
        dealAdapter = DealAdapter { deal ->
            showDealDetailDialog(deal)
        }

        // Products - Grid Layout (2 columns)
        binding.recyclerViewProducts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
            isNestedScrollingEnabled = false
        }

        // Deals - Horizontal Layout
        binding.recyclerViewDeals.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = dealAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.primary,
            R.color.accent,
            R.color.error
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshData()
        }
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
        }

        viewModel.deals.observe(viewLifecycleOwner) { deals ->
            dealAdapter.submitList(deals)
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

    private fun showProductDetailDialog(product: ProductEntity) {
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        val dialogBinding = DialogProductDetailBinding.inflate(layoutInflater)
        
        dialogBinding.productName.text = product.name
        dialogBinding.productDescription.text = product.description
        dialogBinding.productPrice.text = String.format("$%.2f", product.price)
        dialogBinding.productCategory.text = product.category
        dialogBinding.productRating.text = String.format("%.1f ⭐", product.rating)
        
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

    private fun showDealDetailDialog(deal: DealEntity) {
        val dialog = BottomSheetDialog(requireContext(), R.style.DialogStyle)
        val dialogBinding = DialogDealDetailBinding.inflate(layoutInflater)
        
        dialogBinding.dealTitle.text = deal.title
        dialogBinding.dealDescription.text = deal.description
        dialogBinding.originalPrice.text = String.format("$%.2f", deal.originalPrice)
        dialogBinding.discountedPrice.text = String.format("$%.2f", deal.discountedPrice)
        dialogBinding.discountBadge.text = String.format("%d%% OFF", deal.discountPercentage)
        
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
