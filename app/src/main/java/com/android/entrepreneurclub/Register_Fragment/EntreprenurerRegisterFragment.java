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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

public class EntreprenurerRegisterFragment extends Fragment {

    private TextInputEditText Entreprenuer_name, Entreprenuer_phone, Entreprenuer_Email_register, Entreprenuer_age, Entreprenuer_Company, Entreprenuer_password, Entreprenuer_confirm_password, Entreprenuer_locality;
    private Button Entreprenuer_register;
    private RadioGroup Entreprenuer_gender_button;
    private RadioButton Entreprenuer_gender_male, Entreprenuer_gender_female;
    private String email, name, password, confirmPassword, phone, gender, age, company_name, locality, content;

    private FirebaseUser user;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private Spinner mContentSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.entreprenuer_fragment, null, false);

        Entreprenuer_name = v.findViewById(R.id.Entreprenuer_name);
        Entreprenuer_phone = v.findViewById(R.id.Entreprenuer_phone);
        Entreprenuer_Email_register = v.findViewById(R.id.Entreprenuer_email_register);
        Entreprenuer_age = v.findViewById(R.id.Entreprenuer_age);
        Entreprenuer_Company = v.findViewById(R.id.Entreprenuer_company);
        mContentSpinner =  v.findViewById(R.id.contentSpinner);
        mContentSpinner = initSpinner(mContentSpinner, R.array.spinner_array);
        Entreprenuer_password = v.findViewById(R.id.Entreprenuer_password);
        Entreprenuer_confirm_password = v.findViewById(R.id.Entreprenuer_confirm_password);
        Entreprenuer_locality = v.findViewById(R.id.Entreprenuer_Locality);

        Entreprenuer_register = v.findViewById(R.id.Entreprenuer_register);

        Entreprenuer_gender_male = v.findViewById(R.id.Entreprenuer_gender_male);
        Entreprenuer_gender_female = v.findViewById(R.id.Entreprenuer_gender_female);
        Entreprenuer_gender_button = v.findViewById(R.id.Entreprenuer_gender_buttons);

        mContentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                content = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                content = parent.getSelectedItem().toString();

            }
        });


        Entreprenuer_gender_button.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.Entreprenuer_gender_male:
                        gender = "Male";
                        Log.d("Gender", "" + gender);

                        break;
                    case R.id.Entreprenuer_gender_female:

                        gender = "Female";
                        Log.d("Gender", "" + gender);

                        break;
                    default:
                        break;
                }
            }
        });
        Entreprenuer_register.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                email = Entreprenuer_Email_register.getText().toString().trim();
                name = Entreprenuer_name.getText().toString().trim();
                password = Entreprenuer_password.getText().toString().trim();
                phone = Entreprenuer_phone.getText().toString().trim();
                confirmPassword = Entreprenuer_confirm_password.getText().toString().trim();
                age = Entreprenuer_age.getText().toString().trim();
                company_name = Entreprenuer_Company.getText().toString().trim();
                locality = Entreprenuer_locality.getText().toString().trim();



                if (validateDetails(name, email, password, confirmPassword, phone, age, company_name, locality)) {
                    if (Entreprenuer_gender_button.getCheckedRadioButtonId() < 0) {
                        Toast.makeText(getActivity(), "Please Select A Gender", Toast.LENGTH_SHORT).show();
                    } else {
                        createAccount(name, email, password, phone, locality, age, gender, company_name, content);
                    }
                }
            }
        });
        return v;
    }

  /*  public void isSelected(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()) {
            case R.id.Entreprenuer_gender_male:
                if (checked) {
                    gender = "M";
                    Log.d("Gender", "" + gender);
                }
                break;
            case R.id.Entreprenuer_gender_female:
                if (checked) {
                    gender = "F";
                    Log.d("Gender", "" + gender);
                }
                break;
            default:
                break;
        }

    }
*/
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
    public boolean validateDetails(String name, String email, final String password, String
            confirmPassword, String phone, String age, String company_name, String locality) {

        boolean check = true;

        if (check) {
            if (!emailIsValid(email)) {
                Entreprenuer_Email_register.setError("Invalid e-Mail");
                check = false;
            }
            if (name.isEmpty()) {
                Entreprenuer_name.setError("Name Cannot Be Empty");
                check = false;
            }
            if (age.isEmpty()) {
                Entreprenuer_age.setError("age Cannot Be Empty");
                check = false;
            }
            if (company_name.isEmpty()) {
                Entreprenuer_Company.setError("Company Name Cannot Be Empty");
                check = false;
            }
            if (locality.isEmpty()) {
                Entreprenuer_locality.setError("Locality Cannot Be Empty");
                check = false;
            }
            if (phone.isEmpty() || !(phone.length() == 10)) {
                Entreprenuer_phone.setError("Invalid Contact No.(length should be 10)");
                check = false;
            }
            if (!passIsValid(password)) {
                Entreprenuer_password.setError("Password should have minimum 5 characters");
                check = false;
            }
            if (!(confirmPassword.equals(password))) {
                Entreprenuer_confirm_password.setError("Passwords Do Not Match");
                Entreprenuer_confirm_password.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String confirmpass = Entreprenuer_confirm_password.getText().toString().trim();
                        if (!(confirmpass.equals(password))) {
                            Entreprenuer_confirm_password.setError("Passwords Do Not Match");
                        }
                    }
                });
                check = false;
            }
            if (Entreprenuer_gender_button.getCheckedRadioButtonId() < 0) {
                Toast.makeText(getActivity(), "Please Select A Gender", Toast.LENGTH_LONG).show();
                check = false;
            }
        } else {
            check = true;
        }
        return check;
    }

    public Spinner initSpinner(Spinner s, int content_array) {

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),content_array,R.layout.spinner_style);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        s.setAdapter(adapter);
        return s;
    }


    public void createAccount(final String name, String email, String password,
                              final String phone, final String locality, final String age, final String gender,
                              final String company_name, final String content) {

        final ProgressDialog loading = ProgressDialog.show(getActivity(), "Creating Account", "Please Wait...", false, false);

        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    FirebaseUser currentUser = auth.getCurrentUser();
                    String uid = currentUser.getUid();
                    String device_token = FirebaseInstanceId.getInstance().getToken();
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
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
                    userDetails.put("type", "Entreprenuer");
                    userDetails.put("status", "Hi there I'm using Lapit Chat App.");
                    userDetails.put("image", "default");
                    userDetails.put("thumb_image", "default");
                    userDetails.put("device_token", device_token);
                    userDetails.put("businessName", content);


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