package com.example.bakerysystem.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.R
import com.example.bakerysystem.data.MenuItemEntity

class ProductsFragment : Fragment() {

    private val menuViewModel: MenuViewModel by viewModels {
        val app = requireActivity().application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_products, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.products_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(context)

        menuViewModel.menuItems.observe(viewLifecycleOwner, Observer { items: List<MenuItemEntity> ->
            items.let {
                recyclerView.adapter = ProductsAdapter(it)
            }
        })

        return view
    }
}