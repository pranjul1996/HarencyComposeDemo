package com.macamps.harencycomposedemo.ui.login

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.macamps.harencycomposedemo.DividerView
import com.macamps.harencycomposedemo.R
import com.macamps.harencycomposedemo.SocialLoginButtons
import com.macamps.harencycomposedemo.data.UserRegisterModel
import com.macamps.harencycomposedemo.navigation.Screen
import com.macamps.harencycomposedemo.ui.theme.Purple500
import com.macamps.harencycomposedemo.utils.DrawableWrapper
import com.macamps.harencycomposedemo.utils.State
import com.macamps.harencycomposedemo.viewModel.LoginSharedViewModel
import retrofit2.Response

@Composable
fun HarencyLoginScreen(
    navController: NavController,
    sharedViewModel: LoginSharedViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .verticalScroll(state = scrollState)
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .padding(top = 15.dp)
        ) {
            Text(
                stringResource(R.string.sign_in),
                style = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Text(stringResource(R.string.sign_in_to_continue))

        }

        Box(modifier = Modifier.padding(vertical = 10.dp)) {
            LoginCardView(navController, sharedViewModel)
        }

        Box(modifier = Modifier.padding(top = 15.dp)) {
            Image(
                painter = painterResource(id = R.drawable.ic_wave),
                contentDescription = null, modifier =
                Modifier.fillMaxSize(),
                contentScale = ContentScale.FillWidth
            )
            Image(
                painter = painterResource(id = R.drawable.ic_wave2),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp),
                contentScale = ContentScale.FillWidth,
                contentDescription = null
            )
        }
    }
}

@Composable
fun LoginCardView(navController: NavController, sharedViewModel: LoginSharedViewModel) {
//    val context = LocalContext.current

    val country = sharedViewModel.countryLiveData.observeAsState()
    val context = (LocalContext.current as? Activity)

//    val country = remember { navController.previousBackStackEntry?.savedStateHandle?.get<CountriesItem>("data") }
//    Log.e("TAG", "LoginCardView: ${getData?.value?.icCode}", )


    var phoneNumber by remember {
        mutableStateOf(TextFieldValue(""))
    }
    val maxLength = 10
/*    val loginResponse = produceState<State<Response<UserRegisterModel>>?>(initialValue = State.Loading){
        value= sharedViewModel.loginLiveData
    }.value*/
    val loginResponse = sharedViewModel.loginLiveData.observeAsState()
    val value = if (country.value != null) "+${country.value?.codeTelePhone}" else "+91"

//    var countryCode by remember {
//        mutableStateOf(value)
//    }
    var password by remember {
        mutableStateOf(TextFieldValue(""))
    }



    when (loginResponse.value) {
        is State.Success -> {
            Toast.makeText(
                context,
                (loginResponse.value as State.Success<Response<UserRegisterModel>>).data.body()?.message,
                Toast.LENGTH_SHORT
            ).show()

        }
        is State.Loading -> {
            Toast.makeText(
                context,
                "Loading",
                Toast.LENGTH_SHORT
            ).show()

        }
        is State.Error -> {

        }
        else -> Unit
    }
    Card(
        elevation = 5.dp,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .padding(0.dp)
            .padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(vertical = 26.dp)) {


            Box {
                TextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.text.length <= maxLength) phoneNumber = it

                    },
                    leadingIcon = { Purple500 },
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .padding(horizontal = 20.dp)
                        .fillMaxWidth(),
                    placeholder = { Text("Phone Number") },

                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    keyboardActions = KeyboardActions(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.White,
                    ),
                )
                DrawableWrapper(drawableEnd = R.drawable.ic_arrow_down) {
                    Text(
                        text = value,
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black
                        ),
                        modifier = Modifier
                            .padding(
                                top = 14.dp,
                                bottom = 14.dp,
                                start = 20.dp,
                            )
                            .clickable {
                                navController.navigate(Screen.CountryCodeScreen.route)
                            },
                    )
                }


            }
            TextField(
                value = password,
                onValueChange = {
                    password = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(
                        bottom = 10.dp,
                        start = 20.dp,
                        end = 20.dp
                    ),
                placeholder = { Text("Password") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.White
                )
            )
            Text(
                "Forgot password?",
                style = TextStyle(
                    color = Purple500,
                    textAlign = TextAlign.End
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        end = 15.dp,
                        bottom = 10.dp
                    )
            )

            Button(
                onClick = {
                    val hashMap = HashMap<String, String?>()
                    hashMap["phone_number"] = phoneNumber.text
                    hashMap["country_code"] = value
                    hashMap["password"] = password.text
                    hashMap["device_type"] = "1"
                    hashMap["device_token"] = "sss"
                    sharedViewModel.login(hashMap)

                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 15.dp)
                    .height(42.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = colorResource(R.color.mainBgColor)
                ),
                shape = RoundedCornerShape(70)
            ) {
                Text(
                    stringResource(R.string.sign_in),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

//            Divider
            DividerView()
            SocialLoginButtons()
        }
    }

}

