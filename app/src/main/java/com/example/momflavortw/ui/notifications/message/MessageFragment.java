package com.example.momflavortw.ui.notifications.message;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.momflavortw.R;
import com.example.momflavortw.data.Common;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class MessageFragment extends Fragment {
    private static final int PICK_IMAGE_REQUEST = 123;
    private Uri fileUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private RecyclerView mRecyclerview;
    private String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
    private List<Message> msg ;
    private String notification_message,autoMessage;
    MessageAdapter mAdapter;
    private List<LastMessage> mLastMessage;
    Runnable runNotification,setFirstMessage;
    Handler handler = new Handler();
    String [] adminId;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_message, container, false);

        final Common common = new Common();
        common.setNewMsg(false);
        mRecyclerview = root.findViewById(R.id.recycler_view_message);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setStackFromEnd(true);
        mRecyclerview.setLayoutManager(layoutManager);
        msg = new ArrayList<>();


        runNotification = new Runnable() {
            @Override
            public void run() {
                pushNotification();
            }
        };

        setFirstMessage = new Runnable(){
            @Override
            public void run() {
                setFirst();
            }
        };


        final EditText etMessage = root.findViewById(R.id.etMessage);
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Contact").document("message")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()) {
                            Message message = document.toObject(Message.class);
                            autoMessage = message.getAutoMessage();
                        }
                    }
                });
        handler.postDelayed(setFirstMessage,1000);



        mLastMessage = new ArrayList<>();

        CollectionReference ref = db.collection("message").document(uid).collection("message");
        Query query = ref.orderBy("date", Query.Direction.ASCENDING);
        query
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            switch (dc.getType()) {
                                case ADDED:
                                    Message message = dc.getDocument().toObject(Message.class);
                                    msg.add(message);
                                    Log.d(TAG, "New message: " + dc.getDocument().getData());
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified message: " + dc.getDocument().getData());
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed message: " + dc.getDocument().getData());
                                    break;
                            }
                        }
                        mAdapter = new MessageAdapter(getActivity(),msg);
                        mRecyclerview.setAdapter(mAdapter);
                        mRecyclerview.scrollToPosition(msg.size()-1);


                        db.collection("message").document(uid)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.isSuccessful()){
                                            DocumentSnapshot document = task.getResult();
                                            if(document.exists()) {
                                                LastMessage lastMessage = document.toObject(LastMessage.class);
                                                if(lastMessage.getFrom()!= uid && common.isMsgAlet()==false){
                                                    Map<String,Object> read = new HashMap<>();
                                                    read.put("read",1);
                                                    db.collection("message").document(uid)
                                                            .update(read);
                                                }
                                            }
                                        }
                                    }
                                });


                    }
                });

        final ImageButton button = root.findViewById(R.id.btnSendMsg);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMessage.getText().toString().equals("")){

                }else {
                    notification_message = etMessage.getText().toString();

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                    final String rt = sdf.format(calendar.getTime());
                    Map<String, Object> message = new HashMap<>();
                    message.put("message", etMessage.getText().toString().replace("\n","*NN"));
                    message.put("email", email);
                    message.put("uid", uid);
                    message.put("type","message");
                    message.put("date", rt);
                    message.put("administrator",false);
                    db.collection("message").document(uid).collection("message")
                            .add(message);


                    Map<String,Object> lastmsg = new HashMap<>();
                    lastmsg.put("lastmsg",etMessage.getText().toString().replace("\n","*NN"));
                    lastmsg.put("date",rt);
                    lastmsg.put("email",email);
                    lastmsg.put("from",uid);
                    lastmsg.put("read",0);


                    db.collection("message").document(uid)
                            .update(lastmsg);
                    etMessage.setText("");

                    handler.postDelayed(runNotification,1000);
                }

            }
        });


        final ImageButton buttonImg = root.findViewById(R.id.btnChooseImg);
        buttonImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "result"), PICK_IMAGE_REQUEST);

            }
        });


        return root;
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
            final String rt = sdf.format(calendar.getTime());
            fileUri = data.getData();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Image Files");

            final StorageReference filePath = storageReference.child(uid+"-"+rt+"."+"jpg");
            uploadTask = filePath.putFile(fileUri);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return filePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        myUri = downloadUri.toString();
                        Map<String, Object> message = new HashMap<>();
                        message.put("message", myUri);
                        message.put("email", email);
                        message.put("uid", uid);
                        message.put("type","image");
                        message.put("date", rt);
                        message.put("administrator",false);
                        db.collection("message").document(uid).collection("message")
                                .add(message);
                        Map<String,Object> lastmsg = new HashMap<>();
                        lastmsg.put("lastmsg","Image");
                        lastmsg.put("date",rt);
                        lastmsg.put("email",email);
                        lastmsg.put("from",uid);
                        lastmsg.put("read",0);
                        db.collection("message").document(uid)
                                .update(lastmsg);
                        notification_message = "圖片";
                        handler.postDelayed(runNotification,1000);

                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });



        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void pushNotification(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
       /* db.collection("message").document(uid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()) {
                                LastMessage lastMessage = document.toObject(LastMessage.class);
                                if(lastMessage.getRead()==0){
                                    Map<String,Object> notification = new HashMap<>();
                                    notification.put("message",notification_message);
                                    notification.put("from",uid);
                                   // db.collection("message").document(uid).collection("Notifications").add(notification);
                                    for (int i=0 ; i < mLastMessage.size() ; i++) {
                                        db.collection("message").document(mLastMessage.get(i).getUid()).collection("Notifications")
                                                .add(notification);
                                    }
                                }
                            }
                        }
                    }
                });
        */
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
        final String rt = sdf.format(calendar.getTime());

        Map<String, Object> message = new HashMap<>();
        message.put("message", autoMessage);
        message.put("email", "momflavortw");
        message.put("uid", "momflavortw");
        message.put("type","message");
        message.put("date", rt);
        message.put("administrator",true);
        db.collection("message").document(uid).collection("message")
                .add(message);


        Map<String,Object> lastmsg = new HashMap<>();
        lastmsg.put("date",rt);
        lastmsg.put("email",email);
        lastmsg.put("from",uid);
        lastmsg.put("read",0);
        db.collection("message").document(uid)
                .update(lastmsg);

    }

    public void setFirst(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("message").document(uid).collection("message").document("firstmessage")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){

                            }else {
                                Calendar calendar = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Taipei"));
                                final String rt = sdf.format(calendar.getTime());
                                Map<String,Object> firstMessage = new HashMap<>();
                                firstMessage.put("message",autoMessage);
                                firstMessage.put("email","momflavortw");
                                firstMessage.put("type","message");
                                firstMessage.put("date",rt);
                                firstMessage.put("administrator",true);
                                db.collection("message").document(uid).collection("message").document("firstmessage")
                                        .set(firstMessage);


                            }
                        }

                    }
                });
    }
}
