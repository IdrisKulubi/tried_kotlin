package com.example.bakerysystem.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.bakerysystem.AppViewModelFactory
import com.example.bakerysystem.BakeryApplication
import com.example.bakerysystem.data.UserEntity // Import UserEntity
import com.example.bakerysystem.databinding.FragmentAccountBinding

class AccountFragment : Fragment() {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    private val authViewModel: AuthViewModel by viewModels {
        val app = requireActivity().application as BakeryApplication
        AppViewModelFactory(app.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authViewModel.loggedInUser.observe(viewLifecycleOwner) { user: UserEntity? ->
            if (user != null) {
                binding.tvUsername.text = user.username
                binding.tvEmail.text = user.email
                binding.btnLogout.visibility = View.VISIBLE
            } else {
                binding.tvUsername.text = "N/A"
                binding.tvEmail.text = "N/A"
                binding.btnLogout.visibility = View.GONE
                // Optionally, navigate to login if no user is logged in
                // startActivity(Intent(requireContext(), LoginActivity::class.java))
                // requireActivity().finish()
            }
        }

        binding.btnLogout.setOnClickListener {
            authViewModel.logout()
        }

        authViewModel.authStatus.observe(viewLifecycleOwner) { status ->
            if (status == "Logout Success") {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            } else if (status.isNotBlank()) {
                Toast.makeText(requireContext(), status, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}