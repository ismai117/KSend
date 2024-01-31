package sms.di


import sms.data.repository.SmsRepositoryImpl
import sms.domain.SmsRepository


object SmsModule {
    val smsRepository: SmsRepository by lazy {
        SmsRepositoryImpl()
    }
}