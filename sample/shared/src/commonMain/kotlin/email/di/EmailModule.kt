package email.di

import email.data.repository.EmailRepositoryImpl
import email.domain.repository.EmailRepository


object EmailModule {
    val emailRepository: EmailRepository by lazy {
        EmailRepositoryImpl()
    }
}