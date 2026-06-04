package com.example.practico_4.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // Desarrollador B: agregar aquí el provider de SocketRepositorio
}
