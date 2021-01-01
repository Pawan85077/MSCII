package com.codinghelper.mscii;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.collection.LLRBBlackValueNode;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;


/**
 * A simple {@link Fragment} subclass.
 */
public class dashboardFragment extends Fragment {

    StorageReference storageUtility, storageTenth, storageTwelfth, storageGraduation;
    StorageReference mainStorageReference;
    FirebaseUser currentUser;
    DatabaseReference reference;
    private StorageReference storageReference;

    //ImageViews
    private ImageView resumeImageView;
    private ImageView thumbImpressionImageView;
    private ImageView signatureImageView;
    private ImageView casteCertificateImageView;
    private ImageView aadharImageView;

    private ImageView tenthAdmitImageView;
    private ImageView tenthMarksSheetImageView;
    private ImageView tenthMatriculationImageView;

    private ImageView twelfthAdmitImageView;
    private ImageView twelfthMarksSheetImageView;
    private ImageView twelfthMatriculationImageView;

    private ImageView collegeID_ImageView;
    private ImageView sem1MarksSheetImageView;
    private ImageView sem2MarksSheetImageView;
    private ImageView sem3MarksSheetImageView;
    private ImageView sem4MarksSheetImageView;
    private ImageView sem5MarksSheetImageView;
    private ImageView sem6MarksSheetImageView;


    //ProgressBars
    private ProgressBar resumeProgress;
    private ProgressBar thumbImpressionProgress;
    private ProgressBar signatureProgress;
    private ProgressBar casteCertificateProgress;
    private ProgressBar aadharProgress;

    private ProgressBar tenthAdmitProgress;
    private ProgressBar tenthMarksSheetProgress;
    private ProgressBar tenthMatriculationProgress;

    private ProgressBar twelfthAdmitProgress;
    private ProgressBar twelfthMarksSheetProgress;
    private ProgressBar twelfthMatriculationProgress;

    private ProgressBar collegeID_Progress;
    private ProgressBar sem1MarksSheetProgress;
    private ProgressBar sem2MarksSheetProgress;
    private ProgressBar sem3MarksSheetProgress;
    private ProgressBar sem4MarksSheetProgress;
    private ProgressBar sem5MarksSheetProgress;
    private ProgressBar sem6MarksSheetProgress;

    //Upload Buttons
    private Button resumeUpload;
    private Button thumbImpressionUpload;
    private Button signatureUpload;
    private Button casteCertificateUpload;
    private Button aadharUpload;

    private Button tenthAdmitUpload;
    private Button tenthMarksSheetUpload;
    private Button tenthMatriculationUpload;

    private Button twelfthAdmitUpload;
    private Button twelfthMarksSheetUpload;
    private Button twelfthMatriculationUpload;

    private Button collegeID_Upload;
    private Button sem1MarksSheetUpload;
    private Button sem2MarksSheetUpload;
    private Button sem3MarksSheetUpload;
    private Button sem4MarksSheetUpload;
    private Button sem5MarksSheetUpload;
    private Button sem6MarksSheetUpload;


    //Bottom Sheet utilities
    private BottomSheetBehavior mBehavior;
    private BottomSheetDialog mBottomSheetDialog;
    private View bottom_sheet;


    //pick variable
    private static final int pick = 2;

    //Doc_Category and doc_Type
    private static String docCategory = "";
    private static String docType = "";


    public dashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //fullScreen
        //requireActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("studentDetail").child(currentUser.getUid());
        storageUtility = FirebaseStorage.getInstance().getReference().child("utility_Documents/");
        storageTenth = FirebaseStorage.getInstance().getReference().child("tenth_Documents/");
        storageTwelfth = FirebaseStorage.getInstance().getReference().child("twelfth_Documents/");
        storageGraduation = FirebaseStorage.getInstance().getReference().child("graduation_Documents/");


        bottom_sheet = rootView.findViewById(R.id.bottom_sheet);
        mBehavior = BottomSheetBehavior.from(bottom_sheet);

        //Defining upload buttons
        resumeUpload = (Button) rootView.findViewById(R.id.resumeUpload);
        thumbImpressionUpload = (Button) rootView.findViewById(R.id.thumbImpressionUpload);
        signatureUpload = (Button) rootView.findViewById(R.id.signatureUpload);
        casteCertificateUpload = (Button) rootView.findViewById(R.id.casteCertificateUpload);
        aadharUpload = (Button) rootView.findViewById(R.id.aadharUpload);

        tenthAdmitUpload = (Button) rootView.findViewById(R.id.tenthAdmitUpload);
        tenthMarksSheetUpload = (Button) rootView.findViewById(R.id.tenthMarksSheetUpload);
        tenthMatriculationUpload = (Button) rootView.findViewById(R.id.tenthMatriculationUpload);

        twelfthAdmitUpload = (Button) rootView.findViewById(R.id.twelfthAdmitUpload);
        twelfthMarksSheetUpload = (Button) rootView.findViewById(R.id.twelfthMarksSheetUpload);
        twelfthMatriculationUpload = (Button) rootView.findViewById(R.id.twelfthMatriculationUpload);

        collegeID_Upload = (Button) rootView.findViewById(R.id.collegeID_Upload);
        sem1MarksSheetUpload = (Button) rootView.findViewById(R.id.sem1MarksSheetUpload);
        sem2MarksSheetUpload = (Button) rootView.findViewById(R.id.sem2MarksSheetUpload);
        sem3MarksSheetUpload = (Button) rootView.findViewById(R.id.sem3MarksSheetUpload);
        sem4MarksSheetUpload = (Button) rootView.findViewById(R.id.sem4MarksSheetUpload);
        sem5MarksSheetUpload = (Button) rootView.findViewById(R.id.sem5MarksSheetUpload);
        sem6MarksSheetUpload = (Button) rootView.findViewById(R.id.sem6MarksSheetUpload);


        //Defining Image Views
        resumeImageView = (ImageView) rootView.findViewById(R.id.resumeImageView);
        thumbImpressionImageView = (ImageView) rootView.findViewById(R.id.thumbImpressionImageView);
        signatureImageView = (ImageView) rootView.findViewById(R.id.signatureImageView);
        casteCertificateImageView = (ImageView) rootView.findViewById(R.id.casteCertificateImageView);
        aadharImageView = (ImageView) rootView.findViewById(R.id.aadharImageView);

        tenthAdmitImageView = (ImageView) rootView.findViewById(R.id.tenthAdmitImageView);
        tenthMarksSheetImageView = (ImageView) rootView.findViewById(R.id.tenthMarksSheetImageView);
        tenthMatriculationImageView = (ImageView) rootView.findViewById(R.id.tenthMatriculationImageView);

        twelfthAdmitImageView = (ImageView) rootView.findViewById(R.id.twelfthAdmitImageView);
        twelfthMarksSheetImageView = (ImageView) rootView.findViewById(R.id.twelfthMarksSheetImageView);
        twelfthMatriculationImageView = (ImageView) rootView.findViewById(R.id.twelfthMatriculationImageView);

        collegeID_ImageView = (ImageView) rootView.findViewById(R.id.collegeID_ImageView);
        sem1MarksSheetImageView = (ImageView) rootView.findViewById(R.id.sem1MarksSheetImageView);
        sem2MarksSheetImageView = (ImageView) rootView.findViewById(R.id.sem2MarksSheetImageView);
        sem3MarksSheetImageView = (ImageView) rootView.findViewById(R.id.sem3MarksSheetImageView);
        sem4MarksSheetImageView = (ImageView) rootView.findViewById(R.id.sem4MarksSheetImageView);
        sem5MarksSheetImageView = (ImageView) rootView.findViewById(R.id.sem5MarksSheetImageView);
        sem6MarksSheetImageView = (ImageView) rootView.findViewById(R.id.sem6MarksSheetImageView);


        //Handling Expand and Collapse
        RelativeLayout UtilityDocHeader = (RelativeLayout) rootView.findViewById(R.id.utilityDocHeader);
        final HorizontalScrollView UtilityDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.utilityDocCardScroll);
        final ImageView UtilityExpandCollapse = (ImageView) rootView.findViewById(R.id.utilityExpandCollapse);
        UtilityDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UtilityDocScroll.getVisibility() == View.GONE) {
                    UtilityDocScroll.setVisibility(View.VISIBLE);
                    docCategory = "utilityDocuments";
                    mainStorageReference = storageUtility;
                    //docType = "resume";
                    fetchUtilityDocumentsStatus(docCategory);
                    UtilityExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                } else {
                    UtilityDocScroll.setVisibility(View.GONE);
                    UtilityExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout TenthDocHeader = (RelativeLayout) rootView.findViewById(R.id.tenthDocHeader);
        final HorizontalScrollView TenthDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.tenthDocCardScroll);
        final ImageView TenthExpandCollapse = (ImageView) rootView.findViewById(R.id.tenthExpandCollapse);
        TenthDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TenthDocScroll.getVisibility() == View.GONE) {
                    TenthDocScroll.setVisibility(View.VISIBLE);
                    docCategory = "tenthDocuments";
                    mainStorageReference = storageTenth;
                    fetchTenthDocumentsStatus(docCategory);
                    TenthExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                } else {
                    TenthDocScroll.setVisibility(View.GONE);
                    TenthExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout TwelfthDocHeader = (RelativeLayout) rootView.findViewById(R.id.twelfthDocHeader);
        final HorizontalScrollView TwelfthDocScroll = (HorizontalScrollView) rootView.findViewById(R.id.twelfthDocCardScroll);
        final ImageView TwelfthExpandCollapse = (ImageView) rootView.findViewById(R.id.twelfthExpandCollapse);
        TwelfthDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TwelfthDocScroll.getVisibility() == View.GONE) {
                    TwelfthDocScroll.setVisibility(View.VISIBLE);
                    docCategory = "twelfthDocuments";
                    mainStorageReference = storageTwelfth;
                    fetchTwelfthDocumentsStatus(docCategory);
                    TwelfthExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                } else {
                    TwelfthDocScroll.setVisibility(View.GONE);
                    TwelfthExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        RelativeLayout GraduationDocHeader = (RelativeLayout) rootView.findViewById(R.id.graduationDocHeader);
        final LinearLayout GraduationDocScroll = (LinearLayout) rootView.findViewById(R.id.graduationDocCardScroll);
        final ImageView GraduationExpandCollapse = (ImageView) rootView.findViewById(R.id.graduationExpandCollapse);
        GraduationDocHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (GraduationDocScroll.getVisibility() == View.GONE) {
                    GraduationDocScroll.setVisibility(View.VISIBLE);
                    docCategory = "graduationDocuments";
                    mainStorageReference = storageGraduation;
                    fetchGraduationDocumentsStatus(docCategory);
                    GraduationExpandCollapse.setBackgroundResource(R.drawable.ic_colapse);
                } else {
                    GraduationDocScroll.setVisibility(View.GONE);
                    GraduationExpandCollapse.setBackgroundResource(R.drawable.ic_expand);
                }
            }
        });

        //Image Views OnclickListener
        resumeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "resume";
                mainStorageReference = storageUtility;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        thumbImpressionImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "thumbImpression";
                mainStorageReference = storageUtility;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        signatureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "signature";
                mainStorageReference = storageUtility;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        casteCertificateImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "casteCertificate";
                mainStorageReference = storageUtility;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        aadharImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "aadharCard";
                mainStorageReference = storageUtility;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        tenthAdmitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "tenthDocuments";
                docType = "admitCard";
                mainStorageReference = storageTenth;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        tenthMarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "tenthDocuments";
                docType = "marksSheet";
                mainStorageReference = storageTenth;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        tenthMatriculationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "tenthDocuments";
                docType = "matriculation";
                mainStorageReference = storageTenth;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        twelfthAdmitImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "twelfthDocuments";
                docType = "admitCard";
                mainStorageReference = storageTwelfth;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        twelfthMarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "twelfthDocuments";
                docType = "marksSheet";
                mainStorageReference = storageTwelfth;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        twelfthMatriculationImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "twelfthDocuments";
                docType = "matriculation";
                mainStorageReference = storageTwelfth;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        collegeID_ImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "collegeID_card";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        sem1MarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem1_marksSheet";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        sem2MarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem2_marksSheet";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        sem3MarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem3_marksSheet";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        sem4MarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem4_marksSheet";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        sem5MarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem5_marksSheet";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });

        sem6MarksSheetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem6_marksSheet";
                mainStorageReference = storageGraduation;
                showBottomSheetDialog(docCategory, docType);
            }
        });


        //Upload Buttons OnclickListener
        resumeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "resume";
                mainStorageReference = storageUtility;
                openGallery();
            }
        });

        thumbImpressionUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "thumbImpression";
                mainStorageReference = storageUtility;
                openGallery();
            }
        });

        signatureUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "signature";
                mainStorageReference = storageUtility;
                openGallery();
            }
        });

        casteCertificateUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "casteCertificate";
                mainStorageReference = storageUtility;
                openGallery();
            }
        });

        aadharUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "utilityDocuments";
                docType = "aadharCard";
                mainStorageReference = storageUtility;
                openGallery();
            }
        });

        tenthAdmitUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "tenthDocuments";
                docType = "admitCard";
                mainStorageReference = storageTenth;
                openGallery();
            }
        });

        tenthMarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "tenthDocuments";
                docType = "marksSheet";
                mainStorageReference = storageTenth;
                openGallery();
            }
        });

        tenthMatriculationUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "tenthDocuments";
                docType = "matriculation";
                mainStorageReference = storageTenth;
                openGallery();
            }
        });

        twelfthAdmitUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "twelfthDocuments";
                docType = "admitCard";
                mainStorageReference = storageTwelfth;
                openGallery();
            }
        });

        twelfthMarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "twelfthDocuments";
                docType = "marksSheet";
                mainStorageReference = storageTwelfth;
                openGallery();
            }
        });

        twelfthMatriculationUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "twelfthDocuments";
                docType = "matriculation";
                mainStorageReference = storageTwelfth;
                openGallery();
            }
        });

        collegeID_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "collegeID_card";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });

        sem1MarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem1_marksSheet";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });

        sem2MarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem2_marksSheet";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });

        sem3MarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem3_marksSheet";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });

        sem4MarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem4_marksSheet";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });

        sem5MarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem5_marksSheet";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });

        sem6MarksSheetUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docCategory = "graduationDocuments";
                docType = "sem6_marksSheet";
                mainStorageReference = storageGraduation;
                openGallery();
            }
        });


        //initToolbar();
        getActivity().setTitle("Dashboard");
        return rootView;
    }

    //function for opening Gallery
    private void openGallery() {
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery, pick);
    }

    //Called automatically when image is selected
    @RequiresApi(api = Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("WrongConstant")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == pick && resultCode == Activity.RESULT_OK && data != null) {
            Uri image = data.getData();
            CropImage.activity(image)
                    .start(getContext(), this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == Activity.RESULT_OK) {
                Uri resultUri = result.getUri();

                //uploading on cropping
                if (resultUri != null) {
                    final ProgressDialog progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("Uploading your Document...");
                    progressDialog.show();


                    final StorageReference ref = mainStorageReference.child(currentUser.getUid() + docType);
                    ref.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            Toast.makeText(getContext().getApplicationContext(), "Document Uploaded", Toast.LENGTH_SHORT).show();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    reference.child("personalDocuments").child(docCategory).child(docType).setValue(String.valueOf(uri))
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(getContext().getApplicationContext(), "Database updated Successfully...", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                            });
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getContext().getApplicationContext(), "Failed!" + e.getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage("Uploaded " + (int) progress + " %");
                                }
                            });
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //fetching Utility documents status
    private void fetchUtilityDocumentsStatus(String docCategoryUtility) {

        //URLs of images
        final String[] resumeURL = new String[1];
        final String[] thumbImpressionURL = new String[1];
        final String[] signatureURL = new String[1];
        final String[] casteCertificateURL = new String[1];
        final String[] aadharCardURL = new String[1];

        mainStorageReference = storageUtility;


        // defining ProgressBars
        resumeProgress = (ProgressBar) getView().findViewById(R.id.resumeProgress);
        thumbImpressionProgress = (ProgressBar) getView().findViewById(R.id.thumbImpressionProgress);
        signatureProgress = (ProgressBar) getView().findViewById(R.id.signatureProgress);
        casteCertificateProgress = (ProgressBar) getView().findViewById(R.id.casteCertificateProgress);
        aadharProgress = (ProgressBar) getView().findViewById(R.id.aadharProgress);

        DatabaseReference ref = reference.child("personalDocuments").child(docCategoryUtility);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for Resume
                resumeURL[0] = String.valueOf(dataSnapshot.child("resume").getValue());
                if (URLUtil.isValidUrl(resumeURL[0])) {
                    resumeImageView.setVisibility(View.VISIBLE);
                    resumeProgress.setVisibility(View.VISIBLE);
                    resumeUpload.setVisibility(View.INVISIBLE);
                    if (resumeImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(resumeURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        resumeProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        resumeProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(resumeImageView);
                    }

                } else {
                    resumeImageView.setVisibility(View.INVISIBLE);
                    resumeProgress.setVisibility(View.INVISIBLE);
                    resumeUpload.setVisibility(View.VISIBLE);
                }

                //for thumb Impression
                thumbImpressionURL[0] = String.valueOf(dataSnapshot.child("thumbImpression").getValue());
                if (URLUtil.isValidUrl(thumbImpressionURL[0])) {
                    thumbImpressionImageView.setVisibility(View.VISIBLE);
                    thumbImpressionProgress.setVisibility(View.VISIBLE);
                    thumbImpressionUpload.setVisibility(View.INVISIBLE);
                    if (thumbImpressionImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(thumbImpressionURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        thumbImpressionProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        thumbImpressionProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(thumbImpressionImageView);
                    }

                } else {
                    thumbImpressionImageView.setVisibility(View.INVISIBLE);
                    thumbImpressionProgress.setVisibility(View.INVISIBLE);
                    thumbImpressionUpload.setVisibility(View.VISIBLE);
                }

                //for Signature
                signatureURL[0] = String.valueOf(dataSnapshot.child("signature").getValue());
                if (URLUtil.isValidUrl(signatureURL[0])) {
                    signatureImageView.setVisibility(View.VISIBLE);
                    signatureProgress.setVisibility(View.VISIBLE);
                    signatureUpload.setVisibility(View.INVISIBLE);
                    if (signatureImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(signatureURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        signatureProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        signatureProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(signatureImageView);
                    }

                } else {
                    signatureImageView.setVisibility(View.INVISIBLE);
                    signatureProgress.setVisibility(View.INVISIBLE);
                    signatureUpload.setVisibility(View.VISIBLE);
                }

                //for Caste certificate
                casteCertificateURL[0] = String.valueOf(dataSnapshot.child("casteCertificate").getValue());
                if (URLUtil.isValidUrl(casteCertificateURL[0])) {
                    casteCertificateImageView.setVisibility(View.VISIBLE);
                    casteCertificateProgress.setVisibility(View.VISIBLE);
                    casteCertificateUpload.setVisibility(View.INVISIBLE);
                    if (casteCertificateImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(casteCertificateURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        casteCertificateProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        casteCertificateProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(casteCertificateImageView);
                    }

                } else {
                    casteCertificateImageView.setVisibility(View.INVISIBLE);
                    casteCertificateProgress.setVisibility(View.INVISIBLE);
                    casteCertificateUpload.setVisibility(View.VISIBLE);
                }

                //for Aadhar Card
                aadharCardURL[0] = String.valueOf(dataSnapshot.child("aadharCard").getValue());
                if (URLUtil.isValidUrl(aadharCardURL[0])) {
                    aadharImageView.setVisibility(View.VISIBLE);
                    aadharProgress.setVisibility(View.VISIBLE);
                    aadharUpload.setVisibility(View.INVISIBLE);
                    if (aadharImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(aadharCardURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        aadharProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        aadharProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(aadharImageView);
                    }

                } else {
                    aadharImageView.setVisibility(View.INVISIBLE);
                    aadharProgress.setVisibility(View.INVISIBLE);
                    aadharUpload.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //fetching tenth documents status
    private void fetchTenthDocumentsStatus(String docCategoryTenth) {

        //URLs of images
        final String[] tenthAdmitURL = new String[1];
        final String[] tenthMarksSheetURL = new String[1];
        final String[] tenthMatriculationURL = new String[1];

        mainStorageReference = storageTenth;

        // defining ProgressBars
        tenthAdmitProgress = (ProgressBar) getView().findViewById(R.id.tenthAdmitProgress);
        tenthMarksSheetProgress = (ProgressBar) getView().findViewById(R.id.tenthMarksSheetProgress);
        tenthMatriculationProgress = (ProgressBar) getView().findViewById(R.id.tenthMatriculationProgress);

        DatabaseReference ref = reference.child("personalDocuments").child(docCategoryTenth);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for AdmitCard
                tenthAdmitURL[0] = String.valueOf(dataSnapshot.child("admitCard").getValue());
                if (URLUtil.isValidUrl(tenthAdmitURL[0])) {
                    tenthAdmitImageView.setVisibility(View.VISIBLE);
                    tenthAdmitProgress.setVisibility(View.VISIBLE);
                    tenthAdmitUpload.setVisibility(View.INVISIBLE);
                    if (tenthAdmitImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(tenthAdmitURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        tenthAdmitProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        tenthAdmitProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(tenthAdmitImageView);
                    }

                } else {
                    tenthAdmitImageView.setVisibility(View.INVISIBLE);
                    tenthAdmitProgress.setVisibility(View.INVISIBLE);
                    tenthAdmitUpload.setVisibility(View.VISIBLE);
                }

                //for MarksSheet
                tenthMarksSheetURL[0] = String.valueOf(dataSnapshot.child("marksSheet").getValue());
                if (URLUtil.isValidUrl(tenthMarksSheetURL[0])) {
                    tenthMarksSheetImageView.setVisibility(View.VISIBLE);
                    tenthMarksSheetProgress.setVisibility(View.VISIBLE);
                    tenthMarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (tenthMarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(tenthMarksSheetURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        tenthMarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        tenthMarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(tenthMarksSheetImageView);
                    }

                } else {
                    tenthMarksSheetImageView.setVisibility(View.INVISIBLE);
                    tenthMarksSheetProgress.setVisibility(View.INVISIBLE);
                    tenthMarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for Matriculetion
                tenthMatriculationURL[0] = String.valueOf(dataSnapshot.child("matriculation").getValue());
                if (URLUtil.isValidUrl(tenthMatriculationURL[0])) {
                    tenthMatriculationImageView.setVisibility(View.VISIBLE);
                    tenthMatriculationProgress.setVisibility(View.VISIBLE);
                    tenthMatriculationUpload.setVisibility(View.INVISIBLE);
                    if (tenthMatriculationImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(tenthMatriculationURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        tenthMatriculationProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        tenthMatriculationProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(tenthMatriculationImageView);
                    }

                } else {
                    tenthMatriculationImageView.setVisibility(View.INVISIBLE);
                    tenthMatriculationProgress.setVisibility(View.INVISIBLE);
                    tenthMatriculationUpload.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //fetching twelfth documents status
    private void fetchTwelfthDocumentsStatus(String docCategoryTwelfth) {

        //URLs of images
        final String[] twelfthAdmitURL = new String[1];
        final String[] twelfthMarksSheetURL = new String[1];
        final String[] twelfthMatriculationURL = new String[1];

        mainStorageReference = storageTwelfth;

        // defining ProgressBars
        twelfthAdmitProgress = (ProgressBar) getView().findViewById(R.id.twelfthAdmitProgress);
        twelfthMarksSheetProgress = (ProgressBar) getView().findViewById(R.id.twelfthMarksSheetProgress);
        twelfthMatriculationProgress = (ProgressBar) getView().findViewById(R.id.twelfthMatriculationProgress);

        DatabaseReference ref = reference.child("personalDocuments").child(docCategoryTwelfth);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for AdmitCard
                twelfthAdmitURL[0] = String.valueOf(dataSnapshot.child("admitCard").getValue());
                if (URLUtil.isValidUrl(twelfthAdmitURL[0])) {
                    twelfthAdmitImageView.setVisibility(View.VISIBLE);
                    twelfthAdmitProgress.setVisibility(View.VISIBLE);
                    twelfthAdmitUpload.setVisibility(View.INVISIBLE);
                    if (twelfthAdmitImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(twelfthAdmitURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        twelfthAdmitProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        twelfthAdmitProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(twelfthAdmitImageView);
                    }

                } else {
                    twelfthAdmitImageView.setVisibility(View.INVISIBLE);
                    twelfthAdmitProgress.setVisibility(View.INVISIBLE);
                    twelfthAdmitUpload.setVisibility(View.VISIBLE);
                }

                //for MarksSheet
                twelfthMarksSheetURL[0] = String.valueOf(dataSnapshot.child("marksSheet").getValue());
                if (URLUtil.isValidUrl(twelfthMarksSheetURL[0])) {
                    twelfthMarksSheetImageView.setVisibility(View.VISIBLE);
                    twelfthMarksSheetProgress.setVisibility(View.VISIBLE);
                    twelfthMarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (twelfthMarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(twelfthMarksSheetURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        twelfthMarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        twelfthMarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(twelfthMarksSheetImageView);
                    }

                } else {
                    twelfthMarksSheetImageView.setVisibility(View.INVISIBLE);
                    twelfthMarksSheetProgress.setVisibility(View.INVISIBLE);
                    twelfthMarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for Matriculetion
                twelfthMatriculationURL[0] = String.valueOf(dataSnapshot.child("matriculation").getValue());
                if (URLUtil.isValidUrl(twelfthMatriculationURL[0])) {
                    twelfthMatriculationImageView.setVisibility(View.VISIBLE);
                    twelfthMatriculationProgress.setVisibility(View.VISIBLE);
                    twelfthMatriculationUpload.setVisibility(View.INVISIBLE);
                    if (twelfthMatriculationImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(twelfthMatriculationURL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        twelfthMatriculationProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        twelfthMatriculationProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(twelfthMatriculationImageView);
                    }

                } else {
                    twelfthMatriculationImageView.setVisibility(View.INVISIBLE);
                    twelfthMatriculationProgress.setVisibility(View.INVISIBLE);
                    twelfthMatriculationUpload.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Show Bottom sheet dialogue
    private void showBottomSheetDialog(final String docCategoryy, final String docTypey) {

        final String[] imageUrl = new String[1];
        final DatabaseReference ref = reference.child("personalDocuments").child(docCategoryy);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                imageUrl[0] = String.valueOf(dataSnapshot.child(docTypey).getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        if (mBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            mBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        final View view = getLayoutInflater().inflate(R.layout.bottom_sheet_delete, null);

        ((View) view.findViewById(R.id.viewFullScreenImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext().getApplicationContext(), "Showing Document in Full-Screen Mode", Toast.LENGTH_SHORT).show();
                if (URLUtil.isValidUrl(imageUrl[0])) {
                    Intent fullScreenDocIntent = new Intent(getContext().getApplicationContext(), UserDocumentView.class);
                    fullScreenDocIntent.putExtra("docCategory", docCategoryy);
                    fullScreenDocIntent.putExtra("docType", docTypey);
                    startActivity(fullScreenDocIntent);
                }
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.shareImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (URLUtil.isValidUrl(imageUrl[0])) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "Here is a link of my uploaded document from MCR Infotech mobile App\n\nClick on the Link below to view\n\n";
                    String shareSub = "MCR Infotech";
                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody + imageUrl[0]);
                    startActivity(Intent.createChooser(sharingIntent, "Share using"));
                }
                mBottomSheetDialog.dismiss();
            }
        });

        ((View) view.findViewById(R.id.deleteImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (URLUtil.isValidUrl(imageUrl[0])) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.AlertDialogTheme);
                    builder.setTitle("Delete Confirmation");
                    builder.setIcon(R.drawable.ic_warning);
                    builder.setMessage("Are you sure ?\nDo you really wanna delete selected document ?");
                    builder.setPositiveButton("PROCEED", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl[0]);
                            storageReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getContext().getApplicationContext(), "Document deleted successfully", Toast.LENGTH_SHORT).show();
                                    ref.child(docTypey).removeValue();
                                    Log.e("onSuccess:", " deleted file");

                                    //calling all categories status
                                    //later have to put switch cases for early loading
                                    fetchUtilityDocumentsStatus(docCategoryy);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext().getApplicationContext(), "SERVER ERROR...!!!\nCould not delete document", Toast.LENGTH_SHORT).show();
                                    Log.e("onFailure:", " could not delete file");
                                }
                            });
                        }
                    });
                    builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(getContext().getApplicationContext(), "You calcelled deletion", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.show();

                }
                mBottomSheetDialog.dismiss();
            }
        });


        mBottomSheetDialog = new BottomSheetDialog(getContext());
        mBottomSheetDialog.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mBottomSheetDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }

    //fetching Graduation documents status
    private void fetchGraduationDocumentsStatus(String docCategoryGraduation) {

        //URLs of images
        final String[] collegeID_URL = new String[1];
        final String[] sem1_marksSheet_URL = new String[1];
        final String[] sem2_marksSheet_URL = new String[1];
        final String[] sem3_marksSheet_URL = new String[1];
        final String[] sem4_marksSheet_URL = new String[1];
        final String[] sem5_marksSheet_URL = new String[1];
        final String[] sem6_marksSheet_URL = new String[1];

        mainStorageReference = storageGraduation;


        // defining ProgressBars
        collegeID_Progress = (ProgressBar) getView().findViewById(R.id.collegeID_Progress);
        sem1MarksSheetProgress = (ProgressBar) getView().findViewById(R.id.sem1MarksSheetProgress);
        sem2MarksSheetProgress = (ProgressBar) getView().findViewById(R.id.sem2MarksSheetProgress);
        sem3MarksSheetProgress = (ProgressBar) getView().findViewById(R.id.sem3MarksSheetProgress);
        sem4MarksSheetProgress = (ProgressBar) getView().findViewById(R.id.sem4MarksSheetProgress);
        sem5MarksSheetProgress = (ProgressBar) getView().findViewById(R.id.sem5MarksSheetProgress);
        sem6MarksSheetProgress = (ProgressBar) getView().findViewById(R.id.sem6MarksSheetProgress);

        DatabaseReference ref = reference.child("personalDocuments").child(docCategoryGraduation);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //for college ID card
                collegeID_URL[0] = String.valueOf(dataSnapshot.child("collegeID_card").getValue());
                if (URLUtil.isValidUrl(collegeID_URL[0])) {
                    collegeID_ImageView.setVisibility(View.VISIBLE);
                    collegeID_Progress.setVisibility(View.VISIBLE);
                    collegeID_Upload.setVisibility(View.INVISIBLE);
                    if (collegeID_ImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(collegeID_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        collegeID_Progress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        collegeID_Progress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(collegeID_ImageView);
                    }

                } else {
                    collegeID_ImageView.setVisibility(View.INVISIBLE);
                    collegeID_Progress.setVisibility(View.INVISIBLE);
                    collegeID_Upload.setVisibility(View.VISIBLE);
                }

                //for sem 1 marksSheet
                sem1_marksSheet_URL[0] = String.valueOf(dataSnapshot.child("sem1_marksSheet").getValue());
                if (URLUtil.isValidUrl(sem1_marksSheet_URL[0])) {
                    sem1MarksSheetImageView.setVisibility(View.VISIBLE);
                    sem1MarksSheetProgress.setVisibility(View.VISIBLE);
                    sem1MarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (sem1MarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(sem1_marksSheet_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        sem1MarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        sem1MarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(sem1MarksSheetImageView);
                    }

                } else {
                    sem1MarksSheetImageView.setVisibility(View.INVISIBLE);
                    sem1MarksSheetProgress.setVisibility(View.INVISIBLE);
                    sem1MarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for sem 2 marksSheet
                sem2_marksSheet_URL[0] = String.valueOf(dataSnapshot.child("sem2_marksSheet").getValue());
                if (URLUtil.isValidUrl(sem2_marksSheet_URL[0])) {
                    sem2MarksSheetImageView.setVisibility(View.VISIBLE);
                    sem2MarksSheetProgress.setVisibility(View.VISIBLE);
                    sem2MarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (sem2MarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(sem2_marksSheet_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        sem2MarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        sem2MarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(sem2MarksSheetImageView);
                    }

                } else {
                    sem2MarksSheetImageView.setVisibility(View.INVISIBLE);
                    sem2MarksSheetProgress.setVisibility(View.INVISIBLE);
                    sem2MarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for sem 3 marksSheet
                sem3_marksSheet_URL[0] = String.valueOf(dataSnapshot.child("sem3_marksSheet").getValue());
                if (URLUtil.isValidUrl(sem3_marksSheet_URL[0])) {
                    sem3MarksSheetImageView.setVisibility(View.VISIBLE);
                    sem3MarksSheetProgress.setVisibility(View.VISIBLE);
                    sem3MarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (sem3MarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(sem3_marksSheet_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        sem3MarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        sem3MarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(sem3MarksSheetImageView);
                    }

                } else {
                    sem3MarksSheetImageView.setVisibility(View.INVISIBLE);
                    sem3MarksSheetProgress.setVisibility(View.INVISIBLE);
                    sem3MarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for sem 4 marksSheet
                sem4_marksSheet_URL[0] = String.valueOf(dataSnapshot.child("sem4_marksSheet").getValue());
                if (URLUtil.isValidUrl(sem4_marksSheet_URL[0])) {
                    sem4MarksSheetImageView.setVisibility(View.VISIBLE);
                    sem4MarksSheetProgress.setVisibility(View.VISIBLE);
                    sem4MarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (sem4MarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(sem4_marksSheet_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        sem4MarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        sem4MarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(sem4MarksSheetImageView);
                    }

                } else {
                    sem4MarksSheetImageView.setVisibility(View.INVISIBLE);
                    sem4MarksSheetProgress.setVisibility(View.INVISIBLE);
                    sem4MarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for sem 5 marksSheet
                sem5_marksSheet_URL[0] = String.valueOf(dataSnapshot.child("sem5_marksSheet").getValue());
                if (URLUtil.isValidUrl(sem5_marksSheet_URL[0])) {
                    sem5MarksSheetImageView.setVisibility(View.VISIBLE);
                    sem5MarksSheetProgress.setVisibility(View.VISIBLE);
                    sem5MarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (sem5MarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(sem5_marksSheet_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        sem5MarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        sem5MarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(sem5MarksSheetImageView);
                    }

                } else {
                    sem5MarksSheetImageView.setVisibility(View.INVISIBLE);
                    sem5MarksSheetProgress.setVisibility(View.INVISIBLE);
                    sem5MarksSheetUpload.setVisibility(View.VISIBLE);
                }

                //for sem 6 marksSheet
                sem6_marksSheet_URL[0] = String.valueOf(dataSnapshot.child("sem6_marksSheet").getValue());
                if (URLUtil.isValidUrl(sem6_marksSheet_URL[0])) {
                    sem6MarksSheetImageView.setVisibility(View.VISIBLE);
                    sem6MarksSheetProgress.setVisibility(View.VISIBLE);
                    sem6MarksSheetUpload.setVisibility(View.INVISIBLE);
                    if (sem6MarksSheetImageView != null) {
                        Glide.with(getContext().getApplicationContext())
                                .load(sem6_marksSheet_URL[0])
                                .listener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        sem6MarksSheetProgress.setVisibility(View.GONE);
                                        Toast.makeText(getContext().getApplicationContext(), "Error loading document\nPls check your internet connectivity", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        sem6MarksSheetProgress.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(sem6MarksSheetImageView);
                    }

                } else {
                    sem6MarksSheetImageView.setVisibility(View.INVISIBLE);
                    sem6MarksSheetProgress.setVisibility(View.INVISIBLE);
                    sem6MarksSheetUpload.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


//    @Override
//    public void onDetach() {
//        super.onDetach();
//        requireActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//    }

//
//    @Override
//    public void onResume() {
//        super.onResume();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }
}
