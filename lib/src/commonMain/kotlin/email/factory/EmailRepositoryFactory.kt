package email.factory


import email.data.repository.EmailRepositoryImpl
import email.data.service.EmailService
import email.data.service.EmailServiceImpl
import email.domain.repository.EmailRepository
import sms.data.repository.SmsRepositoryImpl
import sms.data.service.SmsService
import sms.data.service.SmsServiceImpl
import sms.domain.SmsRepository


object EmailRepositoryFactory {

    private val emailService: EmailService by lazy {
        EmailServiceImpl()
    }

    private val emailRepository: EmailRepository by lazy {
        EmailRepositoryImpl(emailService)
    }

    operator fun invoke(): EmailRepository = emailRepository

}