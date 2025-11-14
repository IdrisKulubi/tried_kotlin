package com.example.bakerysystem.ui

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.R
import com.example.bakerysystem.data.MenuItemEntity
import com.example.bakerysystem.databinding.ActivityAddItemBinding

class AddItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddItemBinding
    private var itemId: Int = 0 // 0 = new item, >0 = edit existing item
    private var selectedImageUri: Uri? = null
    private val defaultImageUri = "android.resource://com.example.bakerysystem/drawable/ic_placeholder"

    private val menuViewModel: MenuViewModel by viewModels {
        val app = application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1️⃣ Image picker launcher
        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                binding.ivItemImage.setImageURI(it)
            }
        }

        binding.btnSelectImage.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        // 2️⃣ Load existing item if editing
        loadExistingItem()

        // 3️⃣ Observe status messages from ViewModel
        menuViewModel.statusMessage.observe(this) { message ->
            if (message.isNotBlank()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                menuViewModel.clearStatus()
            }
        }

        // 4️⃣ Save button click
        binding.btnSaveItem.setOnClickListener {
            saveMenuItem()
        }
    }

    private fun loadExistingItem() {
        itemId = intent.getIntExtra("ITEM_ID", 0)

        if (itemId != 0) {
            // Editing existing item
            binding.tvTitle.text = "Edit Menu Item"
            binding.btnSaveItem.text = "Update Item"

            binding.etItemName.setText(intent.getStringExtra("ITEM_NAME"))
            binding.etItemDescription.setText(intent.getStringExtra("ITEM_DESC"))
            binding.etItemPrice.setText(intent.getStringExtra("ITEM_PRICE"))
            binding.cbIsAvailable.isChecked = intent.getBooleanExtra("ITEM_AVAIL", true)

            // Load existing image URI if passed
            val imageUriStr = intent.getStringExtra("ITEM_IMAGE_URI")
            if (!imageUriStr.isNullOrEmpty()) {
                selectedImageUri = Uri.parse(imageUriStr)
                binding.ivItemImage.setImageURI(selectedImageUri)
            } else {
                binding.ivItemImage.setImageResource(R.drawable.ic_placeholder)
            }
        } else {
            // New item
            binding.ivItemImage.setImageResource(R.drawable.ic_placeholder)
        }
    }

    private fun saveMenuItem() {
        val name = binding.etItemName.text.toString()
        val description = binding.etItemDescription.text.toString()
        val priceText = binding.etItemPrice.text.toString()
        val isAvailable = binding.cbIsAvailable.isChecked

        if (name.isBlank() || description.isBlank() || priceText.isBlank()) {
            menuViewModel.setStatus("Error: All fields must be filled.")
            return
        }

        val price = priceText.toDoubleOrNull() ?: run {
            menuViewModel.setStatus("Error: Invalid price format.")
            return
        }

        // Ensure image URI is never null
        val imageUriStr = selectedImageUri?.toString() ?: defaultImageUri

        val itemToSave = MenuItemEntity(
            id = itemId,
            name = name,
            description = description,
            price = price,
            isAvailable = isAvailable,
            imageUri = imageUriStr
        )

        menuViewModel.saveMenuItem(itemToSave)
        finish()
    }
}
