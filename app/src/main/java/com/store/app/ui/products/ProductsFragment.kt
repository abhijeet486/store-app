package com.store.app.ui.products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.store.app.R
import com.store.app.data.local.entity.ProductEntity
import com.store.app.databinding.FragmentProductsBinding
import com.store.app.databinding.DialogProductDetailBinding
import com.store.app.ui.adapter.ProductAdapter

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var productAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        setupCategoryChips()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            showProductDetailDialog(product)
        }
        
        binding.recyclerViewProducts.apply {
            // Grid Layout (2 columns)
            layoutManager = GridLayoutManager(context, 2)
            adapter = productAdapter
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setColorSchemeResources(
            R.color.primary,
            R.color.accent,
            R.color.error
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshProducts()
        }
    }

    private fun setupCategoryChips() {
        // Add category chips dynamically based on available categories
        val categories = listOf("All", "Electronics", "Clothing", "Home", "Sports", "Books")
        
        binding.chipAll.setOnClickListener {
            viewModel.filterByCategory("All")
        }
    }

    private fun observeViewModel() {
        viewModel.products.observe(viewLifecycleOwner) { products ->
            productAdapter.submitList(products)
            binding.emptyView.visibility = if (products.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerViewProducts.visibility = if (products.isEmpty()) View.GONE else View.VISIBLE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefreshLayout.isRefreshing = isLoading
            binding.progressBar.visibility = if (isLoading && productAdapter.itemCount == 0) View.VISIBLE else View.GONE
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
