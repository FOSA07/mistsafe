package appdev.firebase.config;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
// import com.google.cloud.firestore.Firestore;
// import com.google.cloud.firestore.v1.FirestoreClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

@Configuration
public class FirebaseConfig {

    @Bean
    public static FirebaseApp configureFirebaseConnection() throws IOException {
        
        FileInputStream serviceAccount =
        new FileInputStream("./target/classes/serviceAccountKey.json");
        // FileInputStream serviceAccount =
        // new FileInputStream("./serviceAccountKey.json");

        FirebaseOptions options = new FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        // .setDatabaseUrl("https://eed-grading-system-default-rtdb.europe-west1.firebasedatabase.app")
        .build();
        
        if(FirebaseApp.getApps().isEmpty()){
            FirebaseApp.initializeApp(options);
        }

        return FirebaseApp.getInstance();
        // return FirestoreClient.getFirestore();
        // return FirebaseDatabase.getInstance().getReference();
    }
}
