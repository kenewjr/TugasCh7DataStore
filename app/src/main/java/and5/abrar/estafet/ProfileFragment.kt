package and5.abrar.estafet

import and5.abrar.estafet.datastore.UserManager
import and5.abrar.estafet.model.GetUserItem
import and5.abrar.estafet.network.ApiClient
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {
    lateinit var usermanager: UserManager
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        usermanager = UserManager(requireContext())
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataProfile()
        btnLogout.setOnClickListener {
           GlobalScope.launch {
               usermanager.hapusData()
               it.findNavController().navigate(R.id.action_profileFragment_to_loginFragment)
           }
        }
    }

    fun getDataProfile() {
        usermanager = UserManager(requireContext())
        usermanager.userName.asLiveData().observe(viewLifecycleOwner) {
            ApiClient.instance.allUser(it)
                .enqueue(object : Callback<List<GetUserItem>>{
                    override fun onResponse(
                        call: Call<List<GetUserItem>>,
                        response: Response<List<GetUserItem>>
                    ) {
                        if (response.isSuccessful){
                            val data = response.body()!![0]
                            tvUsername.text = data.username
                            tvAdress.text = data.address
                            tvUmur.text = data.umur.toString()
                            tvNama.text = data.name
                            Glide.with(requireView()).load(data.image).into(imgProfile)
                        }
                    }
                    override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}