package com.example.sinhvien.diemdanhsinhvien;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private FirebaseAuth auth;

    /*@Bind(R.id.input_email) EditText _emailText;
    @Bind(R.id.input_password) EditText _passwordText;
    @Bind(R.id.btn_login) Button _loginButton;
    @Bind(R.id.link_signup) TextView _signupLink;
*/
    EditText emailText, passwordText;
    Button btnLogin;
    Button btnTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acyivity_login);
        auth = FirebaseAuth.getInstance();
        getView();
        btnLogin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });



    }

    private void getView() {
        emailText = (EditText) findViewById(R.id.input_email);
        passwordText = (EditText) findViewById(R.id.input_password);
        btnLogin = (Button)findViewById(R.id.btn_login);
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btnLogin.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this, R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Đang đăng nhập...");
        progressDialog.show();

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        // TODO: Implement your own authentication logic here.
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            //tạo một cái bunde
                            Bundle bundle = new Bundle();
                            //tạo biến người dùng
                            FirebaseUser user = task.getResult().getUser();
                            //lấy uid người dùng
                            //ma hoa Uid
                            String uID = user.getUid()+"";
                            bundle.putString("UID",uID);
                            intent.putExtra("uIDNguoiDung",bundle);
                            startActivity(intent);
                        }else {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Đăng nhập thất bại kiểm tra lại thông tin tài khoảng",Toast.LENGTH_LONG).show();
                            btnLogin.setEnabled(true);

                        }
                    }
                });



    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*/

   /* @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }*/

    /*public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        finish();
    }
*/
   public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Email hoặc password không đúng", Toast.LENGTH_LONG).show();

        btnLogin.setEnabled(true);
   }

    public boolean validate() {
        boolean valid = true;

        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("Email không đúng định dạng");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 12) {
            passwordText.setError("Tối thiểu 4 ký tự và nhỏ hơn 12 kí tự");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        return valid;
    }


}
