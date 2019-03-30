package com.example.aspire.projectba.Others;

import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Conexao {
    private static FirebaseAuth fireBaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;

    private Conexao() {
    }

    public static FirebaseAuth getFireBaseAuth() {
        if (fireBaseAuth == null)
            inicializaFirebaseAuth();
        return fireBaseAuth;
    }

    private static void inicializaFirebaseAuth() {
        fireBaseAuth = fireBaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null)
                    firebaseUser = user;
            }
        };
        fireBaseAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public static void logOut() {
        fireBaseAuth.signOut();
    }
}

