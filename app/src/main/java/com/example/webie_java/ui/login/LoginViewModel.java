package com.example.webie_java.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.example.webie_java.data.LoginRepository;
import com.example.webie_java.data.Result;
import com.example.webie_java.data.model.LoggedInUser;
import com.example.webie_java.R;

public class LoginViewModel extends ViewModel
    {
        //Instancing
        private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
        private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
        //Declaration
        private LoginRepository loginRepository;

        LoginViewModel(LoginRepository loginRepository)
            {
                this.loginRepository = loginRepository;
            }

        LiveData<LoginFormState> getLoginFormState()
            {
                return loginFormState;
            }

        LiveData<LoginResult> getLoginResult() {
            return loginResult;
        }

        //Function to get login data which you insert on login screen
        public void login(String username, String password)
            {
                // can be launched in a separate asynchronous job
                Result<LoggedInUser> result = loginRepository.login(username, password);

                if (result instanceof Result.Success)
                    {
                        LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
                    }
                else
                    {
                        loginResult.setValue(new LoginResult(R.string.login_failed));
                    }
            }

        public void loginDataChanged(String username, String password) {
            if (!isUserNameValid(username)) {
                loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
            } else if (!isPasswordValid(password)) {
                loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
            } else {
                loginFormState.setValue(new LoginFormState(true));
            }
        }

        // A placeholder username validation check
        private boolean isUserNameValid(String username) {
            if (username == null) {
                return false;
            }
            if (username.contains("@")) {
                return Patterns.EMAIL_ADDRESS.matcher(username).matches();
            } else {
                return !username.trim().isEmpty();
            }
        }

        // A placeholder password validation check
        private boolean isPasswordValid(String password)
            {
                // return password, if its not empty and the length is bigger than 5
                return password != null && password.trim().length() > 5;
            }
    }
