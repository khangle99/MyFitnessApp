package com.khangle.myfitnessapp.data

import com.khangle.myfitnessapp.data.db.MyFitnessDao
import com.khangle.myfitnessapp.data.network.MyFitnessAppService
import com.khangle.myfitnessapp.data.network.ResponseMessage
import com.khangle.myfitnessapp.model.*
import com.khangle.myfitnessapp.model.user.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MyFitnessAppRepository @Inject constructor(
    private val service: MyFitnessAppService,
    private val dao: MyFitnessDao
) {

    suspend fun fetchExcercise(categoryid: String, excId: String): Excercise {
        return service.fetchExcerciseDetail(categoryid, excId)
    }

    fun getExcerciseCategory(): Flow<List<ExcerciseCategory>> {
        return dao.getExcCategory()
    }

    suspend fun invalidateExcCategory() {
        val excerciseCategory = service.fetchExcerciseCategory()
        dao.invalidateExcCategory(*excerciseCategory.toTypedArray())
    }

    fun getExcerciseList(catId: String): Flow<List<Excercise>> {
        return dao.getExcerciseByCatId(catId)
    }

    suspend fun getExcercise(catId: String, id: String): Excercise {
        return service.fetchExcercise(catId, id)
    }

    suspend fun invalidateExcerciseList(catId: String) {
        val excercises = service.fetchExcerciseList(catId)
        dao.invalidateExcercise(catId, *excercises.toTypedArray())
    }

    //======================== statistic
    fun getStatistic(): Flow<List<UserStat>> {
        return dao.getUserStat()
    }

    suspend fun invalidateStatisticList(uid: String) {
        val statList = service.fetchUserStat(uid)
        dao.invalidateStat(*statList.toTypedArray())
    }
// sao cho nay k co return ??
    suspend fun deleteStat(uid: String, statId: String) {
        service.deleteStat(uid, statId)
        dao.deleteUserStat(statId)
    }

    suspend fun insertStat(uid: String, stat: UserStat): ResponseMessage {
        val res = service.postStat(uid, stat.dateString, stat.weight, stat.height)
        stat.id = res.id!!
        dao.insertUserStat(stat)
        return res
    }
    //======================== other stat
    suspend fun getAppBodyStat(): List<AppBodyStat> {
        return service.fetchAppBodyStat()
    }

    suspend fun getBodyStat(uid: String): List<BodyStat> {
        return service.fetchBodyStat(uid)
    }

    suspend fun deleteOtherStat(uid: String, id: String): ResponseMessage {
        return service.deleteBodyStat(uid, id)
    }

    suspend fun inserOthertStat(uid: String, stat: BodyStat): ResponseMessage {
        return  service.postBodyStat(uid,stat.dateString,stat.value,stat.statId)
    }

    suspend fun updateOthertStat(uid: String, stat: BodyStat): ResponseMessage {
        return  service.updateBodyStat(uid,stat.dateString,stat.value,stat.statId,stat.id)
    }

    //===========================step track
    fun getStepTrack(): Flow<List<UserStep>> {
        return dao.getUserStep()
    }

    suspend fun invalidateStepList(uid: String) {
        val stepList = service.fetchUserStep(uid)
        dao.invalidateStep(*stepList.toTypedArray())
    }

    suspend fun uploadStep(uid: String, step: UserStep): ResponseMessage {
        return service.postStep(uid, step.dateString, step.steps)
    }

    suspend fun insertStep(uid: String, step: UserStep): ResponseMessage {
        val res = service.postStep(uid, step.dateString, step.steps)
        step.id = res.id!!
        dao.insertUserStep(step)
        return res
    }

    suspend fun clearHistory(uid: String): ResponseMessage {
        return service.deleteAllStep(uid)
    }

    //===========================session

    fun getUserSession(): Flow<List<Session>> {
        return dao.getSession()
    }

    suspend fun invalidateSessionList(uid: String) {
        val sessionList = service.fetchUserSession(uid)
        dao.invalidateSession(*sessionList.toTypedArray())
    }

    suspend fun insertSession(uid: String, session: Session): ResponseMessage {
        val res = service.postUserSession(uid, session.name)
        session.id = res.id!!
        dao.insertSession(session)
        return res
    }

    suspend fun updateSession(uid: String, session: Session): ResponseMessage {
        val res = service.updateUserSession(uid, session.name, session.id)
        dao.updateSession(session)
        return res
    }

    suspend fun deleteSession(uid: String, sessId: String): ResponseMessage {
        val res = service.deleteUserSession(uid, sessId)
        dao.deleteSession(sessId)
        return res
    }

    // =========================user excercise

    fun getUserExc(sessId: String): Flow<List<UserExcercise>> { // do chi cache 1 user nen k truyen uid
        return dao.getUserExcerciseBySessionId(sessId)
    }

    suspend fun invalidateUserExcList(uid: String, sessId: String) {
        val sessionList = service.fetchUserExcercise(uid, sessId)
        dao.invalidateUserExcercise(sessId, *sessionList.toTypedArray())
    }

    suspend fun insertUserExc(uid: String, uexc: UserExcercise): ResponseMessage {
        val res = service.postUserExcercise(
            uid,
            uexc.sessionId,
            uexc.noTurn.toString(),
            uexc.noSec.toString(),
            uexc.noGap.toString(),
            uexc.categoryId,
            uexc.excId
        )
        uexc.id = res.id!!
        dao.insertUserExcercise(uexc)
        return res
    }

    suspend fun updateUserExc(uid: String, uexc: UserExcercise): ResponseMessage {
        val res = service.updateUserExcercise(
            uid,
            uexc.sessionId,
            uexc.id,
            uexc.noTurn.toString(),
            uexc.noSec.toString(),
            uexc.noGap.toString()
        )
        dao.updateUserExcercise(uexc)
        return res
    }

    suspend fun deleteUserExc(uid: String, sessId: String, excId: String): ResponseMessage {
        val res = service.deleteUserExcercise(uid, sessId, excId)
        dao.deleteUserExcerciseBySessionId(sessId, excId)
        return res
    }

    suspend fun loadExcerciseList(categoryId: String): List<Excercise> {
        return service.fetchExcerciseList(categoryId)
    }

    suspend fun getStatEnsureList(id: String, catId: String): Map<String,Any> {
        return service.getStatEnsureList(id, catId)
    }


    // planday
    suspend fun loadDayList(uid: String): List<PlanDay> {
        return service.fetchDayList(uid)
    }

    suspend fun updatePlanDay(uid: String, categoryId: String, excId: String, day: String, oldDay: String): ResponseMessage {
        return service.updatePlanDay(uid, categoryId, excId, day, oldDay)
    }

    suspend fun deletePlanDay(uid: String, day: String): ResponseMessage {
        return service.deletePlanDay(uid, day)
    }


    // log excercise day
    suspend fun loadLogOfMonth(uid: String, myStr: String): List<ExcLog> {
        return service.fetchLogOfMonth(uid, myStr)
    }

    suspend fun createMonth(uid: String, mYStr: String): ResponseMessage {
        return service.createMonth(uid, mYStr)
    }
    suspend fun logDay(uid: String, myStr: String, excId: String, categoryId: String, dateInMonth: String): ResponseMessage {
        return service.logDay(uid, myStr,excId,categoryId,dateInMonth)
    }

}