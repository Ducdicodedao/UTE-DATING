package com.client.utedating.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.client.utedating.R;
import com.client.utedating.models.NoResultModel;
import com.client.utedating.models.User;
import com.client.utedating.retrofit.RetrofitClient;
import com.client.utedating.retrofit.UserApiService;
import com.client.utedating.sharedPreferences.SharedPreferencesClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaceVerifyActivity extends AppCompatActivity {
    ImageView imageViewCloseVerify, imageViewPose, imageViewFace;
    CardView cardViewPose, cardViewFace;
    LinearLayout linearLayoutTakeCapture, linearLayoutVerify;
    AppCompatButton buttonTakeCapture, buttonVerify, buttonRetake;

    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    public static Bitmap cropped;
    public Bitmap avatarBitmap, testBitmap;
    protected Interpreter tflite;
    float[][] avatar_embedding = new float[1][128];
    float[][] test_embedding = new float[1][128];
    private int imageSizeX;
    private int imageSizeY;

    SharedPreferencesClient sharedPreferencesClient;
    User user;
    UserApiService userApiService;

    private final int REQUEST_CAMERA_CODE = 1;
    private final int REQUEST_TAKE_CAPTURE_CODE =2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_verify);

        getPermission();
        initView();
        setData();
        handleEvent();
    }

    public void getPermission() {
//        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(FaceVerifyActivity.this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE);
        }
    }

    private void initView() {
        imageViewCloseVerify = findViewById(R.id.imageViewCloseVerify);
        imageViewPose = findViewById(R.id.imageViewPose);
        imageViewFace = findViewById(R.id.imageViewFace);
        cardViewPose = findViewById(R.id.cardViewPose);
        cardViewFace = findViewById(R.id.cardViewFace);
        linearLayoutTakeCapture = findViewById(R.id.linearLayoutTakeCapture);
        linearLayoutVerify = findViewById(R.id.linearLayoutVerify);
        buttonTakeCapture = findViewById(R.id.buttonTakeCapture);
        buttonVerify = findViewById(R.id.buttonVerify);
        buttonRetake = findViewById(R.id.buttonRetake);
    }

    private void setData() {
        sharedPreferencesClient = new SharedPreferencesClient(this);
        user = sharedPreferencesClient.getUserInfo("user");
        userApiService = RetrofitClient.getInstance().create(UserApiService.class);
        String url;
        if(user.getGender().equals("male")){
            url = "https://img.freepik.com/free-photo/medium-shot-man-taking-selfie_23-2148999104.jpg?w=996&t=st=1683998907~exp=1683999507~hmac=9f2eb67d2b600ed4359edefc65f60ea60ffed33b38fc6cd2c2dd472d6252335b";

        }
        else {
            url = "https://img.freepik.com/free-photo/portrait-female-traveller-park_23-2148570648.jpg?w=996&t=st=1683999292~exp=1683999892~hmac=77e62dba987e62353603ebadd7e11dbd258d17206e8dab0fa05dd01ed71044ad";
        }
        Glide
                .with(this)
                .load(url)
                .centerCrop()
                .into(imageViewPose);

        Glide.with(this)
                .asBitmap()
                .load(user.getAvatar())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        avatarBitmap = resource;
                        face_detector(avatarBitmap, "avatar");
                    }
                });
    }

    private void handleEvent() {
        try {
            tflite = new Interpreter(loadmodelfile(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

        imageViewCloseVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        buttonTakeCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTakeCapture();
            }
        });

        buttonRetake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTakeCapture();
            }
        });

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double distance = calculate_distance(avatar_embedding, test_embedding);
                Log.e("TAG", String.valueOf(distance));
                Dialog dialog = new Dialog(FaceVerifyActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                if (distance < 6.0) {
                    userApiService.verifyUser(user.get_id()).enqueue(new Callback<NoResultModel>() {
                        @Override
                        public void onResponse(Call<NoResultModel> call, Response<NoResultModel> response) {
                            if(response.isSuccessful()){
                                Log.e("TAG", response.body().getMessage());
                                user.setAuthenticated(true);
                                sharedPreferencesClient.putUserInfo("user", user);
                            }
                        }

                        @Override
                        public void onFailure(Call<NoResultModel> call, Throwable t) {
                            Log.e("TAG", t.getMessage());
                        }
                    });
                    dialog.setContentView(R.layout.dialog_face_verify_success);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setDimAmount(0.6f);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Button btnOk = dialog.findViewById(R.id.btn_verifySuccess);
                    btnOk.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
                else {
                    dialog.setContentView(R.layout.dialog_face_verify_fail);
                    dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.getWindow().setDimAmount(0.6f);
                    dialog.getWindow().setGravity(Gravity.CENTER);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Button btnFail = dialog.findViewById(R.id.btn_verifyFail);
                    TextView textViewGoHome = dialog.findViewById(R.id.btn_verifyGoHome);
                    btnFail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            handleTakeCapture();
                        }
                    });
                    textViewGoHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void handleTakeCapture() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, REQUEST_TAKE_CAPTURE_CODE);

//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 13);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        //Sử dụng Tensorflow Lite để tải và chạy mô hình nhận dạng khuôn mặt được đặt tên là "Qfacenet.tflite".
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd("Qfacenet.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startoffset, declaredLength);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_CAPTURE_CODE && data != null) {
            try {
                testBitmap = (Bitmap) data.getExtras().get("data");
                imageViewFace.setImageBitmap(testBitmap);
                face_detector(testBitmap, "test");
                cardViewFace.setVisibility(View.VISIBLE);
                linearLayoutTakeCapture.setVisibility(View.GONE);
                linearLayoutVerify.setVisibility(View.VISIBLE);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

//        if (requestCode == 13 && resultCode == RESULT_OK && data != null) {
//            Uri imageuri = data.getData();
//            try {
//                testBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
//                imageViewFace.setImageBitmap(testBitmap);
//                face_detector(testBitmap, "test");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }

    public void face_detector(final Bitmap bitmap, final String imagetype) {
        final InputImage image = InputImage.fromBitmap(bitmap, 0);
        //Sử dụng Google ML Kit để phát hiện khuôn mặt trong hình ảnh và trích xuất đặc trưng của khuôn mặt
        // từ các vùng phát hiện bằng cách sử dụng FaceDetector.
        FaceDetector detector = FaceDetection.getClient();
        detector.process(image)
                .addOnSuccessListener(
                        new OnSuccessListener<List<Face>>() {
                            @Override
                            public void onSuccess(List<Face> faces) {
                                //Nếu không phát hiện ra được khuôn mặt
                                if (faces.size() == 0) {
                                    Toast.makeText(FaceVerifyActivity.this, "No Face Detected",Toast.LENGTH_LONG ).show();
                                }
                                for (Face face : faces) {
                                    Rect bounds = face.getBoundingBox();
                                    //Với mỗi khuôn mặt được phát hiện, tạo một hình ảnh Bitmap được cắt ra từ vùng phát hiện và truyền vào phương thức get_embaddings để trích xuất các đặc trưng của khuôn mặt.
                                    cropped = Bitmap.createBitmap(bitmap, bounds.left, bounds.top,
                                            bounds.width(), bounds.height());
                                    get_embaddings(cropped, imagetype);
                                }
                            }

                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
    }
    //trích xuất các đặc trưng (embedding) từ một hình ảnh Bitmap bằng cách sử dụng một mô hình máy học TensorFlow Lite.
    public void get_embaddings(Bitmap bitmap, String imagetype) {

        TensorImage inputImageBuffer;
        // biến lưu 128 đặc trưng để so sánh
        float[][] embedding = new float[1][128];

        int imageTensorIndex = 0;
        int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
        imageSizeY = imageShape[1];
        imageSizeX = imageShape[2];
        DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

        inputImageBuffer = new TensorImage(imageDataType);

        inputImageBuffer = loadImage(bitmap, inputImageBuffer);

        tflite.run(inputImageBuffer.getBuffer(), embedding);

        if (imagetype.equals("avatar"))
            avatar_embedding = embedding;
        else if (imagetype.equals("test"))
            test_embedding = embedding;
    }

    //Chuẩn hóa hình ảnh
    private TensorImage loadImage(final Bitmap bitmap, TensorImage inputImageBuffer) {
        // Loads bitmap into a TensorImage.
        inputImageBuffer.load(bitmap);

        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }

    //Tính toán khoảng cách giữa hai đặc trưng khuôn mặt được trích xuất để xác định xem chúng có phải là cùng một khuôn mặt hay không.
    private double calculate_distance(float[][] avatar_embedding, float[][] test_embedding) {
        double sum = 0.0;
        for (int i = 0; i < 128; i++) {
            sum = sum + Math.pow((avatar_embedding[0][i] - test_embedding[0][i]), 2.0);
        }
        return Math.sqrt(sum);
    }
}