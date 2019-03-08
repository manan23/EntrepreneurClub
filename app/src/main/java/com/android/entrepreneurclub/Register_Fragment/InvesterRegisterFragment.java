package com.android.entrepreneurclub.Register_Fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.entrepreneurclub.Activity.LoginActivity;
import com.android.entrepreneurclub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InvesterRegisterFragment extends Fragment {
    @Nullable
    TextInputEditText Investor_name, Investor_phone, Investor_email_register, Investor_age, Investor_Company, Investor_password, Investor_confirm_password, Investor_locality;

    Button Investor_Register;
    RadioGroup Investor_gender_buttons;
    RadioButton Investor_gender_male, Investor_gender_female;
    private String email, name, password, confirmPassword, phone, gender, age, company_name, locality;

    private FirebaseUser user;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private DatabaseReference mUserDatabase;

    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.invester_fragment, null, false);

        Investor_name = v.findViewById(R.id.Investor_name);
        Investor_phone = v.findViewById(R.id.Investor_phone);
        Investor_email_register = v.findViewById(R.id.Investor_email_register);
        Investor_age = v.findViewById(R.id.Investor_age);
        Investor_Company = v.findViewById(R.id.Investor_Company);
        Investor_password = v.findViewById(R.id.Investor_password);
        Investor_confirm_password = v.findViewById(R.id.Investor_confirm_password);
        Investor_locality = v.findViewById(R.id.Investor_Locality);

        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        Investor_Register = v.findViewById(R.id.Investor_Register);

        Investor_gender_buttons = v.findViewById(R.id.Investor_gender_buttons);
        Investor_gender_male = v.findViewById(R.id.Investor_gender_male);
        Investor_gender_female = v.findViewById(R.id.Investor_gender_female);

        Investor_gender_buttons.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Investor_gender_male:
                        gender = "Male";
                        Log.d("Gender", "" + gender);

                        break;
                    case R.id.Investor_gender_female:

                        gender = "Female";
                        Log.d("Gender", "" + gender);

                        break;
                    default:
                        break;
                }
            }
        });
        Investor_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = Investor_email_register.getText().toString().trim();
                name = Investor_name.getText().toString().trim();
                password = Investor_password.getText().toString().trim();
                phone = Investor_phone.getText().toString().trim();
                confirmPassword = Investor_confirm_password.getText().toString().trim();
                age = Investor_age.getText().toString().trim();
                company_name = Investor_Company.getText().toString().trim();
                locality = Investor_locality.getText().toString().trim();


                if (validateDetails(name, email, password, confirmPassword, phone, age, company_name, locality)) {
                    if (Investor_gender_buttons.getCheckedRadioButtonId() < 0) {
                        Toast.makeText(getActivity(), "Please Select A Gender", Toast.LENGTH_SHORT).show();
                    } else {
                        createAccount(name, email, password, phone, locality, age, gender, company_name);
                    }
                }
            }
        });
        return v;
    }

    private boolean emailIsValid(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private boolean passIsValid(String pass) {
        if (pass.length() > 5) return true;
        else return false;
    }

    @SuppressLint("ResourceType")
    public boolean validateDetails(String name, String email, final String password, String confirmPassword, String phone, String age, String company_name, String locality) {

        boolean check = true;

        if (check) {
            if (!emailIsValid(email)) {
                Investor_email_register.setError("Invalid e-Mail");
                check = false;
            }
            if (name.isEmpty()) {
                Investor_name.setError("Name Cannot Be Empty");
                check = false;
            }
            if (age.isEmpty()) {
                Investor_age.setError("age Cannot Be Empty");
                check = false;
            }
            if (company_name.isEmpty()) {
                Investor_Company.setError("Company Name Cannot Be Empty");
                check = false;
            }
            if (locality.isEmpty()) {
                Investor_locality.setError("Locality Cannot Be Empty");
                check = false;
            }
            if (phone.isEmpty() || !(phone.length() == 10)) {
                Investor_phone.setError("Invalid Contact No.(length should be 10)");
                check = false;
            }
            if (!passIsValid(password)) {
                Investor_password.setError("Password should have minimum 5 characters");
                check = false;
            }
            if (!(confirmPassword.equals(password))) {
                Investor_confirm_password.setError("Passwords Do Not Match");
                Investor_confirm_password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String confirmpass = Investor_confirm_password.getText().toString().trim();
                        if (!(confirmpass.equals(password))) {
                            Investor_confirm_password.setError("Passwords Do Not Match");
                        }
                    }
                });
                check = false;
            }
            if (Investor_gender_buttons.getCheckedRadioButtonId() < 0) {
                Toast.makeText(getActivity(), "Please Select A Gender", Toast.LENGTH_LONG).show();
                check = false;
            }
        } else {
            check = true;
        }
        return check;
    }

    public void createAccount(final String name, String email, String password,
                              final String phone, final String locality, final String age, final String gender,
                              final String company_name) {

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Creating Account", "Please Wait...", false, true);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = auth.getCurrentUser();
                    String uid = currentUser.getUid();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    String device_token = FirebaseInstanceId.getInstance().getToken();

                    Log.d("abc", "onComplete: " + uid);
                    String prefix;
                    if (gender == "Male") {
                        prefix = "Mr. ";
                    } else {
                        prefix = "Mrs. ";
                    }
                    HashMap<String, String> userDetails = new HashMap<String, String>();

                    userDetails.put("name", prefix + name);
                    userDetails.put("phone", phone);
                    userDetails.put("locality", locality);
                    userDetails.put("age", age);
                    userDetails.put("gender", gender);
                    userDetails.put("company_name", company_name);
                    userDetails.put("type", "Investor");
                    userDetails.put("status", "Hi there I'm using Lapit Chat App.");
                    userDetails.put("image", "default");
                    userDetails.put("thumb_image", "default");
                    userDetails.put("device_token", device_token);
                    Log.d("test", "onComplete: ");
                    /*db.collection("Users").document(uid).set(userDetails).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("xyz", "testing");

                                loading.dismiss();
                                Toast.makeText(getActivity(), "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                            } else {
                                loading.dismiss();
                                Toast.makeText(getActivity(), "Account not created", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });*/

                    databaseReference.setValue(userDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                Log.d("xyz", "testing");

                                loading.dismiss();
                                Toast.makeText(getActivity(), "Account Successfully Created", Toast.LENGTH_SHORT).show();
                                //startActivity(new Intent(getActivity(), LoginActivity.class));
                                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                                loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(loginIntent);
                                getActivity().finish();
                            } else {
                                loading.dismiss();
                                Toast.makeText(getActivity(), "Account not created", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }


}

