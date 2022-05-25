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
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response


class LoginFragment : Fragment() {

    lateinit var email : String
    lateinit var password : String
    private var userManager : UserManager? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userManager = UserManager(requireContext())
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userManager?.userName?.asLiveData()?.observe(viewLifecycleOwner){
            if (it.isNotEmpty()){
                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        btnLogin.setOnClickListener {
            if (loginEmail.text.isNotEmpty() && loginPassword.text.isNotEmpty()){
                email = loginEmail.text.toString()
                password = loginPassword.text.toString()
                login(email,password)
            }else{
                Toast.makeText(requireContext(), "mohon isi form login", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun login(username : String, password:String){
        ApiClient.instance.allUser(username).enqueue(object  : retrofit2.Callback<List<GetUserItem>>{
            override fun onResponse(
                call: Call<List<GetUserItem>>,
                response: Response<List<GetUserItem>>
            ) {
                if(response.isSuccessful){
                    if (response.body()?.isEmpty()==true){
                        Toast.makeText(requireContext(), "Unknows User", Toast.LENGTH_SHORT).show()
                    }else{
                        when{
                            response.body()?.size!!>1 -> {
                                Toast.makeText(requireContext(), "mohon masukan data yang benar", Toast.LENGTH_SHORT).show()
                            }
                            username !=response.body()!![0].username -> {
                                Toast.makeText(requireContext(), "Username tidak terdaftar", Toast.LENGTH_SHORT).show()
                            }
                            password != response.body()!![0].password -> {
                                Toast.makeText(requireContext(), "Password yang anda masukkan salah", Toast.LENGTH_SHORT).show()
                            }
                            else ->{
                                GlobalScope.launch {
                                    userManager!!.savedata(username)
                                }
                                Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                        }
                    }
                }
            }
            override fun onFailure(call: Call<List<GetUserItem>>, t: Throwable) {
                Toast.makeText(requireContext(), "Password yang anda masukkan salah", Toast.LENGTH_SHORT).show()
            }
        })
    }
}