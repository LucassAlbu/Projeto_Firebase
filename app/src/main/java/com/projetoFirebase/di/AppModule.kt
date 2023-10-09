package com.projetoFirebase.di

import com.projetoFirebase.data.repositories.RepositoryFirebase
import com.projetoFirebase.data.repositories.RepositoryFirebaseImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRepository(
        firebaseAuth: FirebaseAuth,
        database: FirebaseFirestore
    ): RepositoryFirebase {
        return RepositoryFirebaseImpl(firebaseAuth, database)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFireStoreInstance(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

}