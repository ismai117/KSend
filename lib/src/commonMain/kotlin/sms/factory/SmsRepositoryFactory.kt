package sms.factory


import sms.data.repository.SmsRepositoryImpl
import sms.data.service.SmsService
import sms.data.service.SmsServiceImpl
import sms.domain.SmsRepository


object SmsRepositoryFactory {

    private val smsService: SmsService by lazy {
        SmsServiceImpl()
    }

    private val smsRepository: SmsRepository by lazy {
        SmsRepositoryImpl(smsService)
    }

    operator fun invoke(): SmsRepository = smsRepository

}